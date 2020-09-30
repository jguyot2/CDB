package com.excilys.queryparamparsing.ast;

import java.util.Objects;

import com.excilys.queryparamparsing.ast.typing.AtomicType;
import com.excilys.queryparamparsing.ast.typing.TypingException;

/**
 * Prédicat final, id est pas constitué de sous-expressions
 *
 * @author jguyot2
 */
public class SimplePredicate extends Expression {
    private String columnName;
    private AtomicType columnType;
    private BinaryOperator op;
    private Expression rightOperand; // Une constante

    // TODO : Pattern builder ?
    public SimplePredicate(final String col, final BinaryOperator op, final Expression rightOperand,
            final AtomicType t) {
        this.columnName = col;
        this.op = op;
        this.rightOperand = rightOperand;
        this.columnType = t;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SimplePredicate)) {
            return false;
        }
        SimplePredicate other = (SimplePredicate) obj;
        return Objects.equals(this.columnName, other.columnName) && Objects.equals(this.columnType, other.columnType)
                && this.op == other.op && Objects.equals(this.rightOperand, other.rightOperand);
    }

    public String getColumnName() {
        return this.columnName;
    }

    public BinaryOperator getOperator() {
        return this.op;
    }

    public Expression getSubExpression() {
        return this.rightOperand;
    }

    @Override
    public AtomicType getType() {
        return AtomicType.BOOLEAN;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.columnName, this.columnType, this.op, this.rightOperand);
    }

    @Override
    public String toString() {
        return this.columnName + " " + this.op + " " + this.rightOperand;
    }

    @Override
    public void type() throws TypingException {
        if (!this.op.isCorrectInput(this.columnType, this.rightOperand.getType())) {
            throw new TypingException(this.rightOperand, this.columnType, this.rightOperand.getType());
        }
    }
}
