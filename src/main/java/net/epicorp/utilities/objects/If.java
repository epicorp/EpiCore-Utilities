package net.epicorp.utilities.objects;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class If {
	public static void nonNull(Object object, ERunnable runnable) {
		if(object != null)
			runnable.run();
	}

	public static void ifNull(Object object, ERunnable runnable) {
		if(object == null)
			runnable.run();
	}

	public static <T> void nonNull(T object, Consumer<T> consumer) {
		if(object != null)
			consumer.accept(object);
	}

	public static void ifElse(boolean pred, ERunnable _if, ERunnable _else) {
		if(pred)
			_if.run();
		else
			_else.run();
	}

	public static <T> T nonNull(T object, Supplier<T> supplier) {
		if(object == null)
			return supplier.get();
		return object;
	}

	public static <A, B> B computeIfNull(A object, Function<A, B> convert) {
		return object == null ? null : convert.apply(object);
	}

	public interface ERunnable extends Runnable {
		void impl() throws Throwable;

		@Override
		default void run() {
			try {
				this.impl();
			} catch (Throwable throwable) {
				throw new RuntimeException(throwable);
			}
		}
	}
}
