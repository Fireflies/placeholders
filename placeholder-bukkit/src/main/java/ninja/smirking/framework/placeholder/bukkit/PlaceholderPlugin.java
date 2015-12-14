package ninja.smirking.framework.placeholder.bukkit;

import ninja.smirking.framework.placeholder.api.PlaceholderManager;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlaceholderPlugin extends JavaPlugin {
    private BukkitPlaceholderManager placeholderManager;
    private PluginListener pluginListener;

    @Override
    public void onDisable() {
        getServer().getServicesManager().unregister(BukkitPlaceholderManager.class, placeholderManager);
        getServer().getServicesManager().unregister(PlaceholderManager.class, placeholderManager);
        HandlerList.unregisterAll(pluginListener);
        placeholderManager.release();
        placeholderManager = null;
        pluginListener = null;
    }

    @Override
    public void onEnable() {
        placeholderManager = new BukkitPlaceholderManager();
        pluginListener = new PluginListener(this);
        Arrays.stream(BukkitPlaceholder.values()).forEach(placeholder -> {
            placeholderManager.registerMapping(this, placeholder.getPlaceholder(), placeholder.getMappingFunction());
            Arrays.stream(placeholder.getAliases()).forEach(alias -> placeholderManager.registerMapping(this, alias, placeholder.getMappingFunction()));
        });
        getServer().getServicesManager().register(BukkitPlaceholderManager.class, placeholderManager, this, ServicePriority.Highest);
        getServer().getServicesManager().register(PlaceholderManager.class, placeholderManager, this, ServicePriority.Highest);
        getServer().getPluginManager().registerEvents(pluginListener, this);
    }

    public BukkitPlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }

    public class PluginListener implements Listener {
        private final PlaceholderPlugin plugin;

        public PluginListener(PlaceholderPlugin plugin) {
            this.plugin = plugin;
        }

        @EventHandler
        public void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin() != plugin) {
                plugin.getPlaceholderManager().unregisterMappings(event.getPlugin());
            }
        }
    }
}
