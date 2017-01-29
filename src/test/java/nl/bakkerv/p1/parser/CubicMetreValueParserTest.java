package nl.bakkerv.p1.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;

import nl.bakkerv.p1.parser.text.CubicMetreValueParser;
import nl.bakkerv.p1.parser.text.TimestampedValue;
import nl.bakkerv.p1.parser.text.ValueParser;

public class CubicMetreValueParserTest {
	@Test
	public void testParse() throws Exception {
		ValueParser<BigDecimal> parser = new CubicMetreValueParser();
		// Assert.assertEquals(parser.parseCurrentValues("0-1:24.3.0(130206140000)(00)(60)(1)(0-1:24.2.0)(m3)(00047.057)"), new BigDecimal("47.057"));
		Optional<TimestampedValue<BigDecimal>> actual = parser.parse("(00047.057)");
		assertThat(actual.get().getValue()).isEqualTo(new BigDecimal("47.057"));
		assertThat(actual.get().getTimestamp()).isEmpty();
	}
}
