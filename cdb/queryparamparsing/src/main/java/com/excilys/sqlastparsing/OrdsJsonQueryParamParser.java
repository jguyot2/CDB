package com.excilys.sqlastparsing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.queryparamparsing.ast.BinaryOperator;
import com.excilys.queryparamparsing.ast.ComplexPredicate;
import com.excilys.queryparamparsing.ast.Constant;
import com.excilys.queryparamparsing.ast.DatabaseModel;
import com.excilys.queryparamparsing.ast.Expression;
import com.excilys.queryparamparsing.ast.LogicalOperator;
import com.excilys.queryparamparsing.ast.SimplePredicate;
import com.excilys.queryparamparsing.ast.TableReference;
import com.excilys.queryparamparsing.ast.typing.AtomicType;
import com.excilys.queryparamparsing.ast.typing.TypingException;

@Component
public class OrdsJsonQueryParamParser {
    private final DatabaseModel model;

    private String tableName;

    @Autowired
    public OrdsJsonQueryParamParser(final DatabaseModel model) {
        this.model = model;
    }

    private Expression parseComplicatedOperation(final String operator, final JSONArray subExpressions) {
        LogicalOperator op = LogicalOperator.getFromString(operator);
        int length = subExpressions.length();
        List<Expression> l = new ArrayList<>();
        for (int i = 0; i < length; ++i) {
            l.add(parseComplicatedPredicate(subExpressions.getJSONObject(i)));
        }
        return new ComplexPredicate(op, l);
    }

    private Expression parseComplicatedPredicate(final JSONObject queryParam) throws JSONException {
        Iterator<String> keys = queryParam.keys();
        if (!keys.hasNext()) {
            throw new RuntimeException("the json is shit");
        }

        String key = keys.next();

        if (key.startsWith("$")) { // Opérateur = AND/OR => Sous-appel récursif
            return parseComplicatedOperation(key, queryParam.getJSONArray(key));
        } else { // Nom de colonne => opération finale
            JSONObject subObject = queryParam.getJSONObject(key);
            return parseSimpleOperation(key, subObject);
        }
    }

    public Expression parseJSon(final JSONObject queryParam) throws TypingException {
        Expression e = parseComplicatedPredicate(queryParam);
        e.type();
        return e;
    }

    private Expression parseSimpleOperation(final String columnName, final JSONObject finalOperator) {
        Iterator<String> keys = finalOperator.keys();
        if (!keys.hasNext()) {
            throw new IllegalArgumentException("the json is still shit");
        }

        String opName = keys.next();
        String value = finalOperator.getString(opName);

        TableReference tref = this.model.getTableRef(this.tableName);
        AtomicType typ = tref.getColumnType(columnName);

        Expression e = Constant.of(typ, value);

        BinaryOperator op = BinaryOperator.getOperatorFromString(opName);
        return new SimplePredicate(columnName, op, e, typ);
    }

    public void setTableName(final String newTableName) {
        this.tableName = newTableName;
    }
}
