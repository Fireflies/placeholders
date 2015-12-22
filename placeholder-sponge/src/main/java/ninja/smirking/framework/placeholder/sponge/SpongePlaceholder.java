/*
 * Copyright (c) 2015 Connor Spencer Harries
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ninja.smirking.framework.placeholder.sponge;

import com.google.common.net.InetAddresses;

import java.util.function.Function;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

public enum SpongePlaceholder {
    PLAYER_IP("address", player -> InetAddresses.toAddrString(player.getConnection().getAddress().getAddress())),
    PLAYER_UUID("unique_id", new String[]{ "uuid" }, player -> player.getUniqueId().toString()),
    PLAYER_HEALTH("health", player -> String.valueOf(player.getHealthData().health())),
    PLAYER_PING("ping", player -> String.valueOf(player.getConnection().getPing())),
    PLAYER_NAME("username", new String[]{ "player_name" }, User::getName);

    private final Function<Player, String> mappingFunction;
    private final String placeholder;
    private final String[] aliases;

    SpongePlaceholder(String placeholder, Function<Player, String> mappingFunction) {
        this(placeholder, new String[0], mappingFunction);
    }

    SpongePlaceholder(String placeholder, String[] aliases, Function<Player, String> mappingFunction) {
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
