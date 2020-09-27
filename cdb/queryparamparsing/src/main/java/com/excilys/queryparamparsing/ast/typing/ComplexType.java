package com.excilys.queryparamparsing.ast.typing;

import java.util.NoSuchElementException;
import java.util.Objects;

public class ComplexType {
    public static ComplexType of(final AtomicType t) {
        return new ComplexType(t);
    }

    public static ComplexType of(final String tableName) {
        return new ComplexType(tableName);
    }

    private final String referencedTable;

    private final AtomicType t;

    private ComplexType(final AtomicType t) {
        if (t == AtomicType.ID) {
            throw new IllegalArgumentException("The other constructor must be used to create ID");
        }
        this.referencedTable = null;
        this.t = t;
    }

    private ComplexType(final String tableName) {
        assert tableName != null;

        this.referencedTable = tableName;
        this.t = AtomicType.ID;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ComplexType)) {
            return false;
        }

        if (this == obj) {
            return true;
        }
        ComplexType other = (ComplexType) obj;
        return Objects.equals(this.referencedTable, other.referencedTable) && this.t == other.t;
    }

    public String getTableName() {
        if (this.t != AtomicType.ID) {
            throw new NoSuchElementException();
        } else {
            return this.referencedTable;
        }
    }

    public AtomicType getType() {
        return this.t;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.referencedTable, this.t);
    }

    @Override
    public String toString() {
        return this.t + "" + (this.t == AtomicType.ID ? "(" + this.referencedTable + ")" : "");

    }
}
