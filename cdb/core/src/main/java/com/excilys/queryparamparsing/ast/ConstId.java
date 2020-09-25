package com.excilys.queryparamparsing.ast;

// TODO Pë indiquer la table référencée ?
// Permet plus de type-safety mais pas évident
public class ConstId extends Constant {
	Long value;

	public ConstId(final Long id) {
		this.value = id;
	}

	public Long getValue() {
		return this.value;
	}

	@Override
	public Type getType() {
		return Type.ID;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}
}
