package nl.bakkerv.p1.device;

import nl.bakkerv.p1.domain.SmartMeterMeasurement;

public interface SmartMeterListener {
    void put(String datagram);

    SmartMeterMeasurement getCurrentMeasurement();
}
