package com.example.services;

import com.example.models.LogContext;
import com.example.models.Metadata;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class LogService {

    private FileWriter fileWriter;
    private String filename;

    public LogService() {
        filename = "storage/logs/general_log.txt";
        createFile();
    }

    public void log(String string) {
        try {
            String log = prefix().concat("Message: ").concat(string).concat("\n");
            fileWriter.write(log);
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println("Error logging");
        }
    }

    @SneakyThrows
    private void createFile() {
        File file = new File(filename);
        file.createNewFile();
        fileWriter = new FileWriter(filename, true);
    }

    public String prefix() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String date = format.format(new Date(System.currentTimeMillis()));
        String prefix = String.format("User: %s, Execution Time: %s, ", LogContext.getUser().getUsername(), date);

        if (Objects.nonNull(LogContext.getMetadata())) {
            Metadata metadata = LogContext.getMetadata();
            if (Objects.nonNull(metadata.getDatabaseName())) {
                String databaseName = metadata.getDatabaseName();
                Integer tableCount = metadata.getAllTablesFromDatabase().size();
                prefix = prefix.concat(String.format("Database (%d): %s, ", tableCount, databaseName));
            }
        }

        if (Objects.nonNull(LogContext.getTable())) {
            String tableName = LogContext.getTable().getName();
            prefix = prefix.concat(String.format("Table: %s, ", tableName));
        }

        return prefix;
    }
}
