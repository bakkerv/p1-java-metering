package nl.bakkerv.p1.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class DatagramCleanerTest {

	private DatagramCleaner cleanerUnderTest;

	@Before
	public void setUp() {
		this.cleanerUnderTest = new DatagramCleaner();
	}

	@Test
	public void asArray() throws Exception {
		assertThat(this.cleanerUnderTest.asArray(getTestDatagram())).hasSize(19);
	}

	@Test
	public void splitDiagram() {
		fail("TODO");
	}

	private String getTestDatagram() {
		StringWriter stringWriter = new StringWriter();
		String result = null;

		try {
			IOUtils.copy(getClass().getResourceAsStream("/test-data-v3.txt"), stringWriter);
			result = stringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}
