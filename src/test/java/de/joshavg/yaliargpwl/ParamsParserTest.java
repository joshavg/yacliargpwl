package de.joshavg.yaliargpwl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ParamsParserTest {

    private final ParamsParser parser;

    {
        final List<String> args = new ArrayList<>();
        args.add("--flag1");
        args.add("--flag2");
        args.add("--arg1=argv1");
        args.add("--arg2=argv2");
        args.add("--notdefined");

        final List<CliParameter> params = new ArrayList<>();
        params.add(new CliParameter("--flag1"));
        params.add(new CliParameter("--flag2"));
        params.add(new CliParameter("--arg1"));
        params.add(new CliParameter("--arg2"));

        this.parser = new ParamsParser(args, params);
    }

    @Test
    public void testParser() {
        final Map<String, CliParameter> parsed = this.parser.parse();
        assertEquals("argv1", parsed.get("--arg1").getValue().get());
        assertEquals("argv2", parsed.get("--arg2").getValue().get());
        assertTrue(parsed.containsKey("--flag1"));
        assertTrue(parsed.containsKey("--flag2"));
        assertFalse(parsed.containsKey("--notdefined"));
    }

}
