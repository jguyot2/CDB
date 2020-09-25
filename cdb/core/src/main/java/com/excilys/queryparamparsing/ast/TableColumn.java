package com.excilys.queryparamparsing.ast;

public class TableColumn implements Expression {
	Type type;
	String identifier;

	public TableColumn(final Type t, final String identifier) {
		this.type = t;
		this.identifier = identifier;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return "(" + this.identifier + ":" + this.type + ")";
	}
}
