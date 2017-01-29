package nl.bakkerv.p1.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import nl.bakkerv.p1.parser.text.WattValueParser;

public class WattValueParserTest {
	@Test
	public void testParse() throws Exception {
		WattValueParser parser = new WattValueParser();
		assertThat(parser.parse("1-0:1.7.0(0000.55*kW)")).isEqualTo(new BigDecimal("550"));
	}
}
