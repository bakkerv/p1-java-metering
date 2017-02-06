package nl.bakkerv.p1.configuration;

import java.io.File;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import nl.bakkerv.p1.parser.DatagramParserFactory;

public class SmartMeterParserModule extends AbstractModule {

	private String configFile;

	public static SmartMeterParserModule create(final String configFile) {
		return new SmartMeterParserModule(configFile);
	}

	private SmartMeterParserModule(final String configFile) {
		this.configFile = configFile;
	}

	@Override
	protected void configure() {
		ObjectMapper om = new ObjectMapper(new YAMLFactory());
		try {
			SmartMeterParserConfiguration config = om.readValue(new File(this.configFile), SmartMeterParserConfiguration.class);
			bind(SmartMeterParserConfiguration.class).toInstance(config);
			bind(SmartMeterDeviceConfiguration.class).toInstance(config.getSmartMeter());
			bind(TimeZone.class).toInstance(config.getTimeZone());
			bind(DatagramParserFactory.class).in(Singleton.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
