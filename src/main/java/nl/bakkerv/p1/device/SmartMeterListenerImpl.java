package nl.bakkerv.p1.device;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.bakkerv.p1.domain.SmartMeterMeasurement;
import nl.bakkerv.p1.parser.DatagramParser;

public class SmartMeterListenerImpl implements SmartMeterListener {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SmartMeterMeasurement currentMeasurement;

	private DatagramParser datagramParser;

	public SmartMeterListenerImpl() {
		this.datagramParser = new DatagramParser();
	}

	@Override
	public void put(final String datagram) {

		if (this.logger.isTraceEnabled()) {
			this.logger.trace(datagram);
		}

		SmartMeterMeasurement measurement = this.datagramParser.parse(datagram);
		measurement.setTimestamp(Instant.now());

		this.currentMeasurement = measurement;
	}

	@Override
	public SmartMeterMeasurement getCurrentMeasurement() {
		return this.currentMeasurement;
	}
}
