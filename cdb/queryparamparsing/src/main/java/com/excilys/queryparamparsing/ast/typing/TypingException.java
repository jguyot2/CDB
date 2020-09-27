package com.excilys.queryparamparsing.ast.typing;

import com.excilys.queryparamparsing.ast.Expression;

public class TypingException extends Exception {
    private static final long serialVersionUID = 1L;

    private final Expression exp;
    private final ComplexType expectedType;
    private final ComplexType foundType;

    public TypingException(final Expression e, final ComplexType expected, final ComplexType found) {
        this.exp = e;
        this.expectedType = expected;
        this.foundType = found;
    }

    public ComplexType getExpected() {
        return this.expectedType;
    }

    public Expression getExpression() {
        return this.exp;
    }

    public ComplexType getFound() {
        return this.foundType;
    }
}
