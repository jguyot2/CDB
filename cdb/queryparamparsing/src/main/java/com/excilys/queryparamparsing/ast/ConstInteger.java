package com.excilys.queryparamparsing.ast;

import java.util.Objects;

import com.excilys.queryparamparsing.ast.typing.AtomicType;

public class ConstInteger extends Constant {
    Integer value;

    public ConstInteger(final Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConstInteger)) {
            return false;
        }
        ConstInteger other = (ConstInteger) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public AtomicType getType() {
        return AtomicType.INTEGER;
    }

    public Integer getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
