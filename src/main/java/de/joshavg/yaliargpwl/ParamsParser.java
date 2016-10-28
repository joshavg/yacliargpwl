package de.joshavg.yaliargpwl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ParamsParser {

    private final List<String> args;
    private final List<CliParameter> params;

    ParamsParser(final List<String> args, final List<CliParameter> params) {
        this.args = args;
        this.params = params;
    }

    private String findArgValue(final CliParameter p) {
        final String argValue = this.args.stream().filter(s -> s.startsWith(p.getName())).findFirst().orElse("");
        if (argValue.length() > p.getName().length()) {
            return argValue.substring(p.getName().length() + 1);
        }
        return "";
    }

    Map<String, CliParameter> parse() {
        //@formatter:off
        return this.params.stream().map(p -> p.setValue(findArgValue(p)))
                                   .reduce(new HashMap<String, CliParameter>(),
                                           (map, param) -> {
                                               map.put(param.getName(), param);
                                               return map;
                                           },
                                           (map1, map2) -> {
                                               map1.putAll(map2);
                                               return map1;
                                           });
        //@formatter:on
    }

}
