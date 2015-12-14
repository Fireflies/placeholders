package ninja.smirking.framework.placeholder.bukkit;

import ninja.smirking.framework.placeholder.api.PlaceholderManager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlaceholderPlugin extends JavaPlugin {
    private PlaceholderManager<Plugin, Player> placeholderManager;

    @Override
    public void onEnable() {
        placeholderManager = new PlaceholderManager<>();
        for (BukkitPlaceholder placeholder : BukkitPlaceholder.values()) {
            placeholderManager.registerMapping(this, placeholder.getPlaceholder(), placeholder.getMappingFunction());
            for (String alias : placeholder.getAliases()) {
                placeholderManager.registerMapping(this, alias, placeholder.getMappingFunction());
            }
        }
        getServer().getServicesManager().register(PlaceholderManager.class, placeholderManager, this, ServicePriority.Highest);
    }

    @Override
    public void onDisable() {
        getServer().getServicesManager().unregister(PlaceholderManager.class, placeholderManager);
        placeholderManager.release();
        placeholderManager = null;
    }
}
