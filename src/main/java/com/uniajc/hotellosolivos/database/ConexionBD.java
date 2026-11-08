package com.uniajc.hotellosolivos.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static Connection connection = null;
    public static Connection getConnection() {

        try {
            if (connection == null || connection.isClosed()) {
                Properties properties = new Properties();
                properties.load(
                        new FileInputStream("config.properties")
                );

                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = properties.getProperty("URL");
                String username = properties.getProperty("USERNAME");
                String password = properties.getProperty("PASSWORD");
                connection = DriverManager.getConnection(url, username, password);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver JDBC: "
                            + e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println("Error al conectar a la base de datos: "
                            + e.getMessage()
            );

        } catch (IOException e) {
            System.out.println("Error al cargar config.properties: "
                            + e.getMessage()
            );

        }
        return connection;
    }
}
