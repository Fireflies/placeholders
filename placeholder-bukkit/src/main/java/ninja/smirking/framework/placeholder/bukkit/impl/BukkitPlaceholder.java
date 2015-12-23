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

package ninja.smirking.framework.placeholder.bukkit.impl;

import ninja.smirking.framework.placeholder.api.Placeholder;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

public enum BukkitPlaceholder implements Placeholder<Player> {
    PLAYER_COUNT("player_count", player -> String.valueOf(player.getServer().getOnlinePlayers().size())),
    PLAYER_UUID("unique_id", player -> String.valueOf(player.getUniqueId())),
    PLAYER_NAME("username", Player::getName),
    RANDOM_PLAYER("random_player", player -> {
        List<String> playerNames = player.getServer().getOnlinePlayers().stream().filter(player::canSee).map(Player::getName).collect(Collectors.toList());
        return playerNames.get(ThreadLocalRandom.current().nextInt(0, playerNames.size()));
    });

    private final Function<Player, String> mappingFunction;
    private final String text;

    BukkitPlaceholder(String text, Function<Player, String> mappingFunction) {
        this.mappingFunction = mappingFunction;
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Function<Player, String> getMappingFunction() {
        return mappingFunction;
    }
}
