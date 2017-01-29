package nl.bakkerv.p1.device;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.bakkerv.p1.domain.measurement.Measurement;
import nl.bakkerv.p1.parser.DatagramParser;

public class P1DatagramListenerImpl implements P1DatagramListener {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private DatagramParser datagramParser;

	private Set<SmartMeterMeasurementListener> listeners = new HashSet<>();

	private Set<Measurement<?>> currentMeasurement;

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

		Set<Measurement<?>> measurement = this.datagramParser.parse(datagram);

		this.currentMeasurement = measurement;
		this.listeners.forEach(l -> l.smartMeterMeasurementRead(measurement));
	}

	public Set<Measurement<?>> getCurrentMeasurements() {
		return this.currentMeasurement;
	}
}
