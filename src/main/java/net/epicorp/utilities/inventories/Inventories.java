package net.epicorp.utilities.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class Inventories {
	/**
	 * takes 1 item from the export inventory and adds it to the input inventory
	 * this emulates a hopper
	 *
	 * @param export
	 * @param input
	 */
	public static void mergeOne(Inventory export, Inventory input) {
		ItemStack[] contents = export.getContents();
		ItemStack[] inputs = input.getContents();
		for (ItemStack content : contents) {
			if (content != null) {
				ItemStack stack = content.clone();
				stack.setAmount(1);
				if (canAddStack(stack, inputs)) {
					input.addItem(stack);
					export.removeItem(stack);
					return;
				}
			}
		}
	}

	/**
	 * gets the amount of items that would be left over after a merge with the target inventory
	 *
	 * @param stack the stack
	 * @param target the target inventory
	 * @return the amount of that item that would have been excluded
	 */
	public static int getLeftover(ItemStack stack, ItemStack[] target) {
		int items = stack.getAmount();
		for (int i = 0; i < target.length; i++) {
			ItemStack current = target[i];
			if (current == null) // if there is a totally empty slot in the inventory, then there is definitely space for an item
				return 0;
			else if (current.isSimilar(stack))
				items -= getLeft(current); // subtract the left over space for stacking in the item
			if (items <= 0) // if there is enough space, we good
				return 0;
		}
		return items;
	}

	public static ItemStack addStack(ItemStack stack, ItemStack[] target) {
		int items = stack.getAmount();
		for (int i = 0; i < target.length; i++) {
			ItemStack current = target[i];
			if (current == null) {// if there is a totally empty slot in the inventory, then there is definitely space for an item
				target[i] = stack.clone();
				return null;
			} else if (current.isSimilar(stack)) {
				items -= getLeft(current); // subtract the left over space for stacking in the item
				current.setAmount(current.getMaxStackSize());
			}
			if (items <= 0) // if there is enough space, we good
				return null;
		}
		ItemStack left = stack.clone();
		left.setAmount(items);
		return left;
	}
	/**
	 * checks if the inventory has space for the item
	 *
	 * @param stack the item
	 * @param inventory the inventory to insert
	 * @return
	 */
	public static boolean canAddStack(ItemStack stack, Inventory inventory) {
		return canAddStack(stack, inventory.getStorageContents());
	}

	/**
	 * Check if the inventory has enough space for the itemstack
	 *
	 * @param stack the stack you wish to check for
	 * @param target the "inventory" you want to check for space
	 * @return
	 */
	public static boolean canAddStack(ItemStack stack, ItemStack[] target) {
		return getLeftover(stack, target) == 0;
	}

	public static boolean canAddStacks(ItemStack[] stacks, ItemStack[] output) {
		int size = stacks.length + output.length;
		Inventory inventory = Bukkit.createInventory(null, (9 - (size % 9)) + size);
		inventory.addItem(removeNulls(stacks));
		inventory.addItem(removeNulls(output));
		return inventory.firstEmpty() < output.length;
	}

	/**
	 * returns the remaining capacity for stacking items
	 *
	 * @param stacks
	 * @return
	 */
	public static int getLeft(ItemStack stacks) {
		return stacks.getMaxStackSize() - stacks.getAmount();
	}

	/**
	 * condense the stacks of items unsafely (ignoring the max stack size)
	 *
	 * @param stacks
	 * @return
	 */
	public static ItemStack[] condenseUnsafe(ItemStack[] stacks) {
		List<ItemStack> stackList = new ArrayList<>();
		for (ItemStack stack : stacks) {
			boolean newstack = true;
			for (ItemStack itemStack : stackList) {
				if (itemStack.isSimilar(stack)) {
					newstack = false;
					itemStack.setAmount(itemStack.getAmount() + stack.getAmount());
				}
			}
			if (stack != null && newstack) stackList.add(stack.clone());
		}

		return stackList.toArray(new ItemStack[0]);
	}

	/**
	 * uncondenses illegal stacks of items (items that exceed their maximum stack size)
	 *
	 * @param stack
	 * @return
	 */
	public static ItemStack[] uncondense(ItemStack[] stack) {
		List<ItemStack> stacks = new ArrayList<>();
		for (ItemStack itemStack : stack) {
			int max = itemStack.getMaxStackSize();
			int cur = itemStack.getAmount();
			if (max > cur) // if the stack is already safe
				stacks.add(itemStack.clone()); // add it
			else { // if the item is an unsafe (larger than max size)
				if (max == 0) continue;
				int over = cur / max;
				for (int x = 0; x < over; x++) {
					ItemStack newStack = itemStack.clone();
					newStack.setAmount(newStack.getMaxStackSize());
					stacks.add(newStack);
				}
				if (over * max != cur) {// if there is a remainder
					ItemStack newStack = itemStack.clone();
					newStack.setAmount(cur - over * max);
					stacks.add(newStack);
				}
			}
		}
		return stacks.toArray(new ItemStack[0]);
	}

	/**
	 * will replace all air stacks with null
	 *
	 * @param stacks
	 */
	public static void clean(ItemStack[] stacks) {
		for (int i = 0; i < stacks.length; i++) {
			ItemStack stack = stacks[i];
			if (stack != null && stack.getType() == Material.AIR) stacks[i] = null;
		}
	}

	public static ItemStack[] sort(ItemStack[] array) {
		Arrays.sort(array, Comparator.comparingInt(ItemStack::hashCode));
		return array;
	}

	public static boolean contains(ItemStack[] stacks, ItemStack[] inventory) {
		stacks = condenseUnsafe(stacks);
		inventory = condenseUnsafe(inventory);
		for (ItemStack stack : stacks)
			if (!containsUnsafe(stack, inventory)) return false;
		return true;
	}

	/**
	 * assumes the stacks contents has been condensed Unsafely
	 *
	 * @param stack
	 * @param stacks
	 * @return
	 */
	public static boolean containsUnsafe(ItemStack stack, ItemStack[] stacks) {
		for (ItemStack itemStack : stacks)
			if (isSimilar(stack, itemStack))
				return getAmount(stack) <= getAmount(itemStack);
		return false;
	}

	public static ItemStack[] removeNulls(ItemStack[] stacks) {
		List<ItemStack> newStacks = new ArrayList<>();
		for (ItemStack stack : stacks) if (stack != null) newStacks.add(stack.clone());
		return newStacks.toArray(new ItemStack[0]);
	}

	public static ItemStack stack(Material material, int amount) {
		return new ItemStack(material, amount);
	}

	public static ItemStack stack(Material material) {
		return stack(material, 1);
	}

	public static boolean isSimilar(ItemStack a, ItemStack b) {
		if((a == null) != (b==null))
			return false;
		return a == null || a.isSimilar(b);
	}

	public static int getAmount(ItemStack a) {
		return a == null ? 0 : a.getAmount();
	}

	public static ItemStack[] clone(ItemStack[] stacks) {
		ItemStack[] inv = new ItemStack[stacks.length];
		for (int i = 0; i < stacks.length; i++) {
			if(stacks[i] != null)
				inv[i] = stacks[i].clone();
		}
		return inv;
	}

	/**
	 * removes a certain amount from every item stack in the contents
	 * @param input
	 * @param sub
	 */
	public static void removeNFrom(ItemStack[] input, int sub) {
		for (ItemStack stack : input) {
			if(stack != null)
				stack.setAmount(stack.getAmount()-sub);
		}
	}

	/**
	 * remove the item stack from the array
	 * @param stack the stack to remove
	 * @param array the array to deduct from
	 */
	public static ItemStack remove(ItemStack stack, ItemStack[] array) {
		int count = stack.getAmount(); // get current amount
		for (int i = 0; i < array.length; i++) {
			ItemStack current = array[i];
			if(Inventories.isSimilar(current, stack)) { // if they are the same items
				int amount = current.getAmount(); // amount of items in that slot
				amount -= count;
				if(amount <= 0) { // if the stack is too small
					array[i] = null; // take out the whole stack
					count = Math.abs(amount); // and set the counter to the remainder
				} else {
					current.setAmount(amount); // if it's large enough
					return null; // all of the stack was added
				}
			}
		}
		ItemStack newStack = stack.clone();
		newStack.setAmount(count);
		return newStack;
	}

	public static boolean contains(ItemStack stack, ItemStack[] stacks) {
		return remove(stack, stacks.clone()) == null;
	}

	public static List<ItemStack> take(Inventory inventory, int amount) {
		ItemStack[] stacks = inventory.getContents();
		List<ItemStack> stackList = new LinkedList<>();

		for (int i = 0; i < stacks.length; i++) {
			ItemStack stack = stacks[i];
			if(stack != null) {
				int size = stack.getAmount();
				if(size > amount) {
					stack.setAmount(size - amount);

					if(amount == 0)
						break;

					ItemStack clone = stack.clone();
					clone.setAmount(amount);
					stackList.add(clone);
					break;
				} else {
					stackList.add(stack);
					stacks[i] = null;
					amount -= size;
				}
			}
		}

		return stackList;
	}



}
