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

package ninja.smirking.framework.placeholder.bukkit;

import ninja.smirking.framework.placeholder.bukkit.util.VaultUtil;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public enum BukkitPlaceholder {
    PLAYER_COUNT("player_count", player -> String.valueOf(player.getServer().getOnlinePlayers().size())),
    PLAYER_UUID("unique_id", player -> String.valueOf(player.getUniqueId())),
    PLAYER_NAME("username", Player::getName),
    RANDOM_PLAYER("random_player", player -> {
        List<String> playerNames = player.getServer().getOnlinePlayers().stream().filter(player::canSee).map(Player::getName).collect(Collectors.toList());
        return playerNames.get(ThreadLocalRandom.current().nextInt(0, playerNames.size()));
    }),
    VAULT_BALANCE("balance", player -> {
        Optional<Economy> possibleEconomy = VaultUtil.getEconomy(player.getServer());
        if (VaultUtil.isEconomyEnabled(possibleEconomy)) {
            return VaultUtil.formatBalance(possibleEconomy.get().getBalance(player));
        }
        return VaultUtil.formatBalance(0.00D);
    }),
    VAULT_BANK_BALANCE("bank_balance", player -> {
        Optional<Economy> possibleEconomy = VaultUtil.getEconomy(player.getServer());
        if (VaultUtil.isEconomyEnabled(possibleEconomy) && possibleEconomy.get().hasBankSupport()) {
            Economy economy = possibleEconomy.get();
            double balance = 0.00D;
            for (String bankName : economy.getBanks()) {
                EconomyResponse response = economy.isBankMember(bankName, player);
                if (response.type == EconomyResponse.ResponseType.SUCCESS) {
                    response = economy.bankBalance(bankName);
                    if (response.type == EconomyResponse.ResponseType.SUCCESS) {
                        balance += response.balance;
                    }
                }
            }
            return VaultUtil.formatBalance(balance);
        }
        return VaultUtil.formatBalance(0.00D);
    }),
    VAULT_ECONOMY_NAME("economy", player -> {
        Optional<Economy> possibleEconomy = VaultUtil.getEconomy(player.getServer());
        if (VaultUtil.isEconomyEnabled(possibleEconomy)) {
            return String.valueOf(possibleEconomy.get().getName());
        }
        return "";
    });

    private final Function<Player, String> mappingFunction;
    private final String placeholder;

    BukkitPlaceholder(String placeholder, Function<Player, String> mappingFunction) {
        this.mappingFunction = mappingFunction;
        this.placeholder = placeholder;
    }

    public Function<Player, String> getMappingFunction() {
        return mappingFunction;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
