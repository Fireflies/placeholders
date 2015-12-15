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
