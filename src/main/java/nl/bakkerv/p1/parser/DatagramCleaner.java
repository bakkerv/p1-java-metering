package nl.bakkerv.p1.parser;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.assertj.core.util.Maps;

public class DatagramCleaner {

	private static final String OBIS_ID = "obisID";
	private static final String VALUE = "value";
	private static final Pattern P1_DATAGRAM_PATTERN = Pattern.compile("(?<" + OBIS_ID + ">\\d+-\\d+:\\d+\\.\\d+\\.\\d+)(?<" + VALUE + ">\\(.*\\))");

	public String[] asArray(String source) {

		// Remove windowsy line ends
		source = source.replaceAll("\\r", "");

		// Put gas measurements on one line
		source = source.replaceAll("\\(m3\\)\\n", "(m3)");

		return source.split("\\n");
	}

	public Map<String, String> splitDiagram(final String datagram) {
		String[] datagramLines = this.asArray(datagram);
		Map<String, String> lines = Maps.newHashMap();

		for (String line : datagramLines) {
			Matcher matcher = P1_DATAGRAM_PATTERN.matcher(line);
			String obis = matcher.group(OBIS_ID);
			if (obis == null) {
				continue;
			}
			String value = matcher.group(VALUE);
			if (value != null) {
				lines.put(obis, value);
			}
		}
		return lines;
	}
}
