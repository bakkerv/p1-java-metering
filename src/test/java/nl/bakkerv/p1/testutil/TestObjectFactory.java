package nl.bakkerv.p1.testutil;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.StringWriter;

public class TestObjectFactory {

	public static String getTestV3Datagram() {
		StringWriter stringWriter = new StringWriter();
		String result = null;

		try {
			IOUtils.copy(TestObjectFactory.class.getResourceAsStream("/test-data-v3.txt"), stringWriter);
			result = stringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String getTestV5Datagram() {
		StringWriter stringWriter = new StringWriter();
		String result = null;

		try {
			IOUtils.copy(TestObjectFactory.class.getResourceAsStream("/test-data-v5.txt"), stringWriter);
			result = stringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
