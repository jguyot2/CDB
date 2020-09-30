package com.excilys.queryparamparsing.ast;

import java.util.Objects;

import com.excilys.queryparamparsing.ast.typing.AtomicType;

public class ConstString extends Constant {
    private String value;

    public ConstString(final String value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConstString)) {
            return false;
        }
        ConstString other = (ConstString) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public AtomicType getType() {
        return AtomicType.STRING;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return "\"" + this.value + "\"";
    }
}
