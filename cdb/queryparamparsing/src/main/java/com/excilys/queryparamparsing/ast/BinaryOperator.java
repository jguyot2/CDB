package com.excilys.queryparamparsing.ast;

import java.util.Objects;

import com.excilys.queryparamparsing.ast.typing.AtomicType;

/**
 * Opérateurs logiques sur les
 *
 * @author mono
 *
 */
public enum BinaryOperator {
    LIKE {
        @Override
        public boolean isCorrectInput(final AtomicType left, final AtomicType right) {
            return left == AtomicType.STRING && right == AtomicType.STRING;
        }
    },
    EQ {
        @Override
        public boolean isCorrectInput(final AtomicType left, final AtomicType right) {
            return Objects.equals(left, right);
        }
    },
    GT {
        @Override
        public boolean isCorrectInput(final AtomicType left, final AtomicType right) {
            return Objects.equals(left, right);
        }
    },
    LT {
        @Override
        public boolean isCorrectInput(final AtomicType left, final AtomicType right) {
            return Objects.equals(left, right);
        }
    },
    GTE {
        @Override
        public boolean isCorrectInput(final AtomicType left, final AtomicType right) {
            return Objects.equals(left, right);
        }
    },
    LTE {
        @Override
        public boolean isCorrectInput(final AtomicType left, final AtomicType right) {
            return Objects.equals(left, right);
        }
    },
    INSTR {
        @Override
        public boolean isCorrectInput(final AtomicType left, final AtomicType right) {
            return left == AtomicType.STRING && right == AtomicType.STRING;
        }
    };

    public static BinaryOperator getOperatorFromString(final String str) throws IllegalArgumentException {
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

    public AtomicType getOutputType() {
        return AtomicType.BOOLEAN;
    }

    public abstract boolean isCorrectInput(final AtomicType leftInputType, final AtomicType rightInputType);
}
