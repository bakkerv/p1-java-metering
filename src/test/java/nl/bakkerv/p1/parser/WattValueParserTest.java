package nl.bakkerv.p1.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

import nl.bakkerv.p1.parser.text.TimestampedValue;
import nl.bakkerv.p1.parser.text.WattValueParser;

public class WattValueParserTest {
	@Test
	public void testParse() throws Exception {
		WattValueParser parser = new WattValueParser();
		Optional<TimestampedValue<Integer>> actual = parser.parse("(0000.55*kW)");
		assertThat(actual.get().getValue()).isEqualTo(550);
		assertThat(actual.get().getTimestamp()).isEmpty();
	}
}
