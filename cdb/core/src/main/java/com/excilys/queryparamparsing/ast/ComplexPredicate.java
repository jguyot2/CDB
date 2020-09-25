package com.excilys.queryparamparsing.ast;

import java.util.List;
import java.util.stream.Collectors;

public class ComplexPredicate implements Expression {
	LogicalOperator op;
	List<Expression> subExpressions;

	public ComplexPredicate(final LogicalOperator op, final List<Expression> subExpressions) {
		this.op = op;
		this.subExpressions = subExpressions;
	}

	@Override
	public Type getType() {
		return Type.BOOLEAN;
	}

	@Override
	public String toString() {
		return "(" + this.subExpressions.stream().map(Expression::toString)
				.collect(Collectors.joining(" " + this.op.toString() + " ")) + ")";
	}
}
