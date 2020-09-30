package com.excilys.queryparamparsing.ast;

import java.util.HashMap;
import java.util.Map;

import com.excilys.queryparamparsing.ast.typing.AtomicType;

public class TableReference {
    private Map<String, AtomicType> columnTypes;
    private String tableName;

    public TableReference(final String tableName, final Map<String, AtomicType> t) {
        this.columnTypes = new HashMap<>(t);
        this.tableName = tableName;
    }

    public AtomicType getColumnType(final String columnName) {
        return this.columnTypes.get(columnName);
    }

    public String getTableName() {
        return this.tableName;
    }

    @Override
    public String toString() {
        return this.tableName + ": " + this.columnTypes;
    }
}
