package nl.bakkerv.p1.device;

import nl.bakkerv.p1.domain.SmartMeterMeasurement;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;

public class SmartMeterListenerMock implements SmartMeterListener {

	private SmartMeterMeasurement smartMeterMeasurement;
	private Random random = new Random();

	public void init() {
		this.smartMeterMeasurement = new SmartMeterMeasurement();
		this.smartMeterMeasurement.setTimestamp(Instant.now());
		this.smartMeterMeasurement.setCurrentPowerProductionW(new BigDecimal(100));
		this.smartMeterMeasurement.setCurrentPowerProductionW(new BigDecimal(0));

		loadDummyData();
	}

	private void loadDummyData() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					sleep(10000);
					SmartMeterListenerMock.this.smartMeterMeasurement.setTimestamp(Instant.now());
					SmartMeterListenerMock.this.smartMeterMeasurement
							.setCurrentPowerConsumptionW(new BigDecimal(SmartMeterListenerMock.this.random.nextInt(2500)));
					SmartMeterListenerMock.this.smartMeterMeasurement
							.setCurrentPowerProductionW(new BigDecimal(SmartMeterListenerMock.this.random.nextInt(1600)));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	@Override
	public void put(final String datagram) {
	}

	@Override
	public SmartMeterMeasurement getCurrentMeasurement() {
		return this.smartMeterMeasurement;
	}
}
