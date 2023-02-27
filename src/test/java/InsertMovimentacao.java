
import com.portuary.dao.ConnectionFactory;
import com.portuary.dao.Container_DAO;
import com.portuary.dao.Movimentacao_DAO;
import com.portuary.dem.Container;
import com.portuary.dem.Movimentacao;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Portuary
 */
public class InsertMovimentacao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            // Obtém a lista completa de container
            List<Container> containerList = (new Container_DAO()).getAll();
            // Varre a lista de container
            for (Container container : containerList){
                // Verifica o tipo de categoria e insere o movimento segundo ele
                Inserir(container.getId(), (int)(container.getCategoria().getId().equals(1) ? 1 : 3));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        // Finaliza a conexão com o banco de dados
        try{ ConnectionFactory.getInstance().stopDatabasePool(); } catch(Exception ex){ /* nada a fazer */ }
    }
    
    /**
     * Inserir novo registro
     * @param container int contendo o id do tipo de movimento
     * @param tipo int contendo o id do tipo de container
     * @return int contendo o valor do id do novo registro
     * @throws java.lang.Exception
     */
    public static int Inserir(int container, int tipo) throws Exception{
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.getContainer().setId(container);
        movimentacao.getTipo().setId(tipo);
        return (new Movimentacao_DAO()).Insert(movimentacao);
    }
}
