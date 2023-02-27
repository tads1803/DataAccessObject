/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portuary.dao;

import java.sql.Connection;

/**
 *
 * @author Potuary
 */
abstract class BaseDAO {

    public BaseDAO() {
    }

    /**
     * Retorna uma conexão com o banco de dados.
     *
     * @param dbName Nome do banco de dados de destino da conexão. Se o
     * parâmetro for null, uma conexão existente no pool de conexões será
     * retornada. Caso seja forncecido o nome de um banco de dados existente no
     * servidor, será retornada uma nova conexão com o referido banco.
     * @return Connection.
     * @throws SmartHomeException
     */
    public Connection openConnection(String dbName) throws Exception {
        try {
            return dbName == null ? ConnectionFactory.getInstance().getConnection() : ConnectionFactory.getInstance().getConnection(dbName);
        } catch (Exception e) {
            throw new Exception("Não foi possível estabelecer a conexão com o database: " + e.getMessage());
        }
    }
    
    public void closeConnection(AutoCloseable... ac1) {
        for (AutoCloseable a : ac1) {
            try {
                a.close();
            } catch (Exception e) {
                /* ignored */
            }
        }
    }
}
