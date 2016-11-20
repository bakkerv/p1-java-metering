package nl.bakkerv.p1.configuration;

import gnu.io.SerialPort;

public enum StopBits {
	ONE(SerialPort.STOPBITS_1),
	ONE_HALF(SerialPort.STOPBITS_1_5),
	TWO(SerialPort.STOPBITS_2);

	private int stopBits;

	StopBits(final int bits) {
		this.stopBits = bits;
	}

	public int getStopBits() {
		return this.stopBits;
	}
}