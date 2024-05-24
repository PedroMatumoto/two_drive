package src;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;


public class MainApp {
    private static String nome_cliente = null;
    public static void main(String[] args) {
        boolean login = false;

        

        ClienteBatepapo client = new ClienteBatepapo();
        
        // invoca a tela de login
        while(!login){
            String envioLogin = null;
            String envioSenha = null;

            LoginScreen loginScreen = new LoginScreen();

            // enquanto o botao de prosseguir nao for clicado, nao sai do loop
            while(loginScreen.isVisivel()){
                try {
                    Thread.sleep(500); // Aguarda 100 milissegundos antes de verificar novamente
                } catch (InterruptedException e) {
                    // Lidar com exceção, se necessário
                }
            }
            envioLogin = null;
            envioSenha = null;
            // coleta os dados de login e senha
            while(envioLogin==null || envioLogin==null){
                envioLogin = loginScreen.getLogin();
                envioSenha = loginScreen.getPassword();
            }
        
            // envia os dados para o servidor

            
            try {
                client.start("login:"+envioLogin+"-"+envioSenha);
                
                if(client.getMsg().equals("true")){
        
                    JOptionPane.showMessageDialog(null,"Login realizado com sucesso!");
                    login = true;
    
                }
                else{ 
                    JOptionPane.showMessageDialog(null,"Login ou senha incorretos!");
                }
                
            } catch (IOException e) {
                System.out.println("Erro ao iniciar o cliente: "+e.getMessage());
            }

            try {
                client.start("name:"+envioLogin);
                if(client.getMsg()!=null){
                    nome_cliente = client.getMsg();
                    System.out.println("Nome do cliente: "+nome_cliente);
                }
                else{
                    System.out.println("Nome do cliente: "+envioLogin);
                }
            }
            catch(IOException e){
                System.out.println("Erro ao iniciar o cliente: "+e.getMessage());
            }
        }

        

        

        // se a resposta for positiva, invoca a tela de escolha


        SelectScreen selectScreen = new SelectScreen();
        selectScreen.setname(nome_cliente);

    }

   
    
}
