package com.excilys.queryparamparsing.ast;

public class ConstInteger extends Constant {
	Integer value;

	public ConstInteger(final Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return this.value;
	}

	@Override
	public Type getType() {
		return Type.INTEGER;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}
}
