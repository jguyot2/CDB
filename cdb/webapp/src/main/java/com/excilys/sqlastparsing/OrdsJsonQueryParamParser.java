package com.excilys.sqlastparsing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.excilys.queryparamparsing.ast.BinaryOperator;
import com.excilys.queryparamparsing.ast.ComplexPredicate;
import com.excilys.queryparamparsing.ast.ConstString;
import com.excilys.queryparamparsing.ast.Expression;
import com.excilys.queryparamparsing.ast.LogicalOperator;
import com.excilys.queryparamparsing.ast.SimplePredicate;
import com.excilys.queryparamparsing.ast.TableColumn;
import com.excilys.queryparamparsing.ast.Type;

public class OrdsJsonQueryParamParser {

	public Expression parseComplicatedPredicate(final JSONObject queryParam) throws JSONException {
		Iterator<String> keys = queryParam.keys();
		if (!keys.hasNext()) {
			throw new RuntimeException("the json is shit");
		}

		String key = keys.next();

		if (key.startsWith("$")) { // Opérateur = AND/OR
			return parseComplicatedOperation(key, queryParam.getJSONArray(key));
		} else { // Nom de colonne
			JSONObject subObject = queryParam.getJSONObject(key);
			return parseSimpleOperation(key, subObject);
		}
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

	private Expression parseSimpleOperation(final String columnName, final JSONObject finalOperator) {
		Iterator<String> keys = finalOperator.keys();
		if (!keys.hasNext()) {
			throw new IllegalArgumentException("the json is still shit");
		}

		String opName = keys.next();
		String value = finalOperator.getString(opName);

		/// TODO : récupération du type associé à la valeur ///
		Type typ = Type.STRING;
		Expression e = new ConstString(value);
		///////////////////////////////////////////////////////

		TableColumn t = new TableColumn(typ, columnName);
		BinaryOperator op = BinaryOperator.getOperatorFromString(opName);
		return new SimplePredicate(t, op, e);
	}
}
