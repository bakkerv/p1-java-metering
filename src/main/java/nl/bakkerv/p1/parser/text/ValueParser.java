package nl.bakkerv.p1.parser.text;

import java.util.Optional;

public interface ValueParser<T> {
	Optional<TimestampedValue<T>> parse(String value);
}
