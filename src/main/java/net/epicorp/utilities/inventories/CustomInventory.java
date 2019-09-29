package net.epicorp.utilities.inventories;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class CustomInventory implements Inventory {

	protected ItemStack[] contents;
	protected int stackSize;
	protected String name;

	public CustomInventory(ItemStack[] array) {
		this.contents = array;
	}

	public CustomInventory(int length) {this(new ItemStack[length]);}

	@Override
	public int getSize() {
		return contents.length;
	}

	@Override
	public int getMaxStackSize() {
		return stackSize;
	}

	@Override
	public void setMaxStackSize(int size) {
		stackSize = size;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getItem(int index) {
		return contents[index];
	}

	@Override
	public void setItem(int index, ItemStack item) {
		contents[index] = item;
	}

	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
		HashMap<Integer, ItemStack> unfit = new HashMap<>();
		for (int i = 0; i < items.length; i++) {
			ItemStack stack = Inventories.addStack(items[i], contents);
			unfit.put(i, stack);
		}
		return null;
	}

	@Override
	public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
		HashMap<Integer, ItemStack> unfit = new HashMap<>();
		for (int i = 0; i < items.length; i++) {
			ItemStack stack = Inventories.remove(items[i], contents);
			unfit.put(i, stack);
		}
		return null;
	}

	@Override
	public ItemStack[] getContents() {
		return getStorageContents();
	}

	@Override
	public void setContents(ItemStack[] items) throws IllegalArgumentException {
		setStorageContents(items);
	}

	@Override
	public ItemStack[] getStorageContents() {
		return contents;
	}

	@Override
	public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
		contents = items;
	}

	@Override
	public boolean contains(int materialId) {
		throw new UnsupportedOperationException("deprecated");
	}

	@Override
	public boolean contains(Material material) throws IllegalArgumentException {
		for (ItemStack content : contents)
			if (content.getType() == material) return true;
		return false;
	}

	@Override
	public boolean contains(ItemStack item) {
		for (ItemStack content : contents)
			if (Inventories.isSimilar(content, item)) return true;
		return false;
	}

	@Override
	public boolean contains(int materialId, int amount) {
		throw new UnsupportedOperationException("deprecated");
	}

	@Override
	public boolean contains(Material material, int amount) throws IllegalArgumentException {
		return contains(new ItemStack(material, amount));
	}

	@Override
	public boolean contains(ItemStack item, int amount) {
		item = item.clone();
		item.setAmount(amount);
		return contains(item);
	}

	@Override
	public boolean containsAtLeast(ItemStack item, int amount) {
		item = item.clone();
		item.setAmount(amount);
		return Inventories.contains(item, contents);
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(int materialId) {
		throw new UnsupportedOperationException("deprecated");
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
		HashMap<Integer, ItemStack> stacks = new HashMap<>();
		for (int i = 0; i < contents.length; i++)
			if (contents[i] != null && material == contents[i].getType()) stacks.put(i, contents[i].clone());
		return stacks;
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
		HashMap<Integer, ItemStack> stacks = new HashMap<>();
		for (int i = 0; i < contents.length; i++)
			if (contents[i] != null && Inventories.isSimilar(contents[i], item)) stacks.put(i, contents[i].clone());
		return stacks;
	}

	@Override
	public int first(int materialId) {
		throw new UnsupportedOperationException("deprecated");
	}

	@Override
	public int first(Material material) throws IllegalArgumentException {
		for (int i = 0; i < contents.length; i++)
			if(contents[i] != null && contents[i].getType() == material)
				return i;
		return -1;
	}

	@Override
	public int first(ItemStack item) {
		for (int i = 0; i < contents.length; i++)
			if(contents[i] != null && Inventories.isSimilar(contents[i], item))
				return i;
		return -1;
	}

	@Override
	public int firstEmpty() {
		for (int i = 0; i < contents.length; i++)
			if(contents[i] == null)
				return i;
		return -1;
	}

	@Override
	public void remove(int materialId) {
		throw new UnsupportedOperationException("deprecated");
	}

	@Override
	public void remove(Material material) throws IllegalArgumentException {
		for (ItemStack content : contents)
			if(content != null && content.getType() == material)
				content.setAmount(content.getAmount()-1);
	}

	@Override
	public void remove(ItemStack item) {
		Inventories.remove(item, contents);
	}

	@Override
	public void clear(int index) {
		contents[index] = null;
	}

	@Override
	public void clear() {
		for (int i = 0; i < contents.length; i++)
			contents[i] = null;
	}

	@Override
	public List<HumanEntity> getViewers() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getTitle() {
		return name;
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
		return (ListIterator<ItemStack>) Arrays.asList(contents).iterator();
	}

	@Override
	public ListIterator<ItemStack> iterator(int index) {
		return (ListIterator<ItemStack>) Arrays.asList(contents).iterator();
	}

	@Override
	public Location getLocation() {
		throw new UnsupportedOperationException("Not supported!");
	}
}
