package nl.bakkerv.p1.device;

import java.util.Set;

import nl.bakkerv.p1.domain.measurement.Measurement;

public interface SmartMeterMeasurementListener {

	public void metersDiscovered(SmartMeterDescription description);

	public void smartMeterMeasurementRead(Set<Measurement<?>> smm);

}
