package nl.bakkerv.p1.testutil;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

public class TestObjectFactory {

	public static String getTestV3Datagram() {
		return getResource("/test-data-v3.txt");
	}

	public static String getTestAMDatagram() {
		return getResource("/test-data-am.txt");
	}

	public static String getTestV4Datagram() {
		return getResource("/test-data-v4.txt");
	}

	public static String getTestV5Datagram() {
		return getResource("/test-data-v5.txt");
	}

	private static String getResource(final String filename) {
		StringWriter stringWriter = new StringWriter();
		String result = null;

		try {
			IOUtils.copy(TestObjectFactory.class.getResourceAsStream(filename), stringWriter);
			result = stringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}
