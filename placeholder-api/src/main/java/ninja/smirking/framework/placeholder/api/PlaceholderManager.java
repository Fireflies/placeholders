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

package ninja.smirking.framework.placeholder.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @param <TPlayer> player type for the server implementation
 * @param <TPlugin> plugin type for the server implementation
 *
 * @author Connor Spencer Harries
 */
public class PlaceholderManager<TPlugin, TPlayer> {
    private static final Predicate<String> NOT_NULL_OR_EMPTY = string -> string != null && !string.isEmpty();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("#\\{([a-zA-Z-_]{1,32})\\}");

    private final Map<TPlugin, Set<Function<TPlayer, String>>> mappingFunctionsByRegistrant;
    private final Map<String, Function<TPlayer, String>> mappingFunctions;

    protected PlaceholderManager() {
        this.mappingFunctionsByRegistrant = new HashMap<>();
        this.mappingFunctions = new ConcurrentHashMap<String, Function<TPlayer, String>>() {{
            put("hash_code", p -> String.valueOf(p.hashCode()));
            put("to_string", String::valueOf);
        }};
    }

    public final void registerMapping(TPlugin registrant, String placeholder, Function<TPlayer, String> mappingFunction) {
        assertThat(placeholder, "placeholder should not be null or empty", NOT_NULL_OR_EMPTY);
        assertThat(mappingFunction, "mappingFunction should not be null", Objects::nonNull);
        assertThat(registrant, "registrant should not be null", Objects::nonNull);
        placeholder = placeholder.toLowerCase();

        mappingFunctionsByRegistrant.computeIfAbsent(registrant, plugin -> new HashSet<>()).add(mappingFunction);
        mappingFunctions.put(placeholder, mappingFunction);
    }

    public final void registerMapping(TPlugin registrant, Placeholder<TPlayer> placeholder) {
        registerMapping(registrant, placeholder.getText(), placeholder.getMappingFunction());
    }

    public final void unregisterMappings(TPlugin registrant) {
        assertThat(registrant, "registrant should not be null", Objects::nonNull);
        Set<Function<TPlayer, String>> functions = mappingFunctionsByRegistrant.remove(registrant);
        if (functions != null) {
            for (Iterator<Function<TPlayer, String>> mappingFunction = mappingFunctions.values().iterator(); mappingFunction.hasNext(); ) {
                if (functions.remove(mappingFunction.next())) {
                    mappingFunction.remove();
                }
            }
        }
    }

    public final Function<TPlayer, String> getMappingFunction(String placeholder) {
        assertThat(placeholder, "placeholder should not be null or empty", NOT_NULL_OR_EMPTY);
        return mappingFunctions.get(placeholder);
    }

    public final boolean isMapped(String placeholder) {
        return getMappingFunction(placeholder) != null;
    }

    public final String format(TPlayer player, String string) {
        assertThat(player, "player should not be null", Objects::nonNull);
        if (string == null || string.isEmpty()) {
            return string;
        } else {
            Matcher matcher = PLACEHOLDER_PATTERN.matcher(string);
            boolean foundAny = matcher.find();
            if (foundAny) {
                Function<TPlayer, String> defaultMapping = ignored -> matcher.group(0);
                StringBuilder buffer = new StringBuilder();
                int position = 0;
                while (foundAny) {
                    String placeholder = matcher.group(1);
                    buffer.append(string.substring(position, matcher.start()));

                    Function<TPlayer, String> mappingFunction = mappingFunctions.getOrDefault(placeholder, defaultMapping);
                    String result = mappingFunction.apply(player);
                    if (result == null) {
                        buffer.append(matcher.group(0));
                    } else {
                        buffer.append(result);
                    }
                    position = matcher.end();
                    foundAny = matcher.find();
                }
                return buffer.append(string.substring(position, string.length())).toString();
            }
            return string;
        }
    }

    public final void release() {
        mappingFunctionsByRegistrant.clear();
        mappingFunctions.clear();
    }

    <T> void assertThat(T reference, String message, Predicate<T> test) {
        if (!test.test(reference)) {
            throw new IllegalArgumentException(message);
        }
    }
}
