
import com.portuary.dao.Cliente_DAO;
import com.portuary.dao.ConnectionFactory;
import com.portuary.dao.Container_DAO;
import com.portuary.dem.Cliente;
import com.portuary.dem.Container;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Portuary
 */
public class InsertContainer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            // Armazena o numero do container a ser incrementado
            long numero = 1234567;
            // Adiciona 10 clientes
            for (int cliente = 1; cliente <= 10; cliente++){
                for (int tipo = 1; tipo <= 2; tipo++){
                    for (int status = 1; status <= 2; status++){
                        for (int categoria = 1; categoria <= 2; categoria++){
                            System.out.println("Iserido: " + Inserir("ASDF"+(numero++),cliente,tipo,status,categoria));
                        }
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        // Finaliza a conexão com o banco de dados
        try{ ConnectionFactory.getInstance().stopDatabasePool(); } catch(Exception ex){ /* nada a fazer */ }
    }
    
    /**
     * Inserir novo registro
     * @param numero String numero do container
     * @param cliente int contendo o id do cliente
     * @param tipo int contendo o id do tipo de container
     * @param status int contendo o id do status de container
     * @param categoria int contendo o id do categoria de container
     * @return int contendo o valor do id do novo registro
     * @throws java.lang.Exception
     */
    public static int Inserir(String numero, int cliente, int tipo, int status, int categoria) throws Exception{
        Container container = new Container();
        container.setNumero(numero);
        container.getCliente().setId(cliente);
        container.getTipo().setId(tipo);
        container.getStatus().setId(status);
        container.getCategoria().setId(categoria);
        return (new Container_DAO()).Insert(container);
    }
}
