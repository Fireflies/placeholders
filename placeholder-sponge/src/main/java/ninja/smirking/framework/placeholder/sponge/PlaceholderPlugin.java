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
        // TODO: Share Injector instance without creating singleton.
    }

    @Listener
    public void onDisable(GameStoppingServerEvent event) {
        placeholderManager.release();
    }

    public Injector getInjector() {
        return injector;
    }
}
