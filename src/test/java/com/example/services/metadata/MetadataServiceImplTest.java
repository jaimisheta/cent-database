package com.example.services.metadata;

import com.example.models.*;
import com.example.models.enums.Entity;
import com.example.models.enums.Operation;
import com.example.services.accessor.FileAccessorImpl;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MetadataServiceImplTest {

    @Test
    @SneakyThrows
    public void testDatabaseMetadataService() {
        MetadataService metadataService = new MetadataServiceImpl();
        Metadata metadata = metadataService.read(Entity.DATABASE, "CENT_DB1");
        metadata.getAllTablesFromDatabase()
                .forEach(table -> {System.out.println("Table:"+ table.getName());
                    table.getColumns().forEach(col -> System.out.println("Column name :" + col.getName()));
                });

        List<String> columns = metadata.getAllColumnsNameForTable("BIRDS");
        List<String> tableName = metadata.getAllTableNames();

        FileAccessorImpl accessor = new FileAccessorImpl();
        Column column1 = new Column();
        column1.getName();
        TableQuery query = TableQuery.builder()
                .tableName("CENT_DB1")
                .columns(Arrays.asList(column1))
                .tableName("BIRDS")
                .tableOperation(Operation.SELECT)
                .build();
        List<Row> output = accessor.read(query);
    }

    @Test
    public void createDatabase() throws Exception {
        String dbName = "DB2";
        Database database = Database.builder().name(dbName).build();
        Metadata metadata = new Metadata();
        metadata.setDatabase(database);
        MetadataService metadataService = new MetadataServiceImpl();
        metadataService.write(Entity.DATABASE, metadata);
    }

    @Test
    public void createTable() throws Exception {
        String dbName = "DB2";
        String tableName = "STUDENT";
        String INTEGER = "INTEGER";
        String VARCHAR ="VARCHAR";
        Column id = Column.builder().name("ID").dataType(INTEGER).build();
        Column firstName = Column.builder().name("FIRST_NAME").dataType(VARCHAR).build();
        Column lastName = Column.builder().name("LAST_NAME").dataType(VARCHAR).build();
        Column email = Column.builder().name("EMAIL_ID").dataType(VARCHAR).build();
        Database database = Database.builder().name(dbName).build();
        List<Column> columns = Arrays.asList(id, firstName, lastName, email);
        Table table = Table.builder().name(tableName).columns(columns).build();
        database.setTables(Arrays.asList(table));
        Metadata metadata = new Metadata();
        metadata.setDatabase(database);
        MetadataService metadataService = new MetadataServiceImpl();
        metadataService.write(Entity.TABLE, metadata);
    }

    @Test
    public void deleteTable() throws Exception {
        String dbName = "DB2";
        String tableName = "STUDENT";
        String INTEGER = "INTEGER";
        String VARCHAR ="VARCHAR";
        Column id = Column.builder().name("ID").dataType(INTEGER).build();
        Column firstName = Column.builder().name("FIRST_NAME").dataType(VARCHAR).build();
        Column lastName = Column.builder().name("LAST_NAME").dataType(VARCHAR).build();
        Column email = Column.builder().name("EMAIL_ID").dataType(VARCHAR).build();
        Database database = Database.builder().name(dbName).build();
        List<Column> columns = Arrays.asList(id, firstName, lastName, email);
        Table table = Table.builder().name(tableName).columns(columns).build();
        database.setTables(Arrays.asList(table));
        Metadata metadata = new Metadata();
        metadata.setDatabase(database);
        MetadataService metadataService = new MetadataServiceImpl();
        metadataService.delete(Entity.TABLE, metadata);
    }

    @Test
    public void deleteDatabase() throws Exception {
        String dbName = "DB2";
        Database database = Database.builder().name(dbName).build();
        Metadata metadata = new Metadata();
        metadata.setDatabase(database);
        MetadataService metadataService = new MetadataServiceImpl();
        metadataService.delete(Entity.DATABASE, metadata);
    }

}
