package nl.bakkerv.p1.parser.text;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQueries;
import java.time.zone.ZoneRules;
import java.util.TimeZone;

public class P1Timestamp {
	// YYMMDDhhmmssX and an indication whether DST is active (X=S) or DST is not active (X=W).
	DateTimeFormatter parsePattern = DateTimeFormatter.ofPattern("yyMMddHHmmssXXX");
	DateTimeFormatter printPattern = DateTimeFormatter.ofPattern("yyMMddHHmmss");

	final ZonedDateTime zonedDateTime;

	public P1Timestamp(final Instant timestamp, final TimeZone timezone) {
		this.zonedDateTime = timestamp.atZone(timezone.toZoneId());
	}

	public static P1Timestamp parse(final String p1String, final TimeZone timezone) throws InvalidP1TimestampException {
		return new P1Timestamp(p1String, timezone);
	}

	public static P1Timestamp parse(final String p1String) throws InvalidP1TimestampException {
		return new P1Timestamp(p1String, TimeZone.getDefault());
	}

	public P1Timestamp(final String p1String, final TimeZone timezone) throws InvalidP1TimestampException {
		if (p1String == null) {
			throw new InvalidP1TimestampException("Value cannot be null");
		}
		if (p1String.length() != 13) {
			throw new InvalidP1TimestampException(
					"p1String length incorrect, must be in format YYMMDDhhmmssX "
							+ "and an indication whether DST is active (X=S) or DST is not active (X=W)");
		}
		try {
			String timePart = p1String.substring(0, 12);
			long offset = timezone.getRawOffset(); // basic offset in millis
			if (p1String.endsWith("S") || p1String.endsWith("s")) {
				offset += timezone.getDSTSavings();
			}
			//

			String offsetPart = java.time.ZoneOffset.ofTotalSeconds((int) offset / 1000).getId();
			String properFormat = timePart + offsetPart;
			this.zonedDateTime = ZonedDateTime.parse(properFormat, this.parsePattern);
		} catch (Exception e) {
			throw new InvalidP1TimestampException(e.getMessage());
		}
	}

	public ZonedDateTime getZonedDateTime() {
		return this.zonedDateTime;
	}

	public String asString() {
		ZoneRules rules = this.zonedDateTime.query(TemporalQueries.zoneId()).getRules();
		Duration daylightSavings = rules.getDaylightSavings(this.zonedDateTime.toInstant());
		return this.printPattern.format(this.zonedDateTime) +
				(daylightSavings.getSeconds() > 0 ? "S" : "W");
	}

}