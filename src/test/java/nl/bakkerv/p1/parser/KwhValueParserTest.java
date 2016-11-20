package nl.bakkerv.p1.parser;

import nl.bakkerv.p1.parser.KwhValueParser;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class KwhValueParserTest {
	@Test
	public void testParse() throws Exception {
		KwhValueParser parser = new KwhValueParser();
		assertThat(parser.parse("1-0:1.8.1(00038.113*kWh)")).isEqualTo(new BigDecimal("38.113"));
	}
}
