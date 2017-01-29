package nl.bakkerv.p1.domain.meter;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import nl.bakkerv.p1.domain.measurement.Measurement;
import nl.bakkerv.p1.parser.text.TimestampedValue;
import nl.bakkerv.p1.parser.text.ValueParser;

public class Meter<T> {

	private String identifier;
	private String version;
	private String channel;
	private ValueParser<T> parser;
	private Direction direction;
	private Unit unit;

	private Meter() {
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.identifier, this.version, this.channel, this.direction, this.unit);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		Meter<?> other = (Meter<?>) obj;
		return Objects.equals(this.identifier, other.identifier) &&
				Objects.equals(this.version, other.version) &&
				Objects.equals(this.channel, other.channel) &&
				Objects.equals(this.direction, other.direction) &&
				Objects.equals(this.unit, other.unit);
	}

	public static <T> Builder<T> builder() {
		return new Builder<>();
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public String getVersion() {
		return this.version;
	}

	public String getChannel() {
		return this.channel;
	}

	public ValueParser<T> getParser() {
		return this.parser;
	}

	public Unit getUnit() {
		return this.unit;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public Optional<Measurement<T>> extractMeasurement(final Instant timestampDatagram, final String value) {
		Optional<TimestampedValue<T>> parsedValue = this.parser.parse(value);
		if (!parsedValue.isPresent()) {
			return Optional.empty();
		}
		TimestampedValue<T> timestampedValue = parsedValue.get();
		return Optional.of(new Measurement<>(timestampedValue.getTimestamp().orElse(timestampDatagram), this, timestampedValue.getValue()));
	}

	public static class Builder<T> {
		private Meter<T> instance;

		public Builder<T> withIdentifier(final String identifier) {
			this.instance.identifier = identifier;
			return this;
		}

		public Builder<T> withVersion(final String version) {
			this.instance.version = version;
			return this;
		}

		public Builder<T> withChannel(final String channel) {
			this.instance.channel = channel;
			return this;
		}

		public Builder<T> withParser(final ValueParser<T> parser) {
			this.instance.parser = parser;
			return this;
		}

		public Builder<T> withUnit(final Unit unit) {
			this.instance.unit = unit;
			return this;
		}

		public Builder<T> withDirection(final Direction direction) {
			this.instance.direction = direction;
			return this;
		}

		public Meter<T> build() {
			Meter<T> returnValue = this.instance;
			this.instance = null;
			return returnValue;
		}

	}

}
