package com.example.handler;

import com.example.models.Condition;
import com.example.models.Metadata;
import com.example.models.Row;
import com.example.models.TableQuery;
import com.example.models.enums.Operation;
import com.example.services.accessor.FileAccessorImpl;
import com.example.services.metadata.DatabaseMetadataServiceImpl;
import com.example.services.metadata.MetadataService;
import com.example.services.parser.DatabaseParser;
import com.example.services.parser.TableParser;
import com.example.services.processor.TableProcessor;
import lombok.SneakyThrows;

import java.util.*;


public class InputOperation {

    private static Metadata metadata;

    public static void query(Scanner scanner) {
        QUERY: do {
            try {
                System.out.print("SQL> ");
                final String query = scanner.nextLine();
                operate(scanner, query);
            } catch (Exception e) {
                continue QUERY;
            }
        } while (true);
    }

    public static void operate(Scanner scanner, String query) {
        List<String> strings = Arrays.asList(query.split("\\s"));
        Operation operation = Operation.valueOf(strings.get(0).trim().toUpperCase(Locale.ROOT));

        operation.accept(new Operation.OperationVisitor<Void>() {

            final MetadataService metadataService = new DatabaseMetadataServiceImpl();
            final DatabaseParser databaseParser = new DatabaseParser(metadataService);
            final TableProcessor tableProcessor = new TableProcessor(new FileAccessorImpl());
            final TableParser tableParser = new TableParser();

            @Override
            public Void visitUse() {
                metadata = databaseParser.use(query);
                System.out.printf("%s selected \n", metadata.getDatabaseName());
                return null;
            }

            @Override
            public Void visitCreate() {
                return null;
            }

            @Override
            public Void visitDrop() {
                return null;
            }

            @Override
            public Void visitInsert() {
                return null;
            }

            @Override
            @SneakyThrows
            public Void visitSelect() {
                checkDatabase(metadata);
                TableQuery tableQuery = tableParser.select(query, metadata);
                List<Row> rows = tableProcessor.select(tableQuery);
                // Logger Logic
                return null;
            }

            @Override
            public Void visitUpdate() {
                return null;
            }

            @Override
            public Void visitDelete() {
                return null;
            }

            @Override
            public Void visitAlter() {
                return null;
            }

            @Override
            public Void visitTruncate() {
                return null;
            }
        });
    }

    private static void checkDatabase(Metadata metadata) {
        if (Objects.isNull(metadata)) {
            System.out.print("Please select database \n");
        }
    }
}
