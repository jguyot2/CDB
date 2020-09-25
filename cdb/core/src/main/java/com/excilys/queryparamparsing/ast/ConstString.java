package com.excilys.queryparamparsing.ast;

public class ConstString extends Constant {
	private String value;

	public ConstString(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public Type getType() {
		return Type.STRING;
	}

	@Override
	public String toString() {
		return "\"" + this.value + "\"";
	}
}
