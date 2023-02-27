/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.portuary.dao;

import com.portuary.conf.ConfigManager;
import com.portuary.dem.Cliente;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Responsável por gerenciar os dados de cliente no banco de dados
 * @author Portuary
 */
public class Cliente_DAO extends BaseDAO {
    
    // Armazena o nome do banco de dados a ser trabalhado
    private final String dbName = ConfigManager.getInstance().dataBaseConfig.nameDatabase;
    
    /**
     * Insere um novo registro no banco de dados
     * @param cliente Cliente contendo os dados para inserir cliente
     * @return int contendo o id do novo registro inserido
     * @throws java.lang.Exception
     */
    public int Insert(Cliente cliente) throws Exception{
        PreparedStatement pt = null;
        Connection cn = null;
        ResultSet rs = null;
        try {
            String query = ("INSERT INTO tb_cliente ("
                    + "va_cpf_cnpj, va_nome "
                    + ") VALUES(?,?);");
            cn = openConnection(dbName);
            pt = cn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            pt.setString(i++, cliente.getCpfCnpj());
            pt.setString(i++, cliente.getNome());
            pt.executeUpdate();
            rs = pt.getGeneratedKeys();
            rs.next();
            return rs.getInt("id_cliente");
        } catch (Exception e) {
            throw e;
        } finally {
            closeConnection(rs, pt, cn);
        }
    }
}
