package nl.bakkerv.p1.parser.text;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DSMRVersionParser implements ValueParser<String> {

	private Pattern pattern = Pattern.compile("\\(([0-9]+)\\)");

	@Override
	public Optional<TimestampedValue<String>> parse(final String value) {
		Matcher matcher = this.pattern.matcher(value);

		if (matcher.find()) {
			return Optional.of(new TimestampedValue<>(Optional.empty(), matcher.group(1)));
		}
		return Optional.empty();
	}

}
