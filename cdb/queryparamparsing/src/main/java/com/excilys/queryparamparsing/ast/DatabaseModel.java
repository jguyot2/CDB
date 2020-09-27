package com.excilys.queryparamparsing.ast;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.excilys.queryparamparsing.ast.typing.AtomicType;
import com.excilys.queryparamparsing.ast.typing.ComplexType;

@Component
public class DatabaseModel {
    // TODO : récupération des noms/types de tables à partir d'un fichier de
    // config/properties
    public static DatabaseModel getCdbModel() {
        Map<String, ComplexType> computerColumns = new HashMap<>();
        computerColumns.put("name", ComplexType.of(AtomicType.STRING));
        computerColumns.put("introduced", ComplexType.of(AtomicType.DATE));
        computerColumns.put("discontinued", ComplexType.of(AtomicType.DATE));
        computerColumns.put("id", ComplexType.of("computers"));
        computerColumns.put("company_id", ComplexType.of("companies"));
        TableReference computerTable = new TableReference("computers", computerColumns);

        Map<String, ComplexType> companyColumns = new HashMap<>();
        companyColumns.put("name", ComplexType.of(AtomicType.STRING));
        companyColumns.put("id", ComplexType.of("companies"));
        TableReference companyTable = new TableReference("companies", companyColumns);

        Map<String, TableReference> databaseModel = new HashMap<>();
        databaseModel.put("computers", computerTable);
        databaseModel.put("companies", companyTable);

        return new DatabaseModel(databaseModel);
    }

    public Map<String, TableReference> databaseModel;

    private DatabaseModel(final Map<String, TableReference> model) {
        this.databaseModel = model;
    }

    public TableReference getTableRef(final String tableName) {
        return this.databaseModel.get(tableName);
    }
}
