package nl.bakkerv.p1.parser;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WattValueParser implements ValueParser<BigDecimal> {

	private Pattern pattern;

	public WattValueParser() {
		this.pattern = Pattern.compile("([0-9]*\\.[0-9]*)\\*kW");
	}

	@Override
	public BigDecimal parse(final String value) {
		BigDecimal result = null;

		Matcher matcher = this.pattern.matcher(value);

		if (matcher.find()) {
			result = new BigDecimal(matcher.group(1)).multiply(new BigDecimal(1000)).setScale(0);
		}

		return result;
	}
}
