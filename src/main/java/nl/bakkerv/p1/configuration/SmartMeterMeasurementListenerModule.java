package nl.bakkerv.p1.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import nl.bakkerv.p1.device.SmartMeterMeasurementListener;

public class SmartMeterMeasurementListenerModule<T extends SmartMeterMeasurementListener> extends AbstractModule {

	public static <T extends SmartMeterMeasurementListener> SmartMeterMeasurementListenerModule<T> create(final Class<T> clazz) {
		return new SmartMeterMeasurementListenerModule<>(clazz);
	}

	private final Class<T> clazz;

	private SmartMeterMeasurementListenerModule(final Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public void configure() {
		Multibinder<SmartMeterMeasurementListener> newSetBinder = Multibinder.newSetBinder(binder(), SmartMeterMeasurementListener.class);
		newSetBinder.addBinding().to(this.clazz);
	}

}
