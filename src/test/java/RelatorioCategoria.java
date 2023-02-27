
import com.portuary.dao.ConnectionFactory;
import com.portuary.dao.Container_DAO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Portuary
 */
public class RelatorioCategoria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            
            System.out.println((new Container_DAO()).getRelatorioByCategoria());
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        // Finaliza a conexão com o banco de dados
        try{ ConnectionFactory.getInstance().stopDatabasePool(); } catch(Exception ex){ /* nada a fazer */ }
    }
}
