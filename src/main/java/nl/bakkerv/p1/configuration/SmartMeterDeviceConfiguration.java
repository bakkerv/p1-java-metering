package nl.bakkerv.p1.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class SmartMeterDeviceConfiguration {

	@JsonProperty
	private String portName = "/dev/ttyUSB0";

	@JsonProperty
	private int portTimeOut = 2000;

	@JsonProperty
	SmartMeterPortSettings smartMeterPortSettings = SmartMeterPortSettings.V4;

	public int getPortTimeOut() {
		return this.portTimeOut;
	}

	public String getPortName() {
		return this.portName;
	}

	public SmartMeterPortSettings getSmartMeterPortSettings() {
		return this.smartMeterPortSettings;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("portName", this.portName)
				.add("timeout", this.portTimeOut)
				.add("smartMeterPortSettings", this.smartMeterPortSettings)
				.toString();
	}

}
