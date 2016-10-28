package de.joshavg.yaliargpwl;

import java.util.Map;
import java.util.function.Predicate;

class RequiredParamsChecker {

    private final Map<String, CliParameter> parsed;
    private final Map<CliParameter, Predicate<Map<String, CliParameter>>> requiredIfParams;

    RequiredParamsChecker(final Map<String, CliParameter> parsed,
                          final Map<CliParameter, Predicate<Map<String, CliParameter>>> requiredIfParams) {
        this.parsed = parsed;
        this.requiredIfParams = requiredIfParams;
    }

    void run() {
        this.requiredIfParams.entrySet().forEach(e -> {
            if (e.getValue().test(this.parsed) && !this.parsed.containsKey(e.getKey().getName())) {
                throw new IllegalStateException("required parameter " + e.getKey().getName() + " not found");
            }
        });
    }

}
