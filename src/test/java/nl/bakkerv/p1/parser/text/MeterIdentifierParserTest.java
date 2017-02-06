package nl.bakkerv.p1.parser.text;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

public class MeterIdentifierParserTest {

	@Test
	public void test() {
		final String expected = "31333631363433322020202020202020";
		Optional<TimestampedValue<String>> actual = new MeterIdentifierParser().parse("(" + expected + ")");
		assertThat(actual).isPresent();
		assertThat(actual.get().getTimestamp()).isEmpty();
		assertThat(actual.get().getValue()).isEqualTo(expected);
	}

}
