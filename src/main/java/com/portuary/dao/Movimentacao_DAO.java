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
import java.sql.Timestamp;
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
     * @param filtro JSONObject contendo o filtro
     * @return Lista de Container contendo os dados do registro solicitado
     * @throws java.lang.Exception
     */
    public JSONObject getRelatorioByMovimentacao(JSONObject filtro) throws Exception {
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
            
            /*
             * Utilizado para criar uma cláusula WHERE para container
             */
            // Armazena os filtros a serem aplicados
            String whereDataIniciadoDe = new String();
            
            if (params.has("dataIniciadoDe") && !params.getString("dataIniciadoDe").isEmpty())
            { whereDataIniciadoDe += "(movimentacao.ts_data_inicio >= ?)"; }
            
            // Verifica se foi adicionado algo para ser filtrado
            if (!whereDataIniciadoDe.isEmpty()) { whereDataIniciadoDe = " AND " + whereDataIniciadoDe; }
            
            /*------------------------------------------------------*/
            
            /*
             * Utilizado para criar uma cláusula WHERE para container
             */
            // Armazena os filtros a serem aplicados
            String whereDataIniciadoAte = new String();
            
            if (params.has("dataIniciadoAte") && !params.getString("dataIniciadoAte").isEmpty())
            { whereDataIniciadoAte += "(movimentacao.ts_data_inicio <= ?)"; }
            
            // Verifica se foi adicionado algo para ser filtrado
            if (!whereDataIniciadoAte.isEmpty()) { whereDataIniciadoAte = " AND " + whereDataIniciadoAte; }
            
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
                    + "                         'id', tipoMov.id_tipo_movimentacao, "
                    + "                         'nome', tipoMov.va_nome, "
                    + "                         'quantidade', (SELECT COUNT(*) FROM tb_movimentacao AS movimentacao "
                    + "                                        INNER JOIN tb_container AS container ON container.id_container = movimentacao.id_container "
                    + "                                        WHERE "
                    + "                                        (container.id_cliente = cliente.id_cliente) "
                    + "                                        AND "
                    + "                                        (movimentacao.id_tipo_movimentacao = tipoMov.id_tipo_movimentacao)"
                    + "                                        "+ whereContainer + whereDataIniciadoDe + whereDataIniciadoAte + ") "
                    + "                     )"
                    + "                 )"
                    + "              FROM tb_tipo_movimentacao AS tipoMov)"
                    + "     )"
                    + ") AS lista "
                    + "FROM tb_cliente AS cliente" + whereCliente;
            
            cn = openConnection(dbName);
            pt = cn.prepareStatement(query);
            
            int i = 1;
            
            if (params.has("container")){ pt.setString(i++, "%" + params.getString("container") + "%"); }
            if (params.has("dataIniciadoDe")){ pt.setTimestamp(i++, Timestamp.valueOf((params.getString("dataIniciadoDe").replace("T"," ")+":00"))); }
            if (params.has("dataIniciadoAte")){ pt.setTimestamp(i++, Timestamp.valueOf((params.getString("dataIniciadoAte").replace("T"," ")+":00"))); }
            
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
}
