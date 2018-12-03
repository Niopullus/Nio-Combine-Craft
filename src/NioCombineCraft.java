import combinecraft.CombineRecipe;
import combinecraft.ConfigParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class NioCombineCraft extends JavaPlugin {

    private Map<String, CombineRecipe> recipes;

    public void onEnable() {
        final ConfigParser configParser;
        configParser = new ConfigParser();
        recipes = configParser.getRecipes(getConfig());
    }

    public boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        final String commandName;
        final Player player;
        commandName = command.getName();
        player = (Player) commandSender;
        if (commandName.equals("combinecraft")) {
            final CombineRecipe recipe;
            final String recipeName;
            int amount;
            if (args.length != 2) {
                return false;
            }
            recipeName = args[0];
            try {
                amount = Integer.parseInt(args[1]);
            } catch (final Exception e) {
                return false;
            }
            recipe = recipes.get(recipeName);
            if (recipe == null) {
                commandSender.sendMessage("Recipe not found.");
                return true;
            }
            for (int i = 0; i < amount; i++) {
                final List<ItemStack> results;
                final PlayerInventory playerInventory;
                results = recipe.attemptCraft(player);
                if (results == null) {
                    if (i == 0) {
                        commandSender.sendMessage("Couldn't find necessary ingredients.");
                    } else {
                        commandSender.sendMessage("Some crafting completed, not full requested amount.");
                    }
                    return true;
                }
                playerInventory = player.getInventory();
                for (final ItemStack item : results) {
                    playerInventory.addItem(item);
                }
                player.updateInventory();
            }
            commandSender.sendMessage("Crafting successful.");
        }
        return true;
    }

}
