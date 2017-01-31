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

	private String vendorInformation;
	private String version;

	@Inject
	DatagramCleaner datagramCleaner;

	private String meterIdentifier;

	public DatagramParser() {
		this.mapping = new HashMap<>();
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

	public String getVersion() {
		return this.version;
	}

	public String getVendorInformation() {
		return this.vendorInformation;
	}

	public String getMeterIdentifier() {
		return this.meterIdentifier;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		DatagramParser instance = new DatagramParser();

		public Builder withMeterIdentifier(final String meterID) {
			this.instance.meterIdentifier = meterID;
			return this;
		}

		public Builder withVendorInformation(final String vendorInfo) {
			this.instance.vendorInformation = vendorInfo;
			return this;
		}

		public Builder withVersion(final String version) {
			this.instance.version = version;
			return this;
		}

		public DatagramParser build() {
			DatagramParser returnValue = this.instance;
			this.instance = null;
			return returnValue;
		}

		public Builder addPropertyParser(final String id, final Meter<?> meter) {
			this.instance.mapping.put(id, meter);
			return this;
		}

	}

}
