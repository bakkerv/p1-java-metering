package nl.bakkerv.p1.domain.measurement;

import java.time.Instant;
import java.util.Objects;
import java.util.function.BiFunction;

import com.google.common.base.MoreObjects;

import nl.bakkerv.p1.domain.meter.Meter;

public class Measurement<T> {

	private final Instant timestamp;
	private final Meter<T> meter;
	private final T value;

	public Measurement(final Instant timestamp, final Meter<T> meter, final T value) {
		this.timestamp = timestamp;
		this.meter = meter;
		this.value = value;
	}

	public Instant getTimestamp() {
		return this.timestamp;
	}

	public Meter<?> getMeter() {
		return this.meter;
	}

	public T getValue() {
		return this.value;
	}

	public Measurement<T> combineMeasurements(final Measurement<T> other, final BiFunction<T, T, T> valueCombiner) {
		return new Measurement<>(this.timestamp, this.meter, valueCombiner.apply(this.value, other.value));
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("meter", this.meter).add("timestamp", this.timestamp).add("value", this.value).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.meter, this.timestamp, this.value);
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other.getClass() != this.getClass()) {
			return false;
		}
		Measurement<?> that = (Measurement<?>) other;
		return Objects.equals(this.timestamp, that.timestamp) &&
				Objects.equals(this.meter, that.meter) &&
				Objects.equals(this.value, that.value);

	}

}
