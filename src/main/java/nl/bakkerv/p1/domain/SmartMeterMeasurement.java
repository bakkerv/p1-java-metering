package nl.bakkerv.p1.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class SmartMeterMeasurement implements Serializable {

	private static final long serialVersionUID = 1L;

	private Instant timestamp;
	private BigDecimal electricityConsumptionLowRateKwh;
	private BigDecimal electricityConsumptionNormalRateKwh;
	private BigDecimal electricityProductionLowRateKwh;
	private BigDecimal electricityProductionNormalRateKwh;
	private BigDecimal currentPowerConsumptionW;
	private BigDecimal currentPowerProductionW;
	private BigDecimal gasConsumptionM3;

	public Instant getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(final Instant timestamp) {
		this.timestamp = timestamp;
	}

	public BigDecimal getElectricityConsumptionLowRateKwh() {
		return this.electricityConsumptionLowRateKwh;
	}

	public void setElectricityConsumptionLowRateKwh(final BigDecimal electricityConsumptionLowRateKwh) {
		this.electricityConsumptionLowRateKwh = electricityConsumptionLowRateKwh;
	}

	public BigDecimal getElectricityConsumptionNormalRateKwh() {
		return this.electricityConsumptionNormalRateKwh;
	}

	public void setElectricityConsumptionNormalRateKwh(final BigDecimal electricityConsumptionNormalRateKwh) {
		this.electricityConsumptionNormalRateKwh = electricityConsumptionNormalRateKwh;
	}

	public BigDecimal getElectricityProductionLowRateKwh() {
		return this.electricityProductionLowRateKwh;
	}

	public void setElectricityProductionLowRateKwh(final BigDecimal electricityProductionLowRateKwh) {
		this.electricityProductionLowRateKwh = electricityProductionLowRateKwh;
	}

	public BigDecimal getElectricityProductionNormalRateKwh() {
		return this.electricityProductionNormalRateKwh;
	}

	public void setElectricityProductionNormalRateKwh(final BigDecimal electricityProductionNormalRateKwh) {
		this.electricityProductionNormalRateKwh = electricityProductionNormalRateKwh;
	}

	public BigDecimal getCurrentPowerConsumptionW() {
		return this.currentPowerConsumptionW;
	}

	public void setCurrentPowerConsumptionW(final BigDecimal currentPowerConsumptionW) {
		this.currentPowerConsumptionW = currentPowerConsumptionW;
	}

	public BigDecimal getCurrentPowerProductionW() {
		return this.currentPowerProductionW;
	}

	public void setCurrentPowerProductionW(final BigDecimal currentPowerProductionW) {
		this.currentPowerProductionW = currentPowerProductionW;
	}

	public BigDecimal getGasConsumptionM3() {
		return this.gasConsumptionM3;
	}

	public void setGasConsumptionM3(final BigDecimal gasConsumptionM3) {
		this.gasConsumptionM3 = gasConsumptionM3;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SmartMeterMeasurement that = (SmartMeterMeasurement) o;

		if (this.currentPowerConsumptionW != null
				? !this.currentPowerConsumptionW.equals(that.currentPowerConsumptionW)
				: that.currentPowerConsumptionW != null) {
			return false;
		}
		if (this.currentPowerProductionW != null ? !this.currentPowerProductionW.equals(that.currentPowerProductionW) : that.currentPowerProductionW != null) {
			return false;
		}
		if (this.electricityConsumptionLowRateKwh != null
				? !this.electricityConsumptionLowRateKwh.equals(that.electricityConsumptionLowRateKwh)
				: that.electricityConsumptionLowRateKwh != null) {
			return false;
		}
		if (this.electricityConsumptionNormalRateKwh != null
				? !this.electricityConsumptionNormalRateKwh.equals(that.electricityConsumptionNormalRateKwh)
				: that.electricityConsumptionNormalRateKwh != null) {
			return false;
		}
		if (this.electricityProductionLowRateKwh != null
				? !this.electricityProductionLowRateKwh.equals(that.electricityProductionLowRateKwh)
				: that.electricityProductionLowRateKwh != null) {
			return false;
		}
		if (this.electricityProductionNormalRateKwh != null
				? !this.electricityProductionNormalRateKwh.equals(that.electricityProductionNormalRateKwh)
				: that.electricityProductionNormalRateKwh != null) {
			return false;
		}
		if (this.gasConsumptionM3 != null ? !this.gasConsumptionM3.equals(that.gasConsumptionM3) : that.gasConsumptionM3 != null) {
			return false;
		}
		if (this.timestamp != null ? !this.timestamp.equals(that.timestamp) : that.timestamp != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = this.timestamp != null ? this.timestamp.hashCode() : 0;
		result = 31 * result + (this.electricityConsumptionLowRateKwh != null ? this.electricityConsumptionLowRateKwh.hashCode() : 0);
		result = 31 * result + (this.electricityConsumptionNormalRateKwh != null ? this.electricityConsumptionNormalRateKwh.hashCode() : 0);
		result = 31 * result + (this.electricityProductionLowRateKwh != null ? this.electricityProductionLowRateKwh.hashCode() : 0);
		result = 31 * result + (this.electricityProductionNormalRateKwh != null ? this.electricityProductionNormalRateKwh.hashCode() : 0);
		result = 31 * result + (this.currentPowerConsumptionW != null ? this.currentPowerConsumptionW.hashCode() : 0);
		result = 31 * result + (this.currentPowerProductionW != null ? this.currentPowerProductionW.hashCode() : 0);
		result = 31 * result + (this.gasConsumptionM3 != null ? this.gasConsumptionM3.hashCode() : 0);
		return result;
	}
}
