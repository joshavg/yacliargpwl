package de.joshavg.yaliargpwl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * parses you cli args passed to your main method and lets you register functions to execute if a certain parameter is
 * set
 * <p>
 * Handles Cli Args that are formed like
 * <code>
 * --flagArg --valueArg1=argValue --valueArg2=argValue
 * </code>
 *
 * @author jgizycki (Josha von Gizycki, josha.von.gizycki@triology.de)
 */
public class CliArgsHandler {

    /**
     * original arguments
     */
    private final List<String> args;

    /**
     * run modes to be fired when a certain flag param is met
     */
    private final Map<CliParameter, Consumer<Map<String, CliParameter>>> runModes;

    /**
     * accepted parameters
     */
    private final List<CliParameter> params;

    /**
     * parameters that are required if their corresponding predicate evaluates to true
     */
    private final Map<CliParameter, Predicate<Map<String, CliParameter>>> requiredIfParams;

    /**
     * creates your desired args handler and lets you continue working on it with its fluent api until you finally
     * kick-off its workflow
     *
     * @param args
     */
    public CliArgsHandler(final String... args) {
        this.args = Arrays.asList(args);

        this.runModes = new HashMap<>();
        this.params = new ArrayList<>();
        this.requiredIfParams = new HashMap<>();
    }

    /**
     * asserts that the value passed to it with the given name is not null. otherwise, throw an NPE
     *
     * @param name
     * @param value
     * @throws NullPointerException
     */
    private static void assertNotNull(final String name, final Object value) {
        if (value == null) {
            throw new NullPointerException(name + " must not be null");
        }
    }

    /**
     * registers a runmode that will be called when the given flag is met.
     * <p>
     * the parameter objects passed to runMode will be the same objects you registered using
     * {@link #acceptParam(CliParameter)} etc
     *
     * @param param
     * @param runMode
     * @return
     *
     * @throws NullPointerException if param is null
     * @throws NullPointerException if runMode is null
     * @throws IllegalArgumentException if the given param is not yet registered using {@link #acceptParam(String)} or
     *             {@link #acceptParam(CliParameter)}
     */
    public CliArgsHandler withRunMode(final CliParameter param, final Consumer<Map<String, CliParameter>> runMode) {
        assertNotNull("param", param);
        assertNotNull("runMode", runMode);

        if (!this.params.contains(param)) {
            throw new IllegalArgumentException("param " + param + " has not been registered yet");
        }

        this.runModes.put(param, runMode);
        return this;
    }

    /**
     * adds a parameter to be accepted by the parser. allows to configure the parameter further.
     *
     * @param param
     * @return
     *
     * @throws NullPointerException if param is null
     */
    public CliArgsHandler acceptParam(final CliParameter param) {
        assertNotNull("param", param);

        this.params.add(param);
        return this;
    }

    /**
     * adds a parameter to be required. also adds it to the standard accepted range of parameters. no need to add it
     * twice.
     *
     * @param param
     * @return
     *
     * @throws NullPointerException if param is null
     */
    public CliArgsHandler requireParam(final CliParameter param) {
        assertNotNull("param", param);

        requireParamIf(param, e -> true);
        this.params.add(param);
        return this;
    }

    /**
     * adds a parameter to be required if the given predicate evaluates to true
     *
     * @param param
     * @param predicate
     * @return
     *
     * @throws NullPointerException if param is null
     * @throws NullPointerException if predicate is null
     */
    public CliArgsHandler requireParamIf(final CliParameter param,
                                         final Predicate<Map<String, CliParameter>> predicate) {
        assertNotNull("param", param);
        assertNotNull("predicate", predicate);

        this.requiredIfParams.put(param, predicate);
        return this;
    }

    /**
     * @return
     * @throws IllegalStateException if a required param is not found
     */
    public Map<String, CliParameter> run() {
        final Map<String, CliParameter> parsed = new ParamsParser(this.args, this.params).parse();

        final RequiredParamsChecker argsChecker = new RequiredParamsChecker(parsed, this.requiredIfParams);
        argsChecker.run();

        //@formatter:off
        this.runModes.entrySet().stream()
            .filter(e -> this.args.contains(e.getKey().getName()))
            .forEach(e -> e.getValue().accept(parsed));
        //@formatter:on

        return parsed;
    }

}
