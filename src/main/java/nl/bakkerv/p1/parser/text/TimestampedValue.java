package nl.bakkerv.p1.parser.text;

import java.time.Instant;
import java.util.Optional;

public class TimestampedValue<T> {

	private final Optional<Instant> timestamp;
	private final T value;

	public TimestampedValue(final Optional<Instant> timestamp, final T value) {
		this.timestamp = timestamp;
		this.value = value;
	}

	public Optional<Instant> getTimestamp() {
		return this.timestamp;
	}

	public T getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.timestamp == null ? 0 : this.timestamp.hashCode());
		result = prime * result + (this.value == null ? 0 : this.value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TimestampedValue<?> other = (TimestampedValue<?>) obj;
		if (this.timestamp == null) {
			if (other.timestamp != null) {
				return false;
			}
		} else if (!this.timestamp.equals(other.timestamp)) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}

}
