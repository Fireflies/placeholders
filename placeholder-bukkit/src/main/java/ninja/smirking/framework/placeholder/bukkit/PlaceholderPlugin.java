package ninja.smirking.framework.placeholder.bukkit;

import ninja.smirking.framework.placeholder.api.PlaceholderManager;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlaceholderPlugin extends JavaPlugin {
    private BukkitPlaceholderManager placeholderManager;

    @Override
    public void onEnable() {
        placeholderManager = new BukkitPlaceholderManager();
        for (BukkitPlaceholder placeholder : BukkitPlaceholder.values()) {
            placeholderManager.registerMapping(this, placeholder.getPlaceholder(), placeholder.getMappingFunction());
            for (String alias : placeholder.getAliases()) {
                placeholderManager.registerMapping(this, alias, placeholder.getMappingFunction());
            }
        }
        getServer().getServicesManager().register(BukkitPlaceholderManager.class, placeholderManager, this, ServicePriority.Highest);
        getServer().getServicesManager().register(PlaceholderManager.class, placeholderManager, this, ServicePriority.Highest);
    }

    @Override
    public void onDisable() {
        getServer().getServicesManager().unregister(BukkitPlaceholderManager.class, placeholderManager);
        getServer().getServicesManager().unregister(PlaceholderManager.class, placeholderManager);
        placeholderManager.release();
        placeholderManager = null;
    }

    public BukkitPlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }
}
