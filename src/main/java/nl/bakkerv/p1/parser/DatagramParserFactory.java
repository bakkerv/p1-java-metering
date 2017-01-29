package nl.bakkerv.p1.parser;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.bakkerv.p1.domain.meter.Direction;
import nl.bakkerv.p1.domain.meter.Meter;
import nl.bakkerv.p1.domain.meter.Unit;
import nl.bakkerv.p1.parser.text.DSMRVersionParser;
import nl.bakkerv.p1.parser.text.MeterIdentifierParser;
import nl.bakkerv.p1.parser.text.TimestampedValue;
import nl.bakkerv.p1.parser.text.ValueParser;
import nl.bakkerv.p1.parser.text.WattValueParser;

public class DatagramParserFactory {

	@Inject
	private DatagramCleaner datagramCleaner;

	private static final Logger logger = LoggerFactory.getLogger(DatagramParserFactory.class);

	public Optional<DatagramParser> create(final String dataGram) {
		DatagramParser newParser = new DatagramParser();
		Map<String, String> cleanedUp = this.datagramCleaner.splitDiagram(dataGram);
		Optional<String> dsmrVersion = extractField(cleanedUp, DatagramCodes.DSMR_VERSION, DSMRVersionParser::new);
		Optional<String> meterID = extractField(cleanedUp, DatagramCodes.SMART_METER_ID, MeterIdentifierParser::new);
		if (!dsmrVersion.isPresent() || !meterID.isPresent()) {
			logger.error("No DSMR version and/or meter identifier present");
			return Optional.empty();
		}
		if (cleanedUp.containsKey(DatagramCodes.ELECTRICITY_CURRENT_POWER_CONSUMPTION)) {
			newParser.addPropertyParser(DatagramCodes.ELECTRICITY_CURRENT_POWER_CONSUMPTION,
					Meter.<Integer> builder()
							.withChannel("Total")
							.withIdentifier(meterID.get())
							.withParser(new WattValueParser())
							.withUnit(Unit.WATT)
							.withVersion("DSMR-" + dsmrVersion.get())
							.withDirection(Direction.TO_CLIENT)
							.build());
		}
		if (cleanedUp.containsKey(DatagramCodes.ELECTRICITY_CURRENT_POWER_PRODUCTION)) {
			newParser.addPropertyParser(DatagramCodes.ELECTRICITY_CURRENT_POWER_PRODUCTION,
					Meter.<Integer> builder()
							.withChannel("Total")
							.withIdentifier(meterID.get())
							.withParser(new WattValueParser())
							.withUnit(Unit.WATT)
							.withVersion("DSMR-" + dsmrVersion.get())
							.withDirection(Direction.FROM_CLIENT)
							.build());
		}

		return Optional.of(newParser);
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
