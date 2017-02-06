package nl.bakkerv.p1.parser.text;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;

public class AmpParserTest {

	@Test
	public void test() {
		Optional<TimestampedValue<BigDecimal>> actual = new AmpParser().parse("(0001*A)");
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get().getTimestamp()).isEmpty();
		assertThat(actual.get().getValue()).isEqualTo(new BigDecimal("1"));
	}

}
