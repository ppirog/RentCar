package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instace = null;
    private final String url;
    private final String user;
    private final String password;
    public static DatabaseManager getInstance() {
        if(DatabaseManager.instace==null){
                DatabaseManager.instace = new DatabaseManager();
        }
        return DatabaseManager.instace;
    }
    private DatabaseManager(){
        this.url = "jdbc:postgresql://localhost:54322/postgres";
        this.user = "user";
        this.password = "admin";
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}