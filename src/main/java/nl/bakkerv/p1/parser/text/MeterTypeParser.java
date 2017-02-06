package nl.bakkerv.p1.parser.text;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeterTypeParser implements ValueParser<MeterType> {

	Pattern pattern = Pattern.compile("\\(([0-9]+)\\)");

	@Override
	public Optional<TimestampedValue<MeterType>> parse(final String value) {
		try {
			Matcher matcher = this.pattern.matcher(value);
			matcher.find();
			MeterType fromId = MeterType.fromId(Integer.parseInt(matcher.group(1)));
			return Optional.of(new TimestampedValue<>(Optional.empty(), fromId));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
