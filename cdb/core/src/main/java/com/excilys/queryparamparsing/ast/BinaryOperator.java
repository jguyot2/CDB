package com.excilys.queryparamparsing.ast;

public enum BinaryOperator {
	LIKE {
		@Override
		public boolean isCorrectInput(final Type left, final Type right) {
			return left == Type.STRING && right == Type.STRING;
		}
	},
	EQ {
		@Override
		public boolean isCorrectInput(final Type left, final Type right) {
			return left == right;
		}
	},
	GT {
		@Override
		public boolean isCorrectInput(final Type left, final Type right) {
			return left == right;
		}
	},
	LT {
		@Override
		public boolean isCorrectInput(final Type left, final Type right) {
			return left == right;
		}
	},
	GTE {
		@Override
		public boolean isCorrectInput(final Type left, final Type right) {
			return left == right;
		}
	},
	LTE {
		@Override
		public boolean isCorrectInput(final Type left, final Type right) {
			return left == right;
		}
	},
	INSTR {
		@Override
		public boolean isCorrectInput(final Type left, final Type right) {
			return left == Type.STRING && right == Type.STRING;
		}
	};

	public abstract boolean isCorrectInput(final Type leftInputType, final Type rightInputType);

	public Type getOutputType() {
		return Type.BOOLEAN;
	}

	public static BinaryOperator getOperatorFromString(final String str) {
		switch (str) {

		case "$eq":
			return EQ;
		case "$like":
			return LIKE;
		case "$lt":
			return LT;
		case "$gt":
			return GT;
		case "$lte":
			return LTE;
		case "$gte":
			return GTE;
		case "$instr":
			return INSTR;
		default:
			throw new IllegalArgumentException();
		}
	}
}
