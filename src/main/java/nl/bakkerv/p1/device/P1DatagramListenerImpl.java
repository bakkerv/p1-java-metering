package nl.bakkerv.p1.device;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import nl.bakkerv.p1.domain.measurement.Measurement;
import nl.bakkerv.p1.parser.DatagramParser;
import nl.bakkerv.p1.parser.DatagramParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class P1DatagramListenerImpl implements P1DatagramListener {

	private static final Logger logger = LoggerFactory.getLogger(P1DatagramListenerImpl.class);

	private DatagramParser datagramParser = null;

	@Inject
	private Set<SmartMeterMeasurementListener> listeners;
	@Inject
	private DatagramParserFactory parserFactory;

	private Set<Measurement<?>> currentMeasurement;

	private Map<SmartMeterMeasurementListener, ExecutorService> listenerWorkerPool = Maps.newIdentityHashMap();

	@Override
	public void put(final String datagram) {

		if (logger.isTraceEnabled()) {
			logger.trace(datagram);
		}

		if (this.datagramParser == null) {
			logger.info("No parser configured yet, create datagram parser based on seen datagram");
			Optional<DatagramParser> datagramParserOpt = this.parserFactory.create(datagram);
			if (datagramParserOpt.isPresent()) {
				this.datagramParser = datagramParserOpt.get();
				logger.info("Created {}", this.datagramParser);
				this.listeners.forEach(s -> s.metersDiscovered(this.datagramParser));
			} else {
				logger.warn("Could not create parser for datagram, ignoring datagram");
				return;
			}
		}

		Set<Measurement<?>> measurement = this.datagramParser.parse(datagram);

		this.currentMeasurement = measurement;
		logger.debug("Listeners {}", this.listeners);
		for (SmartMeterMeasurementListener sml : this.listeners) {
			try {
				final ExecutorService executorService = listenerWorkerPool.computeIfAbsent(sml, key -> Executors.newFixedThreadPool(1));
				executorService.submit(() -> sml.smartMeterMeasurementRead(measurement));
			} catch (Throwable t) {
				logger.error("Could not notify listener {}: {}", sml, t.getMessage());
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		for (ExecutorService executorService : this.listenerWorkerPool.values()) {
			executorService.shutdown();
		}


	}

	public Set<Measurement<?>> getCurrentMeasurements() {
		return this.currentMeasurement;
	}
}
