
import com.portuary.dao.ConnectionFactory;
import com.portuary.dao.Movimentacao_DAO;
import org.json.JSONObject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Portuary
 */
public class RelatorioMovimentacao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            
            System.out.println((new Movimentacao_DAO()).getRelatorioByMovimentacao(
                    new JSONObject()
                            .put("params", new JSONObject()
                                .put("cliente", "00000000000")
                                .put("container", "ASDF1234567")
                                .put("dataIniciadoDe", "2023-02-01T00:00")
                                .put("dataIniciadoAte", "2023-03-01T00:00")
                            )
            ));
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        // Finaliza a conexão com o banco de dados
        try{ ConnectionFactory.getInstance().stopDatabasePool(); } catch(Exception ex){ /* nada a fazer */ }
    }
}
