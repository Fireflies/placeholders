package ninja.smirking.framework.placeholder.bukkit;

import java.util.function.Function;

import org.bukkit.entity.Player;

public enum BukkitPlaceholder {
    PLAYER_COUNT("player_count", player -> String.valueOf(player.getServer().getOnlinePlayers().size())),
    PLAYER_UUID("unique_id", player -> String.valueOf(player.getUniqueId()), "uuid"),
    PLAYER_NAME("username", Player::getName, "player_name")
    ;

    private final Function<Player, String> mappingFunction;
    private final String placeholder;
    private final String[] aliases;

    BukkitPlaceholder(String placeholder, Function<Player, String> mappingFunction) {
        this(placeholder, mappingFunction, new String[0]);
    }

    BukkitPlaceholder(String placeholder, Function<Player, String> mappingFunction, String... aliases) {
        this.mappingFunction = mappingFunction;
        this.placeholder = placeholder;
        this.aliases = aliases;
    }

    public Function<Player, String> getMappingFunction() {
        return mappingFunction;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String[] getAliases() {
        return aliases;
    }
}
