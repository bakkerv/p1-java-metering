package nl.bakkerv.p1.parser.text;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import org.junit.Test;

public class DSMRVersionParserTest {

	@Test
	public void test() {
		Optional<TimestampedValue<String>> actual = new DSMRVersionParser().parse("(42)");
		assertThat(actual).isPresent();
		assertThat(actual.get().getTimestamp()).isEmpty();
		assertThat(actual.get().getValue()).isEqualTo("42");
	}

}
