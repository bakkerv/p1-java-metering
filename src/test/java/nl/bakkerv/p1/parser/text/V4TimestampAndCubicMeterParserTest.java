package nl.bakkerv.p1.parser.text;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.Test;

public class V4TimestampAndCubicMeterParserTest {

	@Test
	public void test() {
		Optional<TimestampedValue<BigDecimal>> actual = new V4TimestampAndCubicMeterParser().parse("(101209112500W)(12785.123*m3)");
		assertThat(actual).isPresent();
		Optional<Instant> actualTimestamp = actual.get().getTimestamp();
		assertThat(actualTimestamp).isPresent();
		assertThat(actualTimestamp.get()).isEqualTo(Instant.ofEpochSecond(1291890300l));
		assertThat(actual.get().getValue()).isEqualTo(new BigDecimal("12785.123"));
	}

}
