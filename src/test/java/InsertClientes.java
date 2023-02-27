
import com.portuary.dao.Cliente_DAO;
import com.portuary.dao.ConnectionFactory;
import com.portuary.dem.Cliente;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Portuary
 */
public class InsertClientes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            
//            System.out.println("Iserido: " + Inserir("00000000000","Amazonas CA"));
//            System.out.println("Iserido: " + Inserir("11111111111","Spinng INC"));
//            System.out.println("Iserido: " + Inserir("22222222222","SAMSUNGS IND"));
//            System.out.println("Iserido: " + Inserir("33333333333","MACS LTDA"));
//            System.out.println("Iserido: " + Inserir("44444444444","Micro soft LTDA"));
//            System.out.println("Iserido: " + Inserir("55555555555","Ce e CA and GO"));
//            System.out.println("Iserido: " + Inserir("66666666666","Fortnetik INC"));
//            System.out.println("Iserido: " + Inserir("77777777777","Berringera LTDA"));
//            System.out.println("Iserido: " + Inserir("88888888888","Micros Inds CO"));
//            System.out.println("Iserido: " + Inserir("99999999999","Refactor IND e CO"));
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        // Finaliza a conexão com o banco de dados
        try{ ConnectionFactory.getInstance().stopDatabasePool(); } catch(Exception ex){ /* nada a fazer */ }
    }
    
    /**
     * Inserir novo registro
     * @param doc String contendo o CPF ou CNPJ do cliente
     * @param nome String contendo o nome do cliente
     * @return int contendo o valor do id do novo registro
     * @throws java.lang.Exception
     */
    public static int Inserir(String doc, String nome) throws Exception{
        Cliente cliente = new Cliente();
        cliente.setCpfCnpj(doc);
        cliente.setNome(nome);
        return (new Cliente_DAO()).Insert(cliente);
    }
}
