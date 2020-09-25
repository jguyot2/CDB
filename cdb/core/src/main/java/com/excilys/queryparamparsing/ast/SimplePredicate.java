package com.excilys.queryparamparsing.ast;

/**
 * Prédicat final, id est qui agit en fonction d'un nom de colonne et d'une
 * sous-expression
 *
 * @author jguyot2
 */
public class SimplePredicate implements Expression {
	private TableColumn col;
	private BinaryOperator op;
	private Expression rightOperand;

	public SimplePredicate(final TableColumn col, final BinaryOperator op, final Expression rightOperand) {
		this.col = col;
		this.op = op;
		this.rightOperand = rightOperand;
	}

	@Override
	public String toString() {
		return this.col + " " + this.op + " " + this.rightOperand;
	}

	public TableColumn getColumn() {
		return this.col;
	}

	public BinaryOperator getOperator() {
		return this.op;
	}

	public Expression getSubExpression() {
		return this.rightOperand;
	}

	@Override
	public Type getType() {
		return Type.BOOLEAN;
	}
}
