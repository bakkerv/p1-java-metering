package nl.bakkerv.p1.device;

import java.util.Set;

import nl.bakkerv.p1.domain.measurement.Measurement;
import nl.bakkerv.p1.parser.DatagramParser;

public interface SmartMeterMeasurementListener {

	public void metersDiscovered(DatagramParser parser);

	public void smartMeterMeasurementRead(Set<Measurement<?>> smm);

}
