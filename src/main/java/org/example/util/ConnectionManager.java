package org.example.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;

@UtilityClass
public final class ConnectionManager {

    private static final String URL = "jdbc:mysql://localhost:3306/top";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    @SneakyThrows
    public static Connection getConnection() {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
