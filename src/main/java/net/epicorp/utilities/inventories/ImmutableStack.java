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

	public Material getType() {return stack.getType();}
	@Deprecated
	public int getTypeId() {return stack.getTypeId();}
	public int getAmount() {return stack.getAmount();}
	public MaterialData getData() {return stack.getData();}
	public short getDurability() {return stack.getDurability();}
	public int getMaxStackSize() {return stack.getMaxStackSize();}
	public boolean isSimilar(ItemStack stack) {return this.stack.isSimilar(stack);}
	public boolean containsEnchantment(Enchantment ench) {return stack.containsEnchantment(ench);}
	public int getEnchantmentLevel(Enchantment ench) {return stack.getEnchantmentLevel(ench);}
	public Map<Enchantment, Integer> getEnchantments() {return stack.getEnchantments();}
	public Map<String, Object> serialize() {return stack.serialize();}
	public ItemMeta getItemMeta() {return stack.getItemMeta();}
	public boolean hasItemMeta() {return stack.hasItemMeta();}
	public ItemStack stack() {
		return stack.clone();
	}

	public boolean isNull() {
		return stack == null;
	}
}
