package net.epicorp.utilities.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BukkitBytes {
	/**
	 * serializes the inventory to the output stream
	 * the inventory holder and locations are not serialized
	 * and now the titles are gone :crab:
	 * @param inventory
	 * @param dos
	 * @throws IOException
	 */
	public static void serializeInventory(Inventory inventory, DataOutputStream dos) throws IOException {

		InventoryType type = inventory.getType(); // non null
		dos.writeUTF(type.name());
		if(type == InventoryType.CHEST)
			dos.writeInt(inventory.getSize());

		dos.writeInt(inventory.getMaxStackSize()); // non null

		ItemStack[] contents = inventory.getContents(); // non null
		dos.writeInt(contents.length);
		BukkitObjectOutputStream boos = new BukkitObjectOutputStream(dos);
		for (ItemStack content : contents)
			boos.writeObject(content);
		// done
	}

	/**
	 * deserializes an inventory from the input stream, itemholder is null
	 * @param dis
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Inventory deserializeInventory(DataInputStream dis) throws IOException, ClassNotFoundException {

		InventoryType type = InventoryType.valueOf(dis.readUTF());
		int size = -1; // only for chests
		if(type == InventoryType.CHEST)
			size = dis.readInt();

		int stackSize = dis.readInt(); // get max stack size

		ItemStack[] stacks = new ItemStack[dis.readInt()];

		BukkitObjectInputStream bois = new BukkitObjectInputStream(dis);
		for (int i = 0; i < stacks.length; i++)
			stacks[i] = (ItemStack) bois.readObject();

		Inventory inventory;
		if(size == -1)
			inventory = Bukkit.createInventory(null, type);
		else
			inventory = Bukkit.createInventory(null, size);
		inventory.setMaxStackSize(stackSize);
		inventory.setContents(stacks);

		return inventory;
	}

	/**
	 * probably a little more compact than the default
	 * serializes the location to the output stream
	 * @param location
	 * @param dos
	 */
	public static void serializeLocation(Location location, DataOutputStream dos) throws IOException {
		serializeUUID(location.getWorld().getUID(), dos);
		dos.writeDouble(location.getX());
		dos.writeDouble(location.getY());
		dos.writeDouble(location.getZ());
	}

	/**
	 * reads a location from the input stream
	 * {@link BukkitBytes#serializeLocation(Location, DataOutputStream)}
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	public static Location deserializeLocation(DataInputStream dis) throws IOException {
		return new Location(Bukkit.getWorld(deserializeUUID(dis)), dis.readDouble(), dis.readDouble(), dis.readDouble());
	}

	/**
	 * writes a uuid to the stream
	 * @param uuid
	 * @param dos
	 * @throws IOException
	 */
	public static void serializeUUID(UUID uuid, DataOutputStream dos) throws IOException {
		dos.writeLong(uuid.getMostSignificantBits());
		dos.writeLong(uuid.getLeastSignificantBits());
	}

	public static UUID deserializeUUID(DataInputStream dis) throws IOException {
		return new UUID(dis.readLong(), dis.readLong());
	}


}
