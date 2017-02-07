package nl.bakkerv.p1.device;

import java.util.Collection;
import java.util.Set;

import nl.bakkerv.p1.domain.measurement.Measurement;
import nl.bakkerv.p1.domain.meter.Meter;

public interface SmartMeterMeasurementListener {

	public void metersDiscovered(Collection<Meter<?>> meters);

	public void smartMeterMeasurementRead(Set<Measurement<?>> smm);

}
