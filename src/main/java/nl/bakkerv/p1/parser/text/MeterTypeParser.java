package nl.bakkerv.p1.parser.text;

import java.util.Optional;

public class MeterTypeParser implements ValueParser<MeterType> {

	@Override
	public Optional<TimestampedValue<MeterType>> parse(final String value) {
		try {
			MeterType fromId = MeterType.fromId(Integer.parseInt(value));
			return Optional.of(new TimestampedValue<>(Optional.empty(), fromId));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
