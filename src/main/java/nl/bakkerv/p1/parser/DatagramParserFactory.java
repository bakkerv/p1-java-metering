package nl.bakkerv.p1.parser;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimaps;

import nl.bakkerv.p1.domain.meter.Direction;
import nl.bakkerv.p1.domain.meter.Kind;
import nl.bakkerv.p1.domain.meter.Meter;
import nl.bakkerv.p1.domain.meter.MeterType;
import nl.bakkerv.p1.domain.meter.Unit;
import nl.bakkerv.p1.parser.DatagramParser.Builder;
import nl.bakkerv.p1.parser.text.DSMRVersionParser;
import nl.bakkerv.p1.parser.text.KwhValueParser;
import nl.bakkerv.p1.parser.text.MeterIdentifierParser;
import nl.bakkerv.p1.parser.text.V4TimestampAndCubicMeterParser;
import nl.bakkerv.p1.parser.text.TimestampedValue;
import nl.bakkerv.p1.parser.text.ValueParser;
import nl.bakkerv.p1.parser.text.WattValueParser;

public class DatagramParserFactory {

	private static final Pattern VALUE_EXPRESSION = Pattern.compile("\\((.+\\))");

	@Inject
	private DatagramCleaner datagramCleaner;

	private static final Logger logger = LoggerFactory.getLogger(DatagramParserFactory.class);

	public Optional<DatagramParser> create(final String dataGram) {
		Builder newParser = DatagramParser.builder();
		Map<String, String> cleanedUp = this.datagramCleaner.splitDiagram(dataGram);
		Optional<String> dsmrVersion = extractField(cleanedUp, DatagramCodes.DSMR_VERSION, DSMRVersionParser::new);
		Optional<String> meterID = extractField(cleanedUp, DatagramCodes.SMART_METER_ID, MeterIdentifierParser::new);
		if (!dsmrVersion.isPresent() || !meterID.isPresent()) {
			logger.error("No DSMR version and/or meter identifier present");
			return Optional.empty();
		}
		newParser.withVersion("DSMR-" + dsmrVersion.get());
		newParser.withMeterIdentifier(meterID.get());
		if (cleanedUp.containsKey(DatagramCodes.ELECTRICITY_CURRENT_POWER_CONSUMPTION)) {
			addCurrentPowerMeter(newParser, Direction.TO_CLIENT);
		}
		if (cleanedUp.containsKey(DatagramCodes.ELECTRICITY_CURRENT_POWER_PRODUCTION)) {
			addCurrentPowerMeter(newParser, Direction.FROM_CLIENT);
		}
		if (cleanedUp.containsKey(DatagramCodes.ELECTRICITY_CONSUMPTION_RATE_1)) {
			generatekWhMeter(newParser, Direction.TO_CLIENT, 1);
		}
		if (cleanedUp.containsKey(DatagramCodes.ELECTRICITY_CONSUMPTION_RATE_2)) {
			generatekWhMeter(newParser, Direction.TO_CLIENT, 2);
		}
		if (cleanedUp.containsKey(DatagramCodes.ELECTRICITY_PRODUCTION_RATE_1)) {
			generatekWhMeter(newParser, Direction.FROM_CLIENT, 1);
		}
		if (cleanedUp.containsKey(DatagramCodes.ELECTRICITY_PRODUCTION_RATE_2)) {
			generatekWhMeter(newParser, Direction.FROM_CLIENT, 2);
		}
		ImmutableListMultimap<String, Entry<String, String>> perBusID = Multimaps.index(cleanedUp.entrySet(), s -> s.getKey().split(":")[0]);
		ImmutableSet<String> keysToIgnore = ImmutableSet.of("1-3", "0-0", "1-0");
		perBusID.asMap().entrySet().stream().filter(s -> !keysToIgnore.contains(s.getKey())).forEach(s -> extractMeter(newParser, s.getValue()));

		return Optional.of(newParser.build());
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private void extractMeter(final Builder newParser, final Collection<Entry<String, String>> values) {
		Kind kind = null;
		String identifier = null;
		ValueParser parser = new V4TimestampAndCubicMeterParser();
		String obisCode = null;
		for (Entry<String, String> e : values) {
			String code = e.getKey().split(":")[1];
			if ("24.1.0".equals(code)) {
				Matcher matcher = VALUE_EXPRESSION.matcher(e.getKey());
				if (!matcher.matches()) {
					logger.warn("{} does not match expected pattern", e.getKey());
					// no meter type known, abort
					return;
				}
				int type = Integer.parseInt(matcher.group(0));
				if (type != 3) {
					logger.warn("Unknown meter type {}", type);
					return;
				}
				kind = Kind.GAS;
			}
			if ("96.1.0".equals(code)) {
				Matcher matcher = VALUE_EXPRESSION.matcher(e.getKey());
				if (!matcher.matches()) {
					logger.warn("{} does not match expected pattern", e.getKey());
					// no meter type known, abort
					return;
				}
			}
			if ("24.2.1".equals(code)) {
				obisCode = e.getKey();
				parser = new V4TimestampAndCubicMeterParser();
			}
		}
		if (parser != null && kind != null && identifier != null) {
			newParser.addPropertyParser(obisCode, Meter.builder()
					.withKind(kind)
					.withIdentifier(identifier)
					.withMeterType(MeterType.INTEGRAL)
					.withParser(parser)
					.build());
		}
	}

	private void generatekWhMeter(final Builder newParser, final Direction direction, final int tariff) {
		newParser.addPropertyParser(DatagramCodes.ELECTRICITY_CONSUMPTION_RATE_1,
				Meter.<BigDecimal> builder()
						.withUnit(Unit.KILOWATTHOUR)
						.withKind(Kind.ELECTRICITY)
						.withMeterType(MeterType.INTEGRAL)
						.withDirection(direction)
						.withParser(new KwhValueParser())
						.withTariff(tariff)
						.build());
	}

	private void addCurrentPowerMeter(final Builder newParser, final Direction d) {
		newParser.addPropertyParser(DatagramCodes.ELECTRICITY_CURRENT_POWER_CONSUMPTION,
				Meter.<Integer> builder()
						.withChannel("Total")
						.withParser(new WattValueParser())
						.withUnit(Unit.WATT)
						.withKind(Kind.ELECTRICITY)
						.withMeterType(MeterType.INSTANTANEOUS)
						.withDirection(d)
						.build());
	}

	private <T> Optional<T> extractField(final Map<String, String> cleanedUp, final String field, final Supplier<ValueParser<T>> parserSupplier) {
		if (cleanedUp.containsKey(field)) {
			final Optional<TimestampedValue<T>> value = parserSupplier.get().parse(cleanedUp.get(field));
			if (!value.isPresent()) {
				return Optional.empty();
			}
			return Optional.of(value.get().getValue());
		} else {
			return Optional.empty();
		}
	}

}
