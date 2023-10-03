package org.escola;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.swing.*;
import java.io.File;
import java.sql.Connection;

/**
 * https://www.baeldung.com/hikaricp
 */ //Esta classe gerencia a conexão com o banco de dados usando HikariCP.
public class Database {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource source;
    private static Connection connection;

    //Inicia a conexão com o banco de dados.
    public static void startConnection() {
        try {
            // Verifica se o arquivo do banco de dados existe; se não existir, cria um novo.
            File db = new File("database.db");
            if (!db.exists()) {
                db.createNewFile();
            }
            config.setJdbcUrl("jdbc:sqlite:" + db.getAbsolutePath());
            config.setDriverClassName("org.sqlite.JDBC");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            // Cria e obtém uma fonte de dados HikariCP e uma conexão com o banco de dados.
            source = new HikariDataSource(config);
            connection = source.getConnection();
        } catch (Exception e) {
            e.printStackTrace(); // Se ocorrer um erro, imprime o stack trace.
        }
    }
    //Obtém a conexão com o banco de dados.

    public static Connection getConnection() {
        return connection;
    }
}
