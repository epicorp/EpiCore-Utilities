package net.epicorp.utilities.inventories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import java.util.Map;

/**
 * an Immutable ItemStack (I just delegated all the methods and removed the mutable ones)
 */
public class ImmutableStack {
	private final ItemStack stack;

	public ImmutableStack(ItemStack stack) {
		this.stack = stack;
	}

	public Material getType() {return this.stack.getType();}

	public int getAmount() {return this.stack.getAmount();}
	public MaterialData getData() {return this.stack.getData();}
	public short getDurability() {return this.stack.getDurability();}
	public int getMaxStackSize() {return this.stack.getMaxStackSize();}
	public boolean isSimilar(ItemStack stack) {return this.stack.isSimilar(stack);}
	public boolean containsEnchantment(Enchantment ench) {return this.stack.containsEnchantment(ench);}
	public int getEnchantmentLevel(Enchantment ench) {return this.stack.getEnchantmentLevel(ench);}
	public Map<Enchantment, Integer> getEnchantments() {return this.stack.getEnchantments();}
	public Map<String, Object> serialize() {return this.stack.serialize();}
	public ItemMeta getItemMeta() {return this.stack.getItemMeta();}
	public boolean hasItemMeta() {return this.stack.hasItemMeta();}
	public ItemStack stack() {
		return this.stack.clone();
	}

	public boolean isNull() {
		return this.stack == null;
	}
}
