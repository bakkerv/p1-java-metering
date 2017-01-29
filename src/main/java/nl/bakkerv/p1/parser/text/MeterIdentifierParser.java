package nl.bakkerv.p1.parser.text;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeterIdentifierParser implements ValueParser<String> {

	private Pattern pattern;

	public MeterIdentifierParser() {
		this.pattern = Pattern.compile("\\((<?id>(.+)\\)");
	}

	@Override
	public Optional<TimestampedValue<String>> parse(final String value) {
		Matcher matcher = this.pattern.matcher(value);
		if (matcher.matches()) {
			return Optional.of(new TimestampedValue<>(Optional.empty(), matcher.group("id")));
		}
		return Optional.empty();
	}

}
