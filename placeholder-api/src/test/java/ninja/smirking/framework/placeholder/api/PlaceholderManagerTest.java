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