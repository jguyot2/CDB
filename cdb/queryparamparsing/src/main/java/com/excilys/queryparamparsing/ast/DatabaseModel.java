package com.excilys.queryparamparsing.ast;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.excilys.queryparamparsing.ast.typing.AtomicType;

@Component
public class DatabaseModel {
    // TODO : récupération des noms/types de tables à partir d'un fichier de
    // config/properties
    public static DatabaseModel getCdbModel() {
        Map<String, AtomicType> computerColumns = new HashMap<>();
        computerColumns.put("name", AtomicType.STRING);
        computerColumns.put("introduced", AtomicType.DATE);
        computerColumns.put("discontinued", AtomicType.DATE);
        computerColumns.put("id", AtomicType.ID);
        computerColumns.put("company_id", AtomicType.ID);
        TableReference computerTable = new TableReference("computers", computerColumns);

        Map<String, AtomicType> companyColumns = new HashMap<>();
        companyColumns.put("name", AtomicType.STRING);
        companyColumns.put("id", AtomicType.ID);
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
