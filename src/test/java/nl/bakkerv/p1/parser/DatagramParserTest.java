package nl.bakkerv.p1.parser;

import nl.bakkerv.p1.domain.SmartMeterMeasurement;
import nl.bakkerv.p1.parser.DatagramParser;
import nl.bakkerv.p1.testutil.TestObjectFactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;

public class DatagramParserTest {

	private DatagramParser parser;

	@BeforeClass
	public void init() {
		this.parser = new DatagramParser();
	}

	@Test
	public void testParse() throws Exception {

		SmartMeterMeasurement reference = new SmartMeterMeasurement();
		// reference.setTimestamp();
		reference.setCurrentPowerProductionW(new BigDecimal(550));
		reference.setCurrentPowerConsumptionW(BigDecimal.ZERO);
		reference.setElectricityConsumptionNormalRateKwh(new BigDecimal("26.006"));
		reference.setElectricityConsumptionLowRateKwh(new BigDecimal("38.113"));
		reference.setElectricityProductionNormalRateKwh(new BigDecimal("6.696"));
		reference.setElectricityProductionLowRateKwh(new BigDecimal("1.104"));
		reference.setGasConsumptionM3(new BigDecimal("47.057"));

		SmartMeterMeasurement result = this.parser.parse(TestObjectFactory.getTestDatagram());

		assertThat(result).isEqualTo(reference);
	}
}
