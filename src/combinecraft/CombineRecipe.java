package combinecraft;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CombineRecipe {

    private String name;
    private List<ItemStack> ingredients;
    private List<ItemStack> results;

    public CombineRecipe(final List<ItemStack> ingredients, final List<ItemStack> results) {
        super();
        this.ingredients = ingredients;
        this.results = results;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<ItemStack> attemptCraft(final Player player) {
        final PlayerInventory playerInventory;
        final ItemStack[] inventoryContents;
        final List<ItemStack> craftStacks;
        final List<ItemStack> resultingItems;
        boolean itemFound;
        playerInventory = player.getInventory();
        inventoryContents = playerInventory.getContents();
        craftStacks = new ArrayList<>();
        outer:
        for (final ItemStack ingredient : ingredients) {
            itemFound = false;
            inner:
            for (int i = 0; i < inventoryContents.length; i++) {
                final ItemStack inventorySlot;
                inventorySlot = inventoryContents[i];
                if (itemstacksEqual(ingredient, inventorySlot)) {
                    if (inventorySlot.getAmount() >= ingredient.getAmount()) {
                        craftStacks.add(inventorySlot);
                        itemFound = true;
                        break inner;
                    }
                }
            }
            if (!itemFound) {
                //System.out.println("couldnt find " + ingredient.getType());
                return null;
            }
        }
        for (int i = 0; i < craftStacks.size(); i++) {
            final ItemStack recipeItem;
            final ItemStack inventoryItem;
            recipeItem = ingredients.get(i);
            inventoryItem = craftStacks.get(i);
            if (inventoryItem.getAmount() > recipeItem.getAmount()) {
                inventoryItem.setAmount(inventoryItem.getAmount() - recipeItem.getAmount());
            } else if (inventoryItem.getAmount() == recipeItem.getAmount()) {
                playerInventory.remove(inventoryItem);
                inventoryItem.setAmount(0);
            }
        }
        player.updateInventory();
        resultingItems = new ArrayList<>();
        for (final ItemStack item : results) {
            resultingItems.add(item.clone());
        }
        return resultingItems;
    }

    private boolean itemstacksEqual(final ItemStack item1, final ItemStack item2) {
        final Material material1;
        final Material material2;
        final ItemMeta itemMeta1;
        final ItemMeta itemMeta2;
        final String name1;
        final String name2;
        final List<String> lore1;
        final List<String> lore2;
        boolean condition1;
        boolean condition2;
        boolean condition3;
        if (item1 == null) {
            return item2 == null;
        }
        if (item2 == null) {
            return false;
        }
        condition2 = true;
        condition3 = true;
        material1 = item2.getType();
        material2 = item1.getType();
        condition1 = material1.equals(material2);
        itemMeta1 = item2.getItemMeta();
        itemMeta2 = item1.getItemMeta();
        if (itemMeta1 == null) {
            if (itemMeta2 != null) {
                return false;
            }
        } else {
            if (itemMeta2 == null) {
                return false;
            }
            name1 = stripReset(itemMeta1.getDisplayName());
            name2 = stripReset(itemMeta2.getDisplayName());
            lore1 = itemMeta1.getLore();
            lore2 = itemMeta2.getLore();
            condition2 = name1.equals(name2);
            condition3 = loresEqual(lore1, lore2);
            //System.out.println(item1.getType() + " - " + item2.getType() + " - " + condition1 + " - " + condition2 + " - " + condition3 + " - " + name1 + " - " + name2);
        }
        return condition1 && condition2 && condition3;
    }

    private String stripReset(final String line) {
        if (line.length() > 2) {
            if (line.substring(0, 2).equals(ChatColor.translateAlternateColorCodes('&', "&r"))) {
                return line.substring(2);
            }
            return line;
        }
        return line;
    }

    private boolean loresEqual(final List<String> lore1, final List<String> lore2) {
        if (lore1 == null) {
            return lore2 == null;
        }
        if (lore2 == null) {
            return false;
        }
        if (lore1.size() != lore2.size()) {
            return false;
        }
        for (int i = 0; i < lore1.size(); i++) {
            final String loreItem;
            loreItem = stripReset(lore1.get(i));
            if (!loreItem.equals(stripReset(lore2.get(i)))) {
                return false;
            }
        }
        return true;
    }

}
