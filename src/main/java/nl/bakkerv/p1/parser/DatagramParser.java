package nl.bakkerv.p1.parser;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.Sets;

import nl.bakkerv.p1.domain.measurement.Measurement;
import nl.bakkerv.p1.domain.meter.Meter;

public class DatagramParser {

	private Map<String, Meter<?>> mapping;

	@Inject
	DatagramCleaner datagramCleaner;

	public DatagramParser() {
		this.mapping = new HashMap<>();
	}

	public <T> void addPropertyParser(final String id, final Meter<?> meter) {
		this.mapping.put(id, meter);
	}

	public Set<Measurement<?>> parse(final String datagram) {
		Set<Measurement<?>> returnValue = Sets.newHashSet();
		Map<String, String> datagramLines = cleanupAndSplitDatagram(datagram);
		Instant timestamp = datagramLines.containsKey(DatagramCodes.TIMESTAMP) ? Instant.now() : Instant.now(); // FIXME: extract timestamp
		for (Map.Entry<String, String> e : datagramLines.entrySet()) {
			Meter<?> meter = this.mapping.get(e.getKey());
			Optional<?> extractedMeasurement = meter.extractMeasurement(timestamp, e.getValue());
			if (extractedMeasurement.isPresent()) {
				returnValue.add((Measurement<?>) extractedMeasurement.get());
			}
		}
		return returnValue;
	}

	private Map<String, String> cleanupAndSplitDatagram(final String datagram) {
		return this.datagramCleaner.splitDiagram(datagram).entrySet().stream()
				.filter(s -> this.mapping.containsKey(s.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
