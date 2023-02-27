/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.portuary.dao;

import com.portuary.conf.ConfigManager;
import com.portuary.dem.Movimentacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Responsável por gerenciar os dados de cliente no banco de dados
 * @author Portuary
 */
public class Movimentacao_DAO extends BaseDAO {
    
    // Armazena o nome do banco de dados a ser trabalhado
    private final String dbName = ConfigManager.getInstance().dataBaseConfig.nameDatabase;
    
    /**
     * Insere um novo registro no banco de dados
     * @param movimentacao Movimentacao contendo os dados para inserir
     * @return int contendo o id do novo registro inserido
     * @throws java.lang.Exception
     */
    public int Insert(Movimentacao movimentacao) throws Exception{
        PreparedStatement pt = null;
        Connection cn = null;
        ResultSet rs = null;
        try {
            String query = ("INSERT INTO tb_movimentacao ("
                    + "id_container, id_tipo_movimentacao"
                    + ") VALUES(?,?);");
            cn = openConnection(dbName);
            pt = cn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            pt.setInt(i++, movimentacao.getContainer().getId());
            pt.setInt(i++, movimentacao.getTipo().getId());
            pt.executeUpdate();
            rs = pt.getGeneratedKeys();
            rs.next();
            return rs.getInt("id_container");
        } catch (Exception e) {
            throw e;
        } finally {
            closeConnection(rs, pt, cn);
        }
    }
    
    
    /**
     * Utilizado para retornar a lista completa
     * @return Lista de Container contendo os dados do registro solicitado
     * @throws java.lang.Exception
     */
    public JSONObject getRelatorioByMovimentacao() throws Exception {
        PreparedStatement pt = null;
        Connection cn = null;
        ResultSet rs = null;
        try {
            String query = new String();
            
            query += 
                      "SELECT"
                    + " json_agg("
                    + "     json_build_object("
                    + "         'id', cliente.id_cliente, "
                    + "         'nome', cliente.va_nome, "
                    + "         'tipo', "
                    + "             (SELECT"
                    + "                 json_agg("
                    + "                     json_build_object("
                    + "                         'id', tipoMov.id_tipo_movimentacao, "
                    + "                         'nome', tipoMov.va_nome, "
                    + "                         'quantidade', (SELECT COUNT(*) FROM tb_movimentacao AS movimentacao "
                    + "                                        INNER JOIN tb_container AS container ON container.id_container = movimentacao.id_container "
                    + "                                        WHERE "
                    + "                                        (container.id_cliente = cliente.id_cliente) "
                    + "                                        AND "
                    + "                                        (movimentacao.id_tipo_movimentacao = tipoMov.id_tipo_movimentacao)) "
                    + "                     )"
                    + "                 )"
                    + "              FROM tb_tipo_movimentacao AS tipoMov)"
                    + "     )"
                    + ") AS lista "
                    + "FROM tb_cliente AS cliente";
            
            cn = openConnection(dbName);
            pt = cn.prepareStatement(query);
            rs = pt.executeQuery();
            if (rs.next()){
                return (new JSONObject()).put("lista", new JSONArray(rs.getString("lista")));
            }else{
                return (new JSONObject().put("lista", new JSONArray()));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            closeConnection(rs, pt, cn);
        }
    }
}
