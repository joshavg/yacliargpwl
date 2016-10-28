package de.joshavg.yaliargpwl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CliArgsHandlerTest {

    private CliArgsHandler handler;

    @Before
    public void beforeEach() {
        this.handler = new CliArgsHandler("--flagarg1", "--flagarg2", "--arg=argv1");
    }

    @Test
    public void testRunMode() {
        final CliParameter flagarg1 = new CliParameter("--flagarg1");
        final CliParameter flagarg2 = new CliParameter("--flagarg2");
        final CliParameter nono = new CliParameter("nono");

        final Map<Integer, Boolean> calls = new HashMap<>();
        //@formatter:off
        this.handler.acceptParam(flagarg1)
                    .acceptParam(flagarg2)
                    .acceptParam(nono)
                    .withRunMode(flagarg1, m -> calls.put(1, true))
                    .withRunMode(flagarg2, m -> calls.put(2, true))
                    .withRunMode(nono, m -> fail())
                    .run();
        //@formatter:on

        assertTrue(calls.get(1));
        assertTrue(calls.get(2));
    }

    @Test
    public void testValues() {
        final CliParameter flagarg1 = new CliParameter("--flagarg1");
        final CliParameter flagarg2 = new CliParameter("--flagarg2");
        final CliParameter arg = new CliParameter("--arg");

        //@formatter:off
        final Map<String, CliParameter> parsed = this.handler.acceptParam(flagarg1)
                                                        .acceptParam(flagarg2)
                                                        .acceptParam(arg)
                                                        .run();
        //@formatter:on

        assertEquals("", parsed.get("--flagarg1").getValue().get());
        assertEquals("", parsed.get("--flagarg2").getValue().get());
        assertEquals("argv1", parsed.get("--arg").getValue().get());
    }

}
