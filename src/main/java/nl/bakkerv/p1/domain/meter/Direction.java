package nl.bakkerv.p1.domain.meter;

public enum Direction {
	TO_CLIENT,
	FROM_CLIENT;

	public Direction inverse() {
		if (this == TO_CLIENT) {
			return Direction.FROM_CLIENT;
		} else {
			return TO_CLIENT;
		}
	}

}
