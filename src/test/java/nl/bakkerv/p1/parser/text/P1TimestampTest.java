package nl.bakkerv.p1.parser.text;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Instant;
import java.util.TimeZone;

import org.junit.Test;

public class P1TimestampTest {

	final static String P1_T1 = "161030020000S";
	final static Instant T1 = Instant.ofEpochSecond(1477785600);
	final static String P1_T2 = "161030020000W";
	final static Instant T2 = Instant.ofEpochSecond(1477789200);
	final static String P1_T3 = "161116084324W";
	final static Instant T3 = Instant.ofEpochSecond(1479282204);
	final static TimeZone timeZone = TimeZone.getTimeZone("Europe/Amsterdam");

	@Test
	public void P1TimestampStringTimeZone() throws InvalidP1TimestampException {

		P1Timestamp actual = new P1Timestamp(P1_T1, timeZone);
		assertThat(actual.getZonedDateTime().toInstant()).isEqualTo(T1);
		actual = new P1Timestamp(P1_T2, timeZone);
		assertThat(actual.getZonedDateTime().toInstant()).isEqualTo(T2);
		actual = new P1Timestamp(P1_T3, timeZone);
		assertThat(actual.getZonedDateTime().toInstant()).isEqualTo(T3);
	}

	@Test
	public void testAsString() {
		assertThat(new P1Timestamp(T1, timeZone).asString()).isEqualTo(P1_T1);
		assertThat(new P1Timestamp(T2, timeZone).asString()).isEqualTo(P1_T2);
		assertThat(new P1Timestamp(T3, timeZone).asString()).isEqualTo(P1_T3);
	}
}
