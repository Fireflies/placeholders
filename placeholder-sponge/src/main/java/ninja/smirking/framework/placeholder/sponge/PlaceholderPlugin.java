package ninja.smirking.framework.placeholder.sponge;

import ninja.smirking.framework.placeholder.api.PlaceholderManager;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.Arrays;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "ninja.smirking.framework.placeholder", name = "PlaceholderEngine", version = "${project.version}")
public class PlaceholderPlugin {
    @Inject
    private Injector injector;

    private SpongePlaceholderManager placeholderManager;

    @Listener
    public void onLoad(GamePreInitializationEvent event) {
        placeholderManager = new SpongePlaceholderManager();
        Arrays.stream(SpongePlaceholder.values()).forEach(placeholder -> {
            placeholderManager.registerMapping(PlaceholderPlugin.this, placeholder.getPlaceholder(), placeholder.getMappingFunction());
            Arrays.stream(placeholder.getAliases()).forEach(alias -> placeholderManager.registerMapping(PlaceholderPlugin.this, alias, placeholder.getMappingFunction()));
        });
        injector = injector.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(SpongePlaceholderManager.class).toInstance(placeholderManager);
                bind(PlaceholderManager.class).toInstance(placeholderManager);
            }
        });
    }

    @Listener
    public void onDisable(GameStoppingServerEvent event) {
        placeholderManager.release();
    }

    public Injector getInjector() {
        return injector;
    }
}
