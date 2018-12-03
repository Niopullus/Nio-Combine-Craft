package combinecraft;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigParser {

    private ItemStackParser itemParser;

    public ConfigParser() {
        super();
        itemParser = new ItemStackParser();
    }

    public Map<String, CombineRecipe> getRecipes(final FileConfiguration config) {
        final ConfigurationSection recipeSection;
        final Map<String, CombineRecipe> recipes;
        recipes = new HashMap<>();
        recipeSection = config.getConfigurationSection("recipes");
        for (final String key : recipeSection.getKeys(false)) {
            final ConfigurationSection innerSection;
            final CombineRecipe recipe;
            innerSection = recipeSection.getConfigurationSection(key);
            recipe = extractRecipe(innerSection);
            recipe.setName(key);
            recipes.put(key, recipe);
        }
        return recipes;
    }

    private CombineRecipe extractRecipe(final ConfigurationSection section) {
        final CombineRecipe recipe;
        final ConfigurationSection ingreidentsSection;
        final ConfigurationSection resultsSection;
        final List<ItemStack> ingredients;
        final List<ItemStack> results;
        ingreidentsSection = section.getConfigurationSection("ingredients");
        resultsSection = section.getConfigurationSection("results");
        ingredients = getItemList(ingreidentsSection);
        results = getItemList(resultsSection);
        return new CombineRecipe(ingredients, results);
    }

    private List<ItemStack> getItemList(final ConfigurationSection section) {
        final List<ItemStack> items;
        items = new ArrayList<>();
        for (final String key : section.getKeys(false)) {
            final ConfigurationSection itemSection;
            final ItemStack item;
            itemSection = section.getConfigurationSection(key);
            item = itemParser.getItemStack(itemSection);
            items.add(item);
        }
        return items;
    }

}
