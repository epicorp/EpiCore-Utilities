package net.epicorp.utilities;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TimedList<T> {
	protected HashMap<T, BukkitTask> backing = new HashMap<>();
	protected BukkitScheduler scheduler = Bukkit.getScheduler();
	protected Plugin plugin;

	public void add(T object, long ticks, Consumer<T> onRemove) {
		this.backing.put(object, this.scheduler.runTaskLater(this.plugin, () -> {
			this.backing.remove(object);
			if (onRemove != null) {
				onRemove.accept(object);
			}
		}, ticks));
	}

	public void evictEarly(T item) {
		BukkitTask task = this.backing.remove(item);
		if(task != null)
			task.cancel();
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public Map<T, BukkitTask> getBacking() {
		return this.backing;
	}
}
