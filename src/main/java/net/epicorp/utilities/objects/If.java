package net.epicorp.utilities.objects;

import java.util.function.Consumer;

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

	public interface ERunnable extends Runnable {
		void impl() throws Throwable;

		default void run() {
			try {
				impl();
			} catch (Throwable throwable) {
				throw new RuntimeException(throwable);
			}
		}
	}
}
