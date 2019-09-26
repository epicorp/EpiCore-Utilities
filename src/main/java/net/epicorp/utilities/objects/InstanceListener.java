package net.epicorp.utilities.objects;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Predicate;

public class InstanceListener {

	/**
	 * registers listeners for all the object's annotated methods
	 * @param plugin the plugin
	 * @param anno the specialised event handler annotation
	 * @param converter converts the listened events into an object
	 * @param acceptable tests if the object is acceptable
	 * @param ignoreCancelled get if the listener should ignore cancels
	 * @param getPriority get the listener priority
	 */
	public static <T, B extends Annotation> void register(Listener object, Plugin plugin, Class<B> anno, Function<Event, T> converter, Predicate<T> acceptable, Predicate<B> ignoreCancelled, Function<B, EventPriority> getPriority) {
		Class<?> thisClass = object.getClass(); // get the current class
		for (Method method : thisClass.getMethods()) { // get all public methods including inherited ones
			B eventHandler = method.getDeclaredAnnotation(anno); // find custom event handler
			Class<?>[] paramTypes = method.getParameterTypes();
			if (eventHandler != null && paramTypes.length > 0) { // filter unannotated methods
				Class<?> eventClass = paramTypes[0];
				boolean morearg = paramTypes.length == 2;
				if (Event.class.isAssignableFrom(eventClass)) { // make sure the method is listening to an event
					EventExecutor executor = (t, e) -> {
						try {
							T converted = converter.apply(e);
							if (acceptable.test(converted)) { // must make sure it's a valid method
								if(!morearg)
									method.invoke(t, e); // invoke the listener
								else
									method.invoke(t, e, converted); // conversion listener
							}
						} catch (ReflectiveOperationException e1) {
							throw new RuntimeException(e1);
						}
					};
					plugin.getServer().getPluginManager().registerEvent((Class<? extends Event>) eventClass, object, getPriority.apply(eventHandler), executor, plugin, ignoreCancelled.test(eventHandler));
					// register the listener after the event is called
				} else
					throw new IllegalArgumentException(method + " is not listening to an event but is annotated with " + anno);
			}

		}
	}
}
