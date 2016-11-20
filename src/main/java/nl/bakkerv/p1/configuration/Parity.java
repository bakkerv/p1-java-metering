package nl.bakkerv.p1.configuration;

import gnu.io.SerialPort;

public enum Parity {
	EVEN(SerialPort.PARITY_EVEN),
	MARK(SerialPort.PARITY_MARK),
	NONE(SerialPort.PARITY_NONE),
	ODD(SerialPort.PARITY_ODD),
	SPACE(SerialPort.PARITY_SPACE);

	private int parity;

	Parity(final int parity) {
		this.parity = parity;
	}

	public int getParity() {
		return this.parity;
	}
}