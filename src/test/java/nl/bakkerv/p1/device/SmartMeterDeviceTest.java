package nl.bakkerv.p1.device;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;

import org.junit.Test;

import nl.bakkerv.p1.device.SmartMeterDevice.ReaderState;
import nl.bakkerv.p1.testutil.TestObjectFactory;

public class SmartMeterDeviceTest implements P1DatagramListener {

	private String readDatagram = null;

	@Test
	public void testReadingFromPort() {
		SmartMeterDevice smd = new SmartMeterDevice();
		String dataGram = TestObjectFactory.getTestV3Datagram();
		smd.buffer = ByteBuffer.allocate(smd.maxBufferSize);
		smd.readerState = ReaderState.Waiting;
		smd.setSmartMeterListener(this);
		dataGram.chars().forEachOrdered(e -> smd.handleCharacter((byte) e));
		String trimmed = this.readDatagram.trim();
		assertThat(trimmed).isEqualTo(dataGram.trim());
	}

	@Override
	public void put(final String datagram) {
		this.readDatagram = datagram;
	}

}
