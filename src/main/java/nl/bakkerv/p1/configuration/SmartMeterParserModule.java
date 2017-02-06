package nl.bakkerv.p1.configuration;

import java.io.File;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import nl.bakkerv.p1.device.P1DatagramListener;
import nl.bakkerv.p1.device.P1DatagramListenerImpl;
import nl.bakkerv.p1.device.SmartMeterDevice;
import nl.bakkerv.p1.device.SmartMeterMeasurementListener;
import nl.bakkerv.p1.parser.DatagramParserFactory;

public class SmartMeterParserModule extends AbstractModule {

	public static SmartMeterParserModule create(final String configFile) {
		try {
			ObjectMapper om = new ObjectMapper(new YAMLFactory());
			SmartMeterParserConfiguration config = om.readValue(new File(configFile), SmartMeterParserConfiguration.class);
			return new SmartMeterParserModule(config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static SmartMeterParserModule create(final SmartMeterParserConfiguration config) {
		return new SmartMeterParserModule(config);
	}

	private SmartMeterParserConfiguration config;

	private SmartMeterParserModule(final SmartMeterParserConfiguration config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		bind(SmartMeterParserConfiguration.class).toInstance(this.config);
		bind(SmartMeterDeviceConfiguration.class).toInstance(this.config.getSmartMeter());
		bind(TimeZone.class).toInstance(this.config.getTimeZone());
		bind(DatagramParserFactory.class).in(Singleton.class);
		bind(SmartMeterDevice.class).in(Singleton.class);
		bind(P1DatagramListener.class).to(P1DatagramListenerImpl.class).in(Singleton.class);
		Multibinder.newSetBinder(binder(), SmartMeterMeasurementListener.class);
	}

}
