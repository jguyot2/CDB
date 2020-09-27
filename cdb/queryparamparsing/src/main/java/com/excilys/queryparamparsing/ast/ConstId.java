package com.excilys.queryparamparsing.ast;

import java.util.Objects;

import com.excilys.queryparamparsing.ast.typing.ComplexType;

public class ConstId extends Constant {
    Long value;
    String tableName; // Nom de l'id de la table

    public ConstId(final Long id, final String tableName) {
        this.value = id;
        this.tableName = tableName;
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
        return Objects.equals(this.tableName, other.tableName) && Objects.equals(this.value, other.value);
    }

    @Override
    public ComplexType getType() {
        return ComplexType.of(this.tableName);
    }

    public Long getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tableName, this.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
