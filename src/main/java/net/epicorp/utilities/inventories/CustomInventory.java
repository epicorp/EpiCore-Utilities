package net.epicorp.utilities.inventories;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class CustomInventory implements Inventory {

	protected ItemStack[] contents; // non null
	protected int stackSize; // TODO implement
	protected String name = "Chest"; // non null

	public CustomInventory(ItemStack[] array) {
		this.contents = array;
		this.name = "Chest";
	}

	public CustomInventory(int length) {this(new ItemStack[length]);}

	public CustomInventory() {}

	@Override
	public int getSize() {
		return this.contents.length;
	}

	@Override
	public int getMaxStackSize() {
		return this.stackSize;
	}

	@Override
	public void setMaxStackSize(int size) {
		this.stackSize = size;
	}

	@Override
	public ItemStack getItem(int index) {
		return this.contents[index];
	}

	@Override
	public void setItem(int index, ItemStack item) {
		this.contents[index] = item;
	}

	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
		HashMap<Integer, ItemStack> unfit = new HashMap<>();
		for (int i = 0; i < items.length; i++) {
			ItemStack stack = Inventories.addStack(items[i], this.contents);
			unfit.put(i, stack);
		}
		return unfit;
	}

	@Override
	public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
		HashMap<Integer, ItemStack> unfit = new HashMap<>();
		for (int i = 0; i < items.length; i++) {
			ItemStack stack = Inventories.remove(items[i], this.contents);
			unfit.put(i, stack);
		}
		return unfit;
	}

	@Override
	public ItemStack[] getContents() {
		return this.getStorageContents();
	}

	@Override
	public void setContents(ItemStack[] items) throws IllegalArgumentException {
		this.setStorageContents(items);
	}

	@Override
	public ItemStack[] getStorageContents() {
		return this.contents;
	}

	@Override
	public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
		this.contents = items;
	}

	@Override
	public boolean contains(Material material) throws IllegalArgumentException {
		for (ItemStack content : this.contents)
			if (content.getType() == material) return true;
		return false;
	}

	@Override
	public boolean contains(ItemStack item) {
		for (ItemStack content : this.contents)
			if (Inventories.isSimilar(content, item)) return true;
		return false;
	}

	@Override
	public boolean contains(Material material, int amount) throws IllegalArgumentException {
		return this.contains(new ItemStack(material, amount));
	}

	@Override
	public boolean contains(ItemStack item, int amount) {
		item = item.clone();
		item.setAmount(amount);
		return this.contains(item);
	}

	@Override
	public boolean containsAtLeast(ItemStack item, int amount) {
		item = item.clone();
		item.setAmount(amount);
		return Inventories.contains(item, this.contents);
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
		HashMap<Integer, ItemStack> stacks = new HashMap<>();
		for (int i = 0; i < this.contents.length; i++)
			if (this.contents[i] != null && material == this.contents[i].getType()) stacks.put(i, this.contents[i].clone());
		return stacks;
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
		HashMap<Integer, ItemStack> stacks = new HashMap<>();
		for (int i = 0; i < this.contents.length; i++)
			if (this.contents[i] != null && Inventories.isSimilar(this.contents[i], item)) stacks.put(i, this.contents[i].clone());
		return stacks;
	}

	@Override
	public int first(Material material) throws IllegalArgumentException {
		for (int i = 0; i < this.contents.length; i++)
			if(this.contents[i] != null && this.contents[i].getType() == material)
				return i;
		return -1;
	}

	@Override
	public int first(ItemStack item) {
		for (int i = 0; i < this.contents.length; i++)
			if(this.contents[i] != null && Inventories.isSimilar(this.contents[i], item))
				return i;
		return -1;
	}

	@Override
	public int firstEmpty() {
		for (int i = 0; i < this.contents.length; i++)
			if(this.contents[i] == null)
				return i;
		return -1;
	}


	@Override
	public void remove(Material material) throws IllegalArgumentException {
		for (ItemStack content : this.contents)
			if(content != null && content.getType() == material)
				content.setAmount(content.getAmount()-1);
	}

	@Override
	public void remove(ItemStack item) {
		Inventories.remove(item, this.contents);
	}

	@Override
	public void clear(int index) {
		this.contents[index] = null;
	}

	@Override
	public void clear() {
		Arrays.fill(this.contents, null);
	}

	@Override
	public List<HumanEntity> getViewers() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public InventoryType getType() {
		return InventoryType.CHEST;
	}

	@Override
	public InventoryHolder getHolder() {
		return () -> CustomInventory.this;
	}

	@Override
	public ListIterator<ItemStack> iterator() {
		return (ListIterator<ItemStack>) Arrays.asList(this.contents).iterator();
	}

	@Override
	public ListIterator<ItemStack> iterator(int index) {
		return (ListIterator<ItemStack>) Arrays.asList(this.contents).iterator();
	}

	@Override
	public Location getLocation() {
		throw new UnsupportedOperationException("Not supported!");
	}
}
