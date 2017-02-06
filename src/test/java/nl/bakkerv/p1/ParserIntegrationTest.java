package nl.bakkerv.p1;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import nl.bakkerv.p1.configuration.SmartMeterParserModule;
import nl.bakkerv.p1.domain.meter.Direction;
import nl.bakkerv.p1.domain.meter.Kind;
import nl.bakkerv.p1.domain.meter.Meter;
import nl.bakkerv.p1.domain.meter.MeterType;
import nl.bakkerv.p1.domain.meter.Unit;
import nl.bakkerv.p1.parser.DatagramParser;
import nl.bakkerv.p1.parser.DatagramParserFactory;
import nl.bakkerv.p1.testutil.TestObjectFactory;

public class ParserIntegrationTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() {
		Injector j = Guice.createInjector(SmartMeterParserModule.create("src/test/resources/config.yaml"));
		DatagramParserFactory factory = j.getInstance(DatagramParserFactory.class);
		String testV5Datagram = TestObjectFactory.getTestV5Datagram();
		Optional<DatagramParser> parser = factory.create(testV5Datagram);
		assertThat(parser).isPresent();
		assertThat(parser.get().getMeterIdentifier()).isEqualTo("4B384547303034303436333935353037");
		assertThat(parser.get().getVersion()).isEqualTo("DSMR-50");
		assertThat(parser.get().getVendorInformation()).isEqualTo("ISk5\\2MT382-1000");
		Map<String, Meter<?>> meters = parser.get().getMapping();
		assertThat(meters).hasSize(7); // 4 kWh meters, 2 Power, 1 gas
		assertThat((Meter) meters.get("1-0:1.8.1")).isEqualToComparingOnlyGivenFields(
				Meter.builder()
						.withDirection(Direction.TO_CLIENT)
						.withKind(Kind.ELECTRICITY)
						.withMeterType(MeterType.INTEGRAL)
						.withTariff(1)
						.withUnit(Unit.KILOWATTHOUR)
						.withIdentifier("4B384547303034303436333935353037")
						.build(),
				"direction", "kind", "meterType", "tariff", "unit", "identifier");
		assertThat((Meter) meters.get("1-0:1.8.2")).isEqualToComparingOnlyGivenFields(
				Meter.builder()
						.withDirection(Direction.TO_CLIENT)
						.withKind(Kind.ELECTRICITY)
						.withMeterType(MeterType.INTEGRAL)
						.withTariff(2)
						.withIdentifier("4B384547303034303436333935353037")
						.withUnit(Unit.KILOWATTHOUR)
						.build(),
				"direction", "kind", "meterType", "tariff", "unit", "identifier");
		assertThat((Meter) meters.get("1-0:2.8.1")).isEqualToComparingOnlyGivenFields(
				Meter.builder()
						.withDirection(Direction.FROM_CLIENT)
						.withKind(Kind.ELECTRICITY)
						.withMeterType(MeterType.INTEGRAL)
						.withTariff(1)
						.withUnit(Unit.KILOWATTHOUR)
						.withIdentifier("4B384547303034303436333935353037")
						.build(),
				"direction", "kind", "meterType", "tariff", "unit", "identifier");
		assertThat((Meter) meters.get("1-0:2.8.2")).isEqualToComparingOnlyGivenFields(
				Meter.builder()
						.withDirection(Direction.FROM_CLIENT)
						.withKind(Kind.ELECTRICITY)
						.withMeterType(MeterType.INTEGRAL)
						.withTariff(2)
						.withUnit(Unit.KILOWATTHOUR)
						.withIdentifier("4B384547303034303436333935353037")
						.build(),
				"direction", "kind", "meterType", "tariff", "unit", "identifier");
		assertThat((Meter) meters.get("1-0:1.7.0")).isEqualToComparingOnlyGivenFields(
				Meter.builder()
						.withDirection(Direction.TO_CLIENT)
						.withKind(Kind.ELECTRICITY)
						.withMeterType(MeterType.INSTANTANEOUS)
						.withUnit(Unit.WATT)
						.withIdentifier("4B384547303034303436333935353037")
						.build(),
				"direction", "kind", "meterType", "unit", "identifier");
		assertThat((Meter) meters.get("1-0:2.7.0")).isEqualToComparingOnlyGivenFields(
				Meter.builder()
						.withDirection(Direction.FROM_CLIENT)
						.withKind(Kind.ELECTRICITY)
						.withMeterType(MeterType.INSTANTANEOUS)
						.withUnit(Unit.WATT)
						.withIdentifier("4B384547303034303436333935353037")
						.build(),
				"direction", "kind", "meterType", "unit", "identifier");
		assertThat((Meter) meters.get("0-1:24.2.1")).isEqualToComparingOnlyGivenFields(
				Meter.builder()
						.withDirection(Direction.TO_CLIENT)
						.withKind(Kind.GAS)
						.withMeterType(MeterType.INTEGRAL)
						.withUnit(Unit.CUBIC_METER)
						.withIdentifier("3232323241424344313233343536373839")
						.build(),
				"direction", "kind", "meterType", "unit", "identifier");

	}

}
