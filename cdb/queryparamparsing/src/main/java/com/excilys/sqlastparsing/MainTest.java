package com.excilys.sqlastparsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.excilys.queryparamparsing.ast.DatabaseModel;
import com.excilys.queryparamparsing.ast.typing.TypingException;

public class MainTest {
    public static void main(final String... args) throws IOException, TypingException {
        MainTest t = new MainTest();
        t.init();
        t.printFile();
    }

    private OrdsJsonQueryParamParser parser = new OrdsJsonQueryParamParser(DatabaseModel.getCdbModel());

    private void init() {
        this.parser.setTableName("computers");
    }

    public void printFile() throws IOException, TypingException {
        String s = Files.lines(new File("src/main/resources/json-computer1").toPath())
                .collect(Collectors.joining("\n"));
        JSONObject o = new JSONObject(s);
        System.out.println("SELECT * FROM COMPUTER WHERE ");
        System.out.println(this.parser.parseJSon(o));
    }

}
