package nl.bakkerv.p1.parser.text;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

public class MeterTypeParserTest {

	@Test
	public void test() {
		Optional<TimestampedValue<MeterType>> actual = new MeterTypeParser().parse("(03)");
		assertThat(actual).isPresent();
		assertThat(actual.get().getTimestamp()).isEmpty();
		assertThat(actual.get().getValue()).isEqualTo(MeterType.GAS);
	}

}
