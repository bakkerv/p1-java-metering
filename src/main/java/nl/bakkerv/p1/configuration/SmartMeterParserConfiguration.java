package nl.bakkerv.p1.configuration;

import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SmartMeterParserConfiguration {

	@JsonProperty
	SmartMeterDeviceConfiguration smartMeter = new SmartMeterDeviceConfiguration();

	@JsonProperty
	TimeZone timeZone;

	public SmartMeterDeviceConfiguration getSmartMeter() {
		return this.smartMeter;
	}

	public TimeZone getTimeZone() {
		return this.timeZone;
	}

}
