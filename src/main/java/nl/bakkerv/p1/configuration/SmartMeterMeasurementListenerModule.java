package nl.bakkerv.p1.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import nl.bakkerv.p1.device.SmartMeterMeasurementListener;

public class SmartMeterMeasurementListenerModule<T extends SmartMeterMeasurementListener> extends AbstractModule {

	public static <T extends SmartMeterMeasurementListener> SmartMeterMeasurementListenerModule<T> create(final Class<T> clazz) {
		return new SmartMeterMeasurementListenerModule<>(clazz);
	}

	public static <T extends SmartMeterMeasurementListener> SmartMeterMeasurementListenerModule<T> create(final SmartMeterMeasurementListener listener) {
		return new SmartMeterMeasurementListenerModule<>(listener);
	}

	private final Class<T> clazz;
	private final SmartMeterMeasurementListener instance;

	private SmartMeterMeasurementListenerModule(final Class<T> clazz) {
		this.clazz = clazz;
		this.instance = null;
	}

	private SmartMeterMeasurementListenerModule(final SmartMeterMeasurementListener instance) {
		this.clazz = null;
		this.instance = instance;
	}

	@Override
	public void configure() {
		Multibinder<SmartMeterMeasurementListener> newSetBinder = Multibinder.newSetBinder(binder(), SmartMeterMeasurementListener.class);
		if (this.clazz != null) {
			newSetBinder.addBinding().to(this.clazz);
		}
		if (this.instance != null) {
			newSetBinder.addBinding().toInstance(this.instance);
		}
	}

}
