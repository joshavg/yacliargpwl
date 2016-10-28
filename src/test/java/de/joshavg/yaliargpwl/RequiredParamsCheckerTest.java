package de.joshavg.yaliargpwl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Test;

public class RequiredParamsCheckerTest {

    private final RequiredParamsChecker checker;

    private final Map<Integer, Boolean> calls = new HashMap<>();

    {
        final Map<String, CliParameter> parsed = new HashMap<>();
        parsed.put("--arg1", new CliParameter("arg1"));

        final Map<CliParameter, Predicate<Map<String, CliParameter>>> requiredIfParams = new HashMap<>();
        requiredIfParams.put(new CliParameter("arg1"), i -> {
            this.calls.put(1, true);
            return true;
        });
        requiredIfParams.put(new CliParameter("notexistent"), i -> {
            this.calls.put(2, true);
            return true;
        });

        this.checker = new RequiredParamsChecker(parsed, requiredIfParams);
    }

    @Test
    public void test() {
        try {
            this.checker.run();
            fail();
        } catch (final IllegalStateException e) {
            assertTrue(this.calls.get(1));
            assertNull(this.calls.get(2));
        }
    }

}
