package nl.bakkerv.p1.parser.text;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleDecimalParser implements ValueParser<BigDecimal> {

	private Pattern pattern;

	public SimpleDecimalParser(final String unit) {
		this.pattern = Pattern.compile(String.format("([0-9]*(\\.[0-9]*)?)\\*%s", unit));
	}

	@Override
	public Optional<TimestampedValue<BigDecimal>> parse(final String value) {
		BigDecimal result = null;

		Matcher matcher = this.pattern.matcher(value);

		if (matcher.find()) {
			result = new BigDecimal(matcher.group(1));
		}

		return result == null ? Optional.empty() : Optional.of(new TimestampedValue<>(Optional.empty(), result));
	}

}
