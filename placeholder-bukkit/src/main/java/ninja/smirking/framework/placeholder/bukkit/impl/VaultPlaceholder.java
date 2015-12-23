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

import ninja.smirking.framework.placeholder.api.ConditionalPlaceholder;
import ninja.smirking.framework.placeholder.bukkit.util.VaultUtil;

import java.util.Optional;
import java.util.function.Function;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public enum VaultPlaceholder implements ConditionalPlaceholder<Server, Player> {
    PLAYER_BALANCE("balance", player -> {
        Optional<Economy> economy = VaultUtil.getEconomy(player.getServer());
        if (economy.isPresent()) {
            return VaultUtil.formatBalance(economy.get().getBalance(player));
        }
        return VaultUtil.formatBalance(0.00D);
    }),
    BANK_BALANCE("bank_balance", player -> {
        Optional<Economy> economy = VaultUtil.getEconomy(player.getServer());
        if (VaultUtil.isEconomyEnabled(economy) && economy.get().hasBankSupport()) {
            double balance = 0.00D;
            for (String bankName : economy.get().getBanks()) {
                EconomyResponse response = economy.get().isBankMember(bankName, player);
                if (response.type == EconomyResponse.ResponseType.SUCCESS) {
                    response = economy.get().bankBalance(bankName);
                    if (response.type == EconomyResponse.ResponseType.SUCCESS) {
                        balance += response.balance;
                    }
                }
            }
            return VaultUtil.formatBalance(balance);
        }
        return VaultUtil.formatBalance(0.00D);
    }),
    ECONOMY_NAME("economy", player -> {
        Optional<Economy> economy = VaultUtil.getEconomy(player.getServer());
        if (VaultUtil.isEconomyEnabled(economy)) {
            return String.valueOf(economy.get().getName());
        }
        return "";
    });

    private final Function<Player, String> mappingFunction;
    private final String text;

    VaultPlaceholder(String text, Function<Player, String> mappingFunction) {
        this.mappingFunction = mappingFunction;
        this.text = text;
    }

    @Override
    public boolean isRegisterable(Server server) {
        if (!server.getPluginManager().isPluginEnabled("Vault")) {
            return false;
        }
        return VaultUtil.isEconomyEnabled(VaultUtil.getEconomy(server));
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
