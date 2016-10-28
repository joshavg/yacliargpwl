package de.joshavg.yaliargpwl;

import java.util.Optional;

public class CliParameter {

    private final String name;

    private final String helpText;

    private String value;

    public CliParameter(final String name) {
        this(name, null);
    }

    public CliParameter(final String name, final String helpText) {
        if (name == null) {
            throw new NullPointerException("name must not be null");
        }
        this.name = name;
        this.helpText = helpText;
        this.value = null;
    }

    public static CliParameter of(final String name) {
        if (name == null) {
            throw new NullPointerException("name must not be null");
        }
        return new CliParameter(name);
    }

    public String getName() {
        return this.name;
    }

    public Optional<String> getHelpText() {
        return Optional.of(this.helpText);
    }

    public Optional<String> getValue() {
        return Optional.of(this.value);
    }

    public CliParameter setValue(final String value) {
        this.value = value;
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CliParameter)) {
            return false;
        }
        final CliParameter other = (CliParameter) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
