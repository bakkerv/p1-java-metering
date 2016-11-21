package nl.bakkerv.p1.parser;

import java.util.EnumSet;

enum MeterType {
	INVALID(-1),
	GAS(3),
	HEAT(5),
	COOLING(6);

	private int id;

	MeterType(final int id) {
		this.id = id;
	}

	static MeterType fromId(final int id) {
		return EnumSet.allOf(MeterType.class).stream().filter(e -> e.id == id).findFirst().orElse(INVALID);
	}

}