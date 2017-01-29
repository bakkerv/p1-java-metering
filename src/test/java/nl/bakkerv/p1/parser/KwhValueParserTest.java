package nl.bakkerv.p1.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;

import nl.bakkerv.p1.parser.text.KwhValueParser;
import nl.bakkerv.p1.parser.text.TimestampedValue;

public class KwhValueParserTest {
	@Test
	public void testParse() throws Exception {
		KwhValueParser parser = new KwhValueParser();
		Optional<TimestampedValue<BigDecimal>> actual = parser.parse("(00038.113*kWh)");
		assertThat(actual.get().getValue()).isEqualTo(new BigDecimal("38.113"));
		assertThat(actual.get().getTimestamp()).isEmpty();
	}
}
