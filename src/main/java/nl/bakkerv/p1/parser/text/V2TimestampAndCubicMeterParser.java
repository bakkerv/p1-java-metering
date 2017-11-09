package nl.bakkerv.p1.parser.text;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class V2TimestampAndCubicMeterParser implements ValueParser<BigDecimal> {

	// (130206140000)(00)(60)(1)(0-1:24.2.0)(m3)
	// (00047.057)
	final static Pattern pattern = Pattern
			.compile("\\((?<timestamp>[0-9]{12})\\).*\\(m3\\)\\((?<meterValue>[0-9]*\\.[0-9]*)\\)");

	final static String dateTimePattern = "yyMMddHHmmss";
	final static DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().appendPattern(dateTimePattern).toFormatter();

	TimeZone tz;

	public V2TimestampAndCubicMeterParser(final TimeZone tz) {
		this.tz = tz;
	}

	@Override
	public Optional<TimestampedValue<BigDecimal>> parse(final String value) {
		try {
			Matcher matcher = pattern.matcher(value);
			if (matcher.matches()) {
				String ts = matcher.group("timestamp");
				String meter = matcher.group("meterValue");
				if (ts == null || meter == null) {
					return Optional.empty();
				}
				final Instant timestamp = LocalDateTime.parse(ts, dateFormatter).atZone(this.tz.toZoneId()).toInstant();
				BigDecimal bd = new BigDecimal(meter);
				return Optional.of(new TimestampedValue<>(Optional.of(timestamp), bd));
			}
		} catch (DateTimeParseException dtpe) {
		}
		return Optional.empty();
	}

}
