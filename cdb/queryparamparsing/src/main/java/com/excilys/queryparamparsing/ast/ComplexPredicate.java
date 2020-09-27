package com.excilys.queryparamparsing.ast;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excilys.queryparamparsing.ast.typing.AtomicType;
import com.excilys.queryparamparsing.ast.typing.ComplexType;
import com.excilys.queryparamparsing.ast.typing.TypingException;

/**
 * Prédicat composé d'une liste de sous-expressions booléennes liées par un
 * opérateur AND ou OR
 *
 * @author mono
 */
public class ComplexPredicate extends Expression {

    LogicalOperator op;

    List<Expression> subExpressions;

    public ComplexPredicate(final LogicalOperator op, final List<Expression> subExpressions) {
        this.op = op;
        this.subExpressions = subExpressions;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ComplexPredicate)) {
            return false;
        }
        ComplexPredicate other = (ComplexPredicate) obj;
        return this.op == other.op && Objects.equals(this.subExpressions, other.subExpressions);
    }

    @Override
    public ComplexType getType() {
        return ComplexType.of(AtomicType.BOOLEAN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.op, this.subExpressions);
    }

    @Override
    public String toString() {
        return "(" + this.subExpressions.stream().map(Expression::toString)
                .collect(Collectors.joining(" " + this.op.toString() + " ")) + ")";
    }

    @Override
    public void type() throws TypingException {
        Optional<Expression> notBooleanExpression = this.subExpressions.stream()
                .filter(e -> !e.getType().equals(ComplexType.of(AtomicType.BOOLEAN)))
                .findFirst();
        if (notBooleanExpression.isPresent()) {
            throw new TypingException(notBooleanExpression.get(), ComplexType.of(AtomicType.BOOLEAN),
                    notBooleanExpression.get().getType());
        }
    }

}
