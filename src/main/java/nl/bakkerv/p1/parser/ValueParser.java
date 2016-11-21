package nl.bakkerv.p1.parser;

public interface ValueParser<T> {
	T parse(String value);
}
