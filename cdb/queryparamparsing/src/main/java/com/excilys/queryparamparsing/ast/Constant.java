package com.excilys.queryparamparsing.ast;

import com.excilys.queryparamparsing.ast.typing.ComplexType;

/**
 * Constantes, e.g des chaînes de caractères ou des nombres directement mis dans
 * la requête
 *
 * @author jguyot2
 *
 */
public abstract class Constant extends Expression {
    public static Constant of(final ComplexType t, final String str) {
        System.out.println(t);
        System.out.println(t.getType());
        switch (t.getType()) {
        case BOOLEAN:
            return new ConstBool(Boolean.parseBoolean(str));
        case ID:
            return new ConstId(Long.parseLong(str), t.getTableName());
        case STRING:
            return new ConstString(str);
        case INTEGER:
            return new ConstInteger(Integer.parseInt(str));
        case DATE:
        default:
            throw new IllegalArgumentException(); // TODO
        }
    }

    @Override
    public void type() {
    }

}
