package nl.bakkerv.p1.parser;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import nl.bakkerv.p1.parser.DatagramCleaner;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.StringWriter;

public class DatagramCleanerTest {

	@Test
	public void testAsArray() throws Exception {
		assertThat(19).isEqualTo(DatagramCleaner.asArray(getTestDatagram()).length);
	}

	private String getTestDatagram() {
		StringWriter stringWriter = new StringWriter();
		String result = null;

		try {
			IOUtils.copy(getClass().getResourceAsStream("/test-data.txt"), stringWriter);
			result = stringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
