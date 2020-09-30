package com.excilys.queryparamparsing.ast;

import com.excilys.queryparamparsing.ast.typing.AtomicType;

/**
 * Constantes, e.g des chaînes de caractères ou des nombres directement mis dans
 * la requête
 *
 * @author jguyot2
 *
 */
public abstract class Constant extends Expression {
    public static Constant of(final AtomicType t, final String str) throws NumberFormatException {
        switch (t) {
        case BOOLEAN:
            return new ConstBool(Boolean.parseBoolean(str)); // TODO : changer ça parce que parseboolean pue du cul
        case ID:
            return new ConstId(Long.parseLong(str));
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
