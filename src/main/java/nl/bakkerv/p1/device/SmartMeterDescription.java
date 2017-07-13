package nl.bakkerv.p1.device;

import java.util.Map;

import nl.bakkerv.p1.domain.meter.Meter;

public interface SmartMeterDescription {

	public String getVersion();

	public String getVendorInformation();

	public String getMeterIdentifier();

	public Map<String, Meter<?>> getMapping();

}
