package nl.bakkerv.p1.device;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.bakkerv.p1.domain.SmartMeterMeasurement;
import nl.bakkerv.p1.parser.DatagramParser;

public class P1DatagramListenerImpl implements P1DatagramListener {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SmartMeterMeasurement currentMeasurement;

	private DatagramParser datagramParser;

	private Set<SmartMeterMeasurementListener> listeners = new HashSet<>();

	public P1DatagramListenerImpl() {
		this.datagramParser = new DatagramParser();
	}

	public void addListener(final SmartMeterMeasurementListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void put(final String datagram) {

		if (this.logger.isTraceEnabled()) {
			this.logger.trace(datagram);
		}

		SmartMeterMeasurement measurement = this.datagramParser.parse(datagram);
		if (measurement.getTimestamp() == null) {
			measurement.setTimestamp(Instant.now());
		}

		this.currentMeasurement = measurement;
		this.listeners.forEach(l -> l.smartMeterMeasurementRead(measurement));
	}

	public SmartMeterMeasurement getCurrentMeasurement() {
		return this.currentMeasurement;
	}
}
