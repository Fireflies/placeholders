package ninja.smirking.framework.placeholder.bukkit;

import ninja.smirking.framework.placeholder.api.PlaceholderManager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/*
 * We don't need this implementation, but we provide it to save API consumers from having to
 * cast the raw PlaceholderManager themselves.
 */
public class BukkitPlaceholderManager extends PlaceholderManager<Plugin, Player> {

}
