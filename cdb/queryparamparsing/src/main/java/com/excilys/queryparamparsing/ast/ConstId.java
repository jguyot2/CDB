package com.excilys.queryparamparsing.ast;

import java.util.Objects;

import com.excilys.queryparamparsing.ast.typing.AtomicType;

public class ConstId extends Constant {
    Long value;

    public ConstId(final Long id) {
        this.value = id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConstId)) {
            return false;
        }
        ConstId other = (ConstId) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public AtomicType getType() {
        return AtomicType.ID;
    }

    public Long getValue() {
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
