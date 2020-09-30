package com.excilys.queryparamparsing.ast;

import java.util.Objects;

import com.excilys.queryparamparsing.ast.typing.AtomicType;

public class ConstBool extends Constant {

    private boolean value;

    public ConstBool(final boolean value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConstBool)) {
            return false;
        }
        ConstBool other = (ConstBool) obj;
        return this.value == other.value;
    }

    @Override
    public AtomicType getType() {
        return AtomicType.BOOLEAN;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}
