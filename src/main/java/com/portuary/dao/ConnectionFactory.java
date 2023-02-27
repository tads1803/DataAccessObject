/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portuary.dao;

/**
 * @author Portuary
 */

import com.portuary.conf.DatabaseConfig;
import com.portuary.conf.ConfigManager;
import com.smartroom.v2.poolfactory.config.DataBaseType;
import com.smartroom.v2.poolfactory.database.DataBase;
import java.sql.Connection;
import java.util.TimeZone;

public class ConnectionFactory {

    private static ConnectionFactory instance;
    private static DataBase dataBase_postgres;

    private final DatabaseConfig conf_postgre = ConfigManager.getInstance().dataBaseConfig;
    
    /**
     * Construtor da classe
     * @exception Exception
     */
    private ConnectionFactory() throws Exception {
        
        // Define o timezone que será trabalhado para universal time
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        
        dataBase_postgres = new DataBase(
                DataBaseType.POSTGRESQL,
                conf_postgre.databaseUrl + ":" + conf_postgre.databasePort,
                conf_postgre.userDatabase,
                conf_postgre.passDatabase,
                conf_postgre.connectionTimeout);
        
        dataBase_postgres.options.setMaximoPoolSize(conf_postgre.maximoPoolSize);
        
        dataBase_postgres.options.setCachePrepStmts(conf_postgre.cachePrepStmts);
        dataBase_postgres.options.setPrepStmtCacheSize(conf_postgre.prepStmtCacheSize);
        dataBase_postgres.options.setPrepStmtCacheSqlLimit(conf_postgre.prepStmtCacheSqlLimit);

        dataBase_postgres.options.setMaxLifeTime(conf_postgre.maxLifeTime);
        dataBase_postgres.options.setIdleTimeout(conf_postgre.idleTimeout);
        dataBase_postgres.options.setConnectionTimeout(conf_postgre.connectionTimeout);
        dataBase_postgres.options.setValidationTimeout(conf_postgre.validationTimeout);
    }

    /**
     * Recupera Conexão
     * @return Connection
     * @throws Exception
     */
    public Connection getConnection() throws Exception {
        return dataBase_postgres.getConnection(conf_postgre.nameDatabase);
    }
    
    /**
     * Retorna uma instância de conexão com o banco solicitado
     * @param databaseName String contendo o nome da base de dados
     * @return Connection com a conexão solicitada
     * @throws java.lang.Exception
     */
    public Connection getConnection(String databaseName) throws Exception {
        return dataBase_postgres.getConnection(databaseName);
    }
    
    /**
     * Retorna Instância de ConnectionFactory
     * @return ConnectionFactory
     * @throws Exception
     */
    public static ConnectionFactory getInstance() throws Exception {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    /**
     * Encerra o pool de conexões com o banco de dados
     */
    public void stopDatabasePool() {
        if (dataBase_postgres != null) { 
            try { dataBase_postgres.finalize(); } 
            catch(Exception trw) { /* Nada a fazer */ System.out.println("Exception ao finalizar pool..."); } 
            catch(Throwable trw) { /* Nada a fazer */ System.out.println("Throwable ao finalizar pool..."); }
        }
    }
}
