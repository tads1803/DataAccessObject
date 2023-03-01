/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.portuary.dao;

import com.portuary.conf.ConfigManager;
import com.portuary.dem.Container;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Responsável por gerenciar os dados de cliente no banco de dados
 * @author Portuary
 */
public class Container_DAO extends BaseDAO {
    
    // Armazena o nome do banco de dados a ser trabalhado
    private final String dbName = ConfigManager.getInstance().dataBaseConfig.nameDatabase;
    
    /**
     * Insere um novo registro no banco de dados
     * @param container Container contendo os dados para inserir
     * @return int contendo o id do novo registro inserido
     * @throws java.lang.Exception
     */
    public int Insert(Container container) throws Exception{
        PreparedStatement pt = null;
        Connection cn = null;
        ResultSet rs = null;
        try {
            String query = ("INSERT INTO tb_container ("
                    + "va_numero, id_cliente, "
                    + "id_tipo_container, id_status_container, id_categoria_container"
                    + ") VALUES(?,?,?,?,?);");
            cn = openConnection(dbName);
            pt = cn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            pt.setString(i++, container.getNumero());
            pt.setInt(i++, container.getCliente().getId());
            pt.setInt(i++, container.getTipo().getId());
            pt.setInt(i++, container.getStatus().getId());
            pt.setInt(i++, container.getCategoria().getId());
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
    public List<Container> getAll() throws Exception {
        PreparedStatement pt = null;
        Connection cn = null;
        ResultSet rs = null;
        try {
            String query = ("SELECT * FROM tb_container");
            cn = openConnection(dbName);
            pt = cn.prepareStatement(query);
            rs = pt.executeQuery();
            return rsToList(rs);
        } catch (Exception e) {
            throw e;
        } finally {
            closeConnection(rs, pt, cn);
        }
    }
    
    /**
     * Utilizado para retornar a lista completa
     * @param filtro JSONObject contendo o filtro
     * @return Lista de Container contendo os dados do registro solicitado
     * @throws java.lang.Exception
     */
    public JSONObject getRelatorioByCategoria(JSONObject filtro) throws Exception {
        PreparedStatement pt = null;
        Connection cn = null;
        ResultSet rs = null;
        try {
            
            JSONObject params = (filtro.has("params") ? filtro.getJSONObject("params") : new JSONObject());
            
            /*
             * Utilizado para criar uma cláusula WHERE para cliente
             */
            // Armazena os filtros a serem aplicados
            String whereCliente = new String();
            
            if (params.has("cliente")){ whereCliente += "(cliente.va_cpf_cnpj LIKE ?)"; }
            
            // Verifica se foi adicionado algo para ser filtrado
            if (!whereCliente.isEmpty()) { whereCliente = " WHERE " + whereCliente; }
            
            /*------------------------------------------------------*/
            
            /*
             * Utilizado para criar uma cláusula WHERE para container
             */
            // Armazena os filtros a serem aplicados
            String whereContainer = new String();
            
            if (params.has("container")){ whereContainer += "(container.va_numero LIKE ?)"; }
            
            // Verifica se foi adicionado algo para ser filtrado
            if (!whereContainer.isEmpty()) { whereContainer = " AND " + whereContainer; }
            
            /*------------------------------------------------------*/
            
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
                    + "                         'id', categoria.id_categoria_container, "
                    + "                         'nome', categoria.va_nome, "
                    + "                         'quantidade', (SELECT COUNT(*) FROM tb_container AS container "
                    + "                                        WHERE "
                    + "                                        (container.id_cliente = cliente.id_cliente) "
                    + "                                        AND "
                    + "                                        (container.id_categoria_container = categoria.id_categoria_container)"
                    + "                                        "+ whereContainer +") "
                    + "                     )"
                    + "                 )"
                    + "              FROM tb_categoria_container AS categoria)"
                    + "     )"
                    + ") AS lista "
                    + "FROM tb_cliente AS cliente " + whereCliente;
            
            cn = openConnection(dbName);
            pt = cn.prepareStatement(query);
            
            int i = 1;
            
            if (params.has("container")){ pt.setString(i++, "%" + params.getString("container") + "%"); }
            if (params.has("cliente")){ pt.setString(i++, "%" + params.getString("cliente") + "%"); }
            
            System.out.println(pt);
            
            rs = pt.executeQuery();
            if (rs.next()){
                String lista = rs.getString("lista");
                if (lista != null)
                    return (new JSONObject()).put("lista", new JSONArray(lista));
            }
            
            return (new JSONObject().put("lista", new JSONArray()));
        } catch (Exception e) {
            throw e;
        } finally {
            closeConnection(rs, pt, cn);
        }
    }
    
    /**
     * Procedimento responsável por converter a lista retornada no resultset
     * @param rs ResultSet contendo o retorno da consulta
     * @return List<Cliente> contendo a lista de registros retornados pela consulta
     */
    private List<Container> rsToList(ResultSet rs) throws Exception {
        List<Container> list = new ArrayList<>();
        Container dados;
        while (rs.next()) {
            
            dados = new Container();
            dados.setId(rs.getInt("id_container"));
            dados.getCliente().setId(rs.getInt("id_cliente"));
            dados.getTipo().setId(rs.getInt("id_tipo_container"));
            dados.getStatus().setId(rs.getInt("id_status_container"));
            dados.getCategoria().setId(rs.getInt("id_categoria_container"));
            dados.setDataCadastro(rs.getTimestamp("ts_data_cadastro"));
            dados.setDataAlteracao(rs.getTimestamp("ts_data_alteracao"));
            
            list.add(dados);
        }
        return list;
    }
}
