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

import java.util.Objects;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class PlaceholderManagerTest {
    private final PlaceholderManager<String, String> placeholderManager = new PlaceholderManager<>();

    @Rule
    public TestName testName = new TestName();

    @Test
    public void testRegisterMapping() throws Exception {
        placeholderManager.registerMapping("JUnit", "test_name", s -> testName.getMethodName());
        assertTrue("mapping function should be registered", placeholderManager.isMapped("test_name"));
    }

    @Test
    public void testUnregisterMapping() throws Exception {
        placeholderManager.registerMapping("JUnit", "test_name", s -> testName.getMethodName());
        assumeTrue("mapping function should be registered", placeholderManager.isMapped("test_name"));
        placeholderManager.unregisterMappings("JUnit");
        assertFalse("mapping function should be unregistered", placeholderManager.isMapped("test_name"));
    }

    @Test
    public void testFormat() throws Exception {
        placeholderManager.registerMapping("JUnit", "test_name", s -> testName.getMethodName());
        assumeTrue("mapping function should be registered", placeholderManager.isMapped("test_name"));

        String message = "Running test \"#{test_name}\" inside " + getClass().getSimpleName();
        assertEquals(message.replace("#{test_name}", testName.getMethodName()), placeholderManager.format("", message));
        assertEquals("plain", placeholderManager.format("", "plain"));
        assertEquals("", placeholderManager.format("", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertThat() throws Exception {
        placeholderManager.assertThat(null, "reference should not be null", Objects::nonNull);
    }

    @Test
    public void testRelease() throws Exception {
        placeholderManager.registerMapping("JUnit", "test_name", s -> testName.getMethodName());
        assumeTrue("mapping function should be registered", placeholderManager.isMapped("test_name"));
        placeholderManager.release();
        assertFalse("mapping function should be released", placeholderManager.isMapped("test_name"));
    }
}