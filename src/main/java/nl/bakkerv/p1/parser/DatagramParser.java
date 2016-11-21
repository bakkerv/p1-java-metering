package nl.bakkerv.p1.parser;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.bakkerv.p1.domain.SmartMeterMeasurement;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DatagramParser {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, PropertyAndPattern<?>> mapping;

	public DatagramParser() {
		this.mapping = new HashMap<>();
		this.mapping.put("1.8.1", new PropertyAndPattern<>(new KwhValueParser(), "electricityConsumptionLowRateKwh"));
		this.mapping.put("1.8.2", new PropertyAndPattern<>(new KwhValueParser(), "electricityConsumptionNormalRateKwh"));
		this.mapping.put("2.8.1", new PropertyAndPattern<>(new KwhValueParser(), "electricityProductionLowRateKwh"));
		this.mapping.put("2.8.2", new PropertyAndPattern<>(new KwhValueParser(), "electricityProductionNormalRateKwh"));
		this.mapping.put("1.7.0", new PropertyAndPattern<>(new WattValueParser(), "currentPowerConsumptionW"));
		this.mapping.put("2.7.0", new PropertyAndPattern<>(new WattValueParser(), "currentPowerProductionW"));
		this.mapping.put("24.3.0", new PropertyAndPattern<>(new CubicMetreValueParser(), "gasConsumptionM3"));
		this.mapping.put("24.1.0", new PropertyAndPattern<>(new MeterTypeParser(), "meterType"));
	}

	public SmartMeterMeasurement parse(final String datagram) {

		SmartMeterMeasurement result = new SmartMeterMeasurement();

		String[] datagramLines = DatagramCleaner.asArray(datagram);

		for (String line : datagramLines) {

			for (Map.Entry<String, PropertyAndPattern<?>> entry : this.mapping.entrySet()) {
				if (line.startsWith(entry.getKey())) {
					entry.getValue().extract(line, result);
					break;
				}
			}
		}

		return result;
	}

	private class PropertyAndPattern<T> {

		private ValueParser<T> valueParser;
		private String fieldName;

		public PropertyAndPattern(final ValueParser<T> valueParser, final String fieldName) {
			this.valueParser = valueParser;
			this.fieldName = fieldName;
		}

		public void extract(final String line, final SmartMeterMeasurement measurement) {
			String[] split = line.split(":", 2);
			String busID = split[0];
			// FIXME VB: look ad busID before parsing to support generic external meters
			T value = this.valueParser.parse(split[1]);
			try {
				PropertyUtils.setProperty(measurement, this.fieldName, value);
			} catch (IllegalAccessException e) {
				DatagramParser.this.logger.error(e.toString(), e);
			} catch (InvocationTargetException e) {
				DatagramParser.this.logger.error(e.toString(), e);
			} catch (NoSuchMethodException e) {
				DatagramParser.this.logger.error(e.toString(), e);
			}
		}
	}
}
