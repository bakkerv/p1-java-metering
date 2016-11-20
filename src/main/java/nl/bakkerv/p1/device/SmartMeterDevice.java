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
		WAITING_FOR_START,
		READING_DATAGRAM,
		WAITING_CHECKSUM
	}

	private static final Logger logger = LoggerFactory.getLogger(SmartMeterDevice.class);

	private SerialPort serialPort;
	private ByteBuffer readByteBuffer;
	private final int START_CHARACTER = '/';
	private final int FINISH_CHARACTER = '!';

	private SmartMeterListener smartMeterListener;

	protected int maxBufferSize = 1024;

	@Inject
	SmartMeterDeviceConfiguration smartMeterConfig;

	public void init() {
		logger.info("Initializing SmartMeterDevice at {}", this.smartMeterConfig.getPortName());
		logger.debug("Port settings: {}", this.smartMeterConfig);
		try {
			CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(this.smartMeterConfig.getPortName());
			this.serialPort = (SerialPort) commPortIdentifier.open("p1meter", this.smartMeterConfig.getPortTimeOut());
			this.serialPort.addEventListener(this);
			this.serialPort.notifyOnDataAvailable(true);
			this.serialPort.setSerialPortParams(this.smartMeterConfig.getSmartMeterPortSettings().getBaudRate(),
					this.smartMeterConfig.getSmartMeterPortSettings().getDataBits().getBits(),
					this.smartMeterConfig.getSmartMeterPortSettings().getStopBits().getStopBits(),
					this.smartMeterConfig.getSmartMeterPortSettings().getParity().getParity());
			this.readByteBuffer = ByteBuffer.allocate(this.maxBufferSize);

			logger.info("Finished initializing SmartMeterDevice.");

		} catch (TooManyListenersException | PortInUseException | UnsupportedCommOperationException | NoSuchPortException e) {
			logger.error(e.toString(), e);
		}

	}

	@Override
	public void serialEvent(final SerialPortEvent serialPortEvent) {
		if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

			try {
				int nextByte;

				while ((nextByte = this.serialPort.getInputStream().read()) != -1) {
					this.readByteBuffer.put((byte) nextByte);

					if (nextByte == this.FINISH_CHARACTER) {
						this.smartMeterListener.put(new String(this.readByteBuffer.array()));
						this.readByteBuffer.clear();
					}
				}
			} catch (IOException e) {
				logger.error(e.toString(), e);
			}
		}
	}

	public void setSmartMeterListener(final SmartMeterListener smartMeterListener) {
		this.smartMeterListener = smartMeterListener;
	}
}
