package com.excilys.queryparamparsing.ast.typing;

import com.excilys.queryparamparsing.ast.Expression;

public class TypingException extends Exception {
    private static final long serialVersionUID = 1L;

    private final Expression exp;
    private final AtomicType expectedType;
    private final AtomicType foundType;

    public TypingException(final Expression e, final AtomicType expected, final AtomicType found) {
        this.exp = e;
        this.expectedType = expected;
        this.foundType = found;
    }

    public AtomicType getExpected() {
        return this.expectedType;
    }

    public Expression getExpression() {
        return this.exp;
    }

    public AtomicType getFound() {
        return this.foundType;
    }
}
