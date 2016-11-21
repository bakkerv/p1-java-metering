package nl.bakkerv.p1.parser;

public class MeterTypeParser implements ValueParser<MeterType> {

	@Override
	public MeterType parse(final String value) {
		try {
			return MeterType.fromId(Integer.parseInt(value));
		} catch (Exception e) {
			return MeterType.INVALID;
		}
	}

}
