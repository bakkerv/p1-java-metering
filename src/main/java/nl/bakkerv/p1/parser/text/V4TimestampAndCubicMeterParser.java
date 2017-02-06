package nl.bakkerv.p1.parser.text;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class V4TimestampAndCubicMeterParser implements ValueParser<BigDecimal> {

	// (170128210000W)(03088.130*m3)
	Pattern pattern = Pattern.compile("\\((?<timestamp>[0-9WS]+)\\)\\((?<meterValue>[0-9]*\\.[0-9]*)\\*m3\\)");

	@Override
	public Optional<TimestampedValue<BigDecimal>> parse(final String value) {
		try {
			Matcher matcher = this.pattern.matcher(value);
			if (matcher.matches()) {
				String ts = matcher.group("timestamp");
				String meter = matcher.group("meterValue");
				if (ts == null || meter == null) {
					return Optional.empty();
				}
				TimeZone tz = TimeZone.getDefault();
				P1Timestamp p1Timestamp = new P1Timestamp(ts, tz);
				BigDecimal bd = new BigDecimal(meter);
				return Optional.of(new TimestampedValue<>(Optional.of(p1Timestamp.getZonedDateTime().toInstant()), bd));
			}
			return Optional.empty();
		} catch (InvalidP1TimestampException e) {
			return Optional.empty();
		}
	}

}
