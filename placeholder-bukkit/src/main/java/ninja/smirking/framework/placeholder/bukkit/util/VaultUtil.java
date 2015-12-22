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

package ninja.smirking.framework.placeholder.bukkit.util;

import java.text.DecimalFormat;
import java.util.Optional;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;

public final class VaultUtil {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    static {
        DECIMAL_FORMAT.setMinimumFractionDigits(2);
    }

    private VaultUtil() {
        throw new UnsupportedOperationException("VaultUtil cannot be instantiated!");
    }

    public static boolean isEnabled(Server server) {
        return server.getPluginManager().isPluginEnabled("Vault");
    }

    public static boolean isEconomyEnabled(Optional<Economy> economy) {
        return economy.isPresent() && economy.get().isEnabled();
    }

    public static Optional<Economy> getEconomy(Server server) {
        if (isEnabled(server)) {
            return Optional.ofNullable(server.getServicesManager().getRegistration(Economy.class).getProvider());
        }
        return Optional.empty();
    }

    public static String formatBalance(double balance) {
        return DECIMAL_FORMAT.format(balance);
    }
}
