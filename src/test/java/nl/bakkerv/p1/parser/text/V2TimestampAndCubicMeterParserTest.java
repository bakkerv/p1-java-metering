package nl.bakkerv.p1.parser.text;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.TimeZone;

import org.junit.Test;

public class V2TimestampAndCubicMeterParserTest {

	@Test
	public void test() {
		V2TimestampAndCubicMeterParser parser = new V2TimestampAndCubicMeterParser(TimeZone.getTimeZone("Europe/Amsterdam"));
		Optional<TimestampedValue<BigDecimal>> actual = parser.parse("(130206140000)(00)(60)(1)(0-1:24.2.0)(m3)(00047.057)");
		assertThat(actual).isPresent();
		Optional<Instant> actualTimestamp = actual.get().getTimestamp();
		assertThat(actualTimestamp).isPresent();
		assertThat(actualTimestamp.get()).isEqualTo(Instant.ofEpochSecond(1360155600l));
		assertThat(actual.get().getValue()).isEqualTo(new BigDecimal("00047.057"));
	}

}
