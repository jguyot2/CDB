package com.excilys.queryparamparsing.ast;

import com.excilys.queryparamparsing.ast.typing.AtomicType;
import com.excilys.queryparamparsing.ast.typing.TypingException;

public abstract class Expression {
    @Override
    public abstract boolean equals(Object other);

    public abstract AtomicType getType();

    @Override
    public abstract int hashCode();

    public abstract void type() throws TypingException;

    // TODO : Récupération d'une chaîne correspondant à un PreparedStatement
    // +Map<Integer String>qui indiquerait quoi remplir où

}
