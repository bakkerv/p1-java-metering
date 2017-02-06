package nl.bakkerv.p1.parser.text;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;

public class SimpleDecimalParserTest {

	@Test
	public void test() {
		Optional<TimestampedValue<BigDecimal>> actual = new SimpleDecimalParser("V").parse("(001.300*V)");
		assertThat(actual).isPresent();
		assertThat(actual.get().getTimestamp()).isEmpty();
		assertThat(actual.get().getValue()).isEqualTo(new BigDecimal("001.300"));
	}

}
