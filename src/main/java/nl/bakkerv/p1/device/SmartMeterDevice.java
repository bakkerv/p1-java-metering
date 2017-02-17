package nl.bakkerv.p1.device;

import gnu.io.*;
import nl.bakkerv.p1.configuration.SmartMeterDeviceConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.TooManyListenersException;

public class SmartMeterDevice implements SerialPortEventListener {

	public enum ReaderState {
		Disabled,
		Waiting,
		Reading,
		Checksum
	}

	private static final Logger logger = LoggerFactory.getLogger(SmartMeterDevice.class);

	private SerialPort serialPort;
	private int crc;
	protected ByteBuffer buffer = ByteBuffer.allocate(this.maxBufferSize);
	protected ByteBuffer checksum = ByteBuffer.allocate(10);
	protected ReaderState readerState = ReaderState.Disabled;
	private static final int START_CHARACTER = '/';
	private static final int FINISH_CHARACTER = '!';

	@Inject
	private P1DatagramListener smartMeterListener;

	protected int maxBufferSize = 4096;

	@Inject
	SmartMeterDeviceConfiguration smartMeterConfig;

	public void init() {
		logger.info("Initializing SmartMeterDevice at {}", this.smartMeterConfig.getPortName());
		logger.debug("Port settings: {}", this.smartMeterConfig);
		this.readerState = ReaderState.Disabled;
		this.buffer = ByteBuffer.allocate(this.maxBufferSize);
		try {
			CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(this.smartMeterConfig.getPortName());
			this.serialPort = commPortIdentifier.open("p1meter", this.smartMeterConfig.getPortTimeOut());
			this.serialPort.addEventListener(this);
			this.serialPort.notifyOnDataAvailable(true);
			this.serialPort.setSerialPortParams(this.smartMeterConfig.getSmartMeterPortSettings().getBaudRate(),
					this.smartMeterConfig.getSmartMeterPortSettings().getDataBits().getBits(),
					this.smartMeterConfig.getSmartMeterPortSettings().getStopBits().getStopBits(),
					this.smartMeterConfig.getSmartMeterPortSettings().getParity().getParity());

			logger.info("Finished initializing SmartMeterDevice.");
			this.readerState = ReaderState.Waiting;

		} catch (TooManyListenersException | PortInUseException | UnsupportedCommOperationException | NoSuchPortException e) {
			logger.error(e.toString(), e);
		}

	}

	@Override
	public void serialEvent(final SerialPortEvent serialPortEvent) {
		if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

			try {
				for (int i = 0; i < this.serialPort.getInputStream().available(); i++) {
					int read = this.serialPort.getInputStream().read();
					if (read == -1) {
						logger.debug("No bytes available");
						continue;
					}
					byte c = (byte) read;
					handleCharacter(c);
				}
			}

			catch (IOException e) {
				logger.error(e.toString(), e);
			}
		}
	}

	protected void handleCharacter(final byte c) {
		switch (this.readerState) {
		case Disabled:
			break;
		case Reading:
			this.crc = crc16_update(this.crc, c);
			if (c == FINISH_CHARACTER) {
				this.readerState = ReaderState.Checksum;
				this.checksum.clear();
				logger.debug("Saw ! -> Checksum");
			}
			this.buffer.put(c);
			break;
		case Waiting:
			if (c == START_CHARACTER) {
				logger.debug("Saw /, go to Reading");
				this.readerState = ReaderState.Reading;
				this.buffer.clear();
				this.buffer.put(c);
				this.crc = crc16_update(0, c);
			}
			break;
		case Checksum:
			// we are reading the checksum (optionally, not present in V3)
			this.checksum.put(c);
			if (this.checksum.position() == 4 || c == '\r' || c == '\n') {
				logger.debug("Read {} checksum chars, verify checksum", this.checksum.position());
				boolean checksumCorrect = false;
				if (this.checksum.position() == 4) {
					// done reading checksum
					try {
						String checksumText = new String(this.checksum.array(), 0, this.checksum.position());
						logger.debug("Checksum text, {}", checksumText);
						int receivedCrc16 = Integer.parseInt(checksumText, 16);
						logger.debug("Done reading checksum: {} vs {}", receivedCrc16, this.crc);
						byte[] data = new byte[this.buffer.position()];
						for (int i = 0; i < data.length; i++) {
							data[i] = this.buffer.get(i);
						}
						if (this.crc == receivedCrc16) {
							logger.debug("Checkum is correct");
							checksumCorrect = true;
						}
					} catch (Exception e) {
						logger.error("Could not verify checksum {}", e.getMessage(), e);
					}
				}
				if (checksumCorrect || this.checksum.position() < 4 && (c == '\r' || c == '\n')) {
					String datagram = new String(this.buffer.array(), 0, this.buffer.position());
					logger.debug("read datagram: {}", datagram);
					this.smartMeterListener.put(datagram);
				}

				this.buffer.clear();
				this.checksum.clear();
				this.readerState = ReaderState.Waiting;
				break;
			}
		}
	}

	int crc16_update(int crc, final byte a) {
		crc ^= a;
		for (int i = 0; i < 8; ++i) {
			if ((crc & 1) != 0) {
				crc = crc >> 1 & 0xFFFF ^ 0xA001;
			} else {
				crc = crc >> 1;
			}
		}
		return crc;
	}

}
