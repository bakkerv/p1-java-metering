package nl.bakkerv.p1.device;

import nl.bakkerv.p1.domain.SmartMeterMeasurement;

public interface SmartMeterMeasurementListener {

	public void smartMeterMeasurementRead(SmartMeterMeasurement smm);

}
