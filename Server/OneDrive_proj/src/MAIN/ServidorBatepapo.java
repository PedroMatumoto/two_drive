package MAIN;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import DAO.*;
import DTO.*;


public class ServidorBatepapo {
    public static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 4000;
    private ServerSocket serverSocket;
    private final List<SocketCliente> clientes=new LinkedList<>();
    private CommandsDB db = new CommandsDB();
    private TextManipulation txt = new TextManipulation();
    LocalDateTime myObj = LocalDateTime.now();

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciado na porta: " + PORT);
        clientConnectionLoop();
    }

    private void clientConnectionLoop() throws IOException {
        System.out.println("Aguardando conexões...");
        while (true) {
            SocketCliente clienteSocket = new SocketCliente(serverSocket.accept());
            clientes.add(clienteSocket);
            new Thread(() -> clientMessageLoop(clienteSocket)).start();
        }
    }

    private void clientMessageLoop(SocketCliente clienteSocket) {
        String msg;
        TextManipulation txt = new TextManipulation();
         
            // escrever todos os arquivos antes de iniciar o servidor
            Connection starting = null;
            ConnFactory fac = new ConnFactory();
            starting = fac.getConn();
            String lista = db.sendAllTxt(starting);
            lista = lista.replace("alltxt;","");
            txt.openFile("OneDrive_proj/src/MAIN/doclogs/listatxt"+myObj.getYear()+"_"+myObj.getDayOfYear()+"_"+myObj.getHour()+"_"+myObj.getMinute()+"_"+myObj.getSecond()+".txt");
            txt.addRecords(lista);
            txt.closeFile();
            lista = db.sendAllImg(starting);
            lista = lista.replace("allimg;", "");
            txt.openFile("OneDrive_proj/src/MAIN/doclogs/listaImg"+myObj.getYear()+"_"+myObj.getDayOfYear()+"_"+myObj.getHour()+"_"+myObj.getMinute()+"_"+myObj.getSecond()+".txt");
            txt.addRecords(lista);
            txt.closeFile();
            lista = db.sendAllWav(starting);
            lista = lista.replace("allwav;", "");
            txt.openFile("OneDrive_proj/src/MAIN/doclogs/listaWav"+myObj.getYear()+"_"+myObj.getDayOfYear()+"_"+myObj.getHour()+"_"+myObj.getMinute()+"_"+myObj.getSecond()+".txt");
            txt.addRecords(lista);
            txt.closeFile();
        try
        {
            

            while((msg=clienteSocket.getMessage())!=null)
            {
                System.out.println("Mensagem recebida de "+clienteSocket.getRemoteSocketAddress()+": "+msg);

                if(msg.contains("login:")){
                    msg = msg.replace("login:", "");
                    verifyOnDB(clienteSocket, msg);
                }

                else if(msg.contains("name:")){
                    msg = msg.replace("name:", "");
                    sendName(clienteSocket, msg);
                }

                else if(msg.contains("removerIMG:")){
                    msg = msg.replace("removerIMG:", "");
                    DelImg(clienteSocket, msg);
                }
                else if(msg.contains("removerWAV:")){
                    msg = msg.replace("removerWAV:", "");
                    DelWav(clienteSocket, msg);
                }
                else if(msg.contains("removerTXT:")){
                    msg = msg.replace("removerTXT:", "");
                    DelTxt(clienteSocket, msg);
                }
                else if(msg.contains("sendalltxt")){
                    mandartudotexto(clienteSocket);  
                }
                else if(msg.contains("sendallimg")){
                    mandartudoimagem(clienteSocket);
                }
                else if(msg.contains("sendallwav")){
                    mandartudoaudio(clienteSocket);
                }
                else if(msg.contains("sendwav:")){
                    msg = msg.replace("sendwav:", "");
                    String name = msg;
                    
                    try{serverSocket.close();clienteSocket.sendMsg("true");}
                    catch(Exception e){}
                    int filesize=6022386;

                    int bytesRead;
                    int current = 0;
                    try{
                        try{
                            Thread.sleep(1000);
                        }
                        catch(Exception e){}
                        Socket sock = new Socket("127.0.0.1",13267);

                        // recebendo o arquivo
                        byte [] mybytearray  = new byte [filesize];
                        InputStream is = sock.getInputStream();
                        FileOutputStream fos = new FileOutputStream( name+".wav");
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        System.out.println(bytesRead);
                        current = bytesRead;
                        

                        do {
                        bytesRead =
                            is.read(mybytearray, current, (mybytearray.length-current));
                        if(bytesRead >= 0) current += bytesRead;
                        } while(bytesRead > -1);

                        System.out.println(mybytearray);
                        bos.write(mybytearray, 0 , current);
                        bos.close();
                        sock.close();
                        Connection con = null;        
                        ConnFactory conn = new ConnFactory();
                        con = conn.getConn();
                        try{
                            db.uploadAudio(con, name, name+".wav");
                        }
                        catch(Exception e){
                            System.out.println("Erro: "+e.getMessage());
                        }
                        System.out.println("*v*v*v* Servidor de Bate-papo *v*v*v*");
                        try {
                            ServidorBatepapo server = new ServidorBatepapo();
                            server.start();
                        } catch (IOException e) {
                            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
                        }
                        System.out.println("Servidor finalizado!");
                        
                    }
                    catch(Exception e){
                        System.out.println("Erro: "+e.getMessage());
                    }

                    
                }
                else if(msg.contains("sendimg:")){
                    msg = msg.replace("sendimg:", "");
                    String name = msg;
                    
                    try{clienteSocket.sendMsg("true");serverSocket.close();}
                    catch(Exception e){}
                    int filesize=6022386;

                    int bytesRead;
                    int current = 0;
                    try{
                        try{
                            Thread.sleep(1000);
                        }
                        catch(Exception e){}
                        Socket sock = new Socket("127.0.0.1",13267);

                        // recebendo o arquivo
                        byte [] mybytearray  = new byte [filesize];
                        InputStream is = sock.getInputStream();
                        FileOutputStream fos = new FileOutputStream( name+".png");
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        System.out.println(bytesRead);
                        current = bytesRead;
                        

                        do {
                        bytesRead =
                            is.read(mybytearray, current, (mybytearray.length-current));
                        if(bytesRead >= 0) current += bytesRead;
                        } while(bytesRead > -1);

                        System.out.println(mybytearray);
                        bos.write(mybytearray, 0 , current);
                        bos.close();
                        sock.close();
                        Connection con = null;        
                        ConnFactory conn = new ConnFactory();
                        con = conn.getConn();
                        try{
                            db.uploadImg(con, name, name+".png");
                        }
                        catch(Exception e){
                            System.out.println("Erro: "+e.getMessage());
                        }
                        System.out.println("*v*v*v* Servidor de Bate-papo *v*v*v*");
                        try {
                            ServidorBatepapo server = new ServidorBatepapo();
                            server.start();
                        } catch (IOException e) {
                            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
                        }
                        System.out.println("Servidor finalizado!");
                        
                    }
                    catch(Exception e){
                        System.out.println("Erro: "+e.getMessage());
                    }
                    
                }
                else if(msg.contains("sendtxt:")){
                    msg = msg.replace("sendtxt:", "");
                    String name = msg;
                    
                    try{serverSocket.close();clienteSocket.sendMsg("true");}
                    catch(Exception e){}
                    int filesize=6022386;

                    int bytesRead;
                    int current = 0;
                    try{
                        try{
                            Thread.sleep(1000);
                        }
                        catch(Exception e){}
                        Socket sock = new Socket("127.0.0.1",13267);

                        // recebendo o arquivo
                        byte [] mybytearray  = new byte [filesize];
                        InputStream is = sock.getInputStream();
                        FileOutputStream fos = new FileOutputStream( name+".txt");
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        System.out.println(bytesRead);
                        current = bytesRead;
                        

                        do {
                        bytesRead =
                            is.read(mybytearray, current, (mybytearray.length-current));
                        if(bytesRead >= 0) current += bytesRead;
                        } while(bytesRead > -1);

                        System.out.println(mybytearray);
                        bos.write(mybytearray, 0 , current);
                        bos.close();
                        sock.close();
                        Connection con = null;        
                        ConnFactory conn = new ConnFactory();
                        con = conn.getConn();
                        try{
                            db.uploadTxt(con, name, name+".txt");
                        }
                        catch(Exception e){
                            System.out.println("Erro: "+e.getMessage());
                        }
                        System.out.println("*v*v*v* Servidor de Bate-papo *v*v*v*");
                        try {
                            ServidorBatepapo server = new ServidorBatepapo();
                            server.start();
                        } catch (IOException e) {
                            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
                        }
                        System.out.println("Servidor finalizado!");
                        
                    }
                    catch(Exception e){
                        System.out.println("Erro: "+e.getMessage());
                    }
                    
                }
                else if(msg.contains("downloadtxt:")){
                    msg = msg.replace("downloadtxt:", "");
                    String name = msg;
                    if(name==null){

                    }
                    else if(name.equals(""))
                    {

                    }
                    else{
                        Connection con = null;        
                    ConnFactory conn = new ConnFactory();
                    con = conn.getConn();
                    clienteSocket.sendMsg("true");
                    db.downloadTxt(con, name);
                    
                    try{
                        
                         ServerSocket servsock = new ServerSocket(13267);
                    Socket sock = servsock.accept();
                    System.out.println("Conexão aceita: " + sock);

                    // envia o arquivo (transforma em byte array)
                    File myFile = new File (name+".txt");
                    byte [] mybytearray  = new byte [(int)myFile.length()];
                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    OutputStream os = sock.getOutputStream();
                    System.out.println("Enviando...");
                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();
                    sock.close();
                    System.out.println("*v*v*v* Servidor de Bate-papo *v*v*v*");
                        try {
                            ServidorBatepapo server = new ServidorBatepapo();
                            server.start();
                        } catch (IOException e) {
                            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
                        }
                        System.out.println("Servidor finalizado!");

                    }
                    catch(Exception e){
                        System.out.println("Erro: "+e.getMessage());
                    }
                    }
                    
                }
                else if(msg.contains("downloadimg:")){
                    msg = msg.replace("downloadimg:", "");
                    String name = msg;
                    Connection con = null;
                    ConnFactory conn = new ConnFactory();
                    con = conn.getConn();
                    db.downloadImg(con, name);
                    clienteSocket.sendMsg("true");
                    
                    
                    try{
                         ServerSocket servsock = new ServerSocket(13267);
                    Socket sock = servsock.accept();
                    System.out.println("Conexão aceita: " + sock);

                    // envia o arquivo (transforma em byte array)
                    File myFile = new File (name+".png");
                    byte [] mybytearray  = new byte [(int)myFile.length()];
                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    OutputStream os = sock.getOutputStream();
                    System.out.println("Enviando...");
                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();
                    sock.close();
                    System.out.println("*v*v*v* Servidor de Bate-papo *v*v*v*");
                        try {
                            ServidorBatepapo server = new ServidorBatepapo();
                            server.start();
                        } catch (IOException e) {
                            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
                        }
                        System.out.println("Servidor finalizado!");

                    }
                    catch(Exception e){
                        System.out.println("Erro: "+e.getMessage());
                    }
                   
                    
    
    
                }
                else if(msg.contains("downloadwav:")){
                    msg = msg.replace("downloadwav:", "");
                    String name = msg;
                    Connection con = null;
                    ConnFactory conn = new ConnFactory();
                    con = conn.getConn();
                    db.downloadAudio(con, name);
                    clienteSocket.sendMsg("true");
                    
                    
                    try{
                         ServerSocket servsock = new ServerSocket(13267);
                    Socket sock = servsock.accept();
                    System.out.println("Conexão aceita: " + sock);

                    // envia o arquivo (transforma em byte array)
                    File myFile = new File (name+".wav");
                    byte [] mybytearray  = new byte [(int)myFile.length()];
                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    OutputStream os = sock.getOutputStream();
                    System.out.println("Enviando...");
                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();
                    sock.close();
                    System.out.println("*v*v*v* Servidor de Bate-papo *v*v*v*");
                        try {
                            ServidorBatepapo server = new ServidorBatepapo();
                            server.start();
                        } catch (IOException e) {
                            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
                        }
                        System.out.println("Servidor finalizado!");

                    }
                    catch(Exception e){
                        System.out.println("Erro: "+e.getMessage());
                    }
                    
                }
                else if(msg.contains("cadastro:")){
                    msg = msg.replace("cadastro:", "");
                    String[] log_senha = msg.split("-");
                    Connection con = null;        
                    ConnFactory conn = new ConnFactory();
                    con = conn.getConn();
                    String email = db.emailVerify(con, log_senha[0]);
                    if(email==null){
                        String sMsgClara = log_senha[1];
                        String sMsgCifrada = null;
                        String sMsgDecifrada = null;
                        byte[] bMsgClara = null;
                        byte[] bMsgCifrada = null;
                        byte[] bMsgDecifrada = null;
                        // Instancia objeto da classe Impressora
                        Impressora prn = new Impressora(); 
                        System.out.println(">>> Cifrando com o algoritmo AES...");
                        System.out.println("");
                        // Instancia um objeto da classe CryptoAES
                        CryptoAES caes = new CryptoAES();
                        try{
                        // Converte a String em um vetor de bytes
                        bMsgClara = sMsgClara.getBytes("ISO-8859-1");
                            // Gera a Chave criptografica AES simetrica e o nome do arquivo onde será
                        // armazenada
                        // Gera a cifra AES da mensagem dada, com a chave simetrica dada
                        caes.geraCifra(bMsgClara, new File("chave.simetrica"));
                        // Recebe o texto cifrado
                        bMsgCifrada = caes.getTextoCifrado();
                        // Converte o texto byte[] no equivalente String
                        sMsgCifrada = (new String(bMsgCifrada, "ISO-8859-1"));
                        // Imprime cabecalho da mensagem
                        System.out.println("Mensagem Cifrada (Hexadecimal):");
                        // Imprime o texto cifrado em Hexadecimal
                        System.out.println(prn.hexBytesToString(bMsgCifrada));
                        System.out.println("Em String: "+sMsgCifrada);
                        }
                        catch(Exception e){
                            System.out.println("Erro: "+e.getMessage());
                        }
                        db.insertUser(con, log_senha[2], log_senha[0],sMsgCifrada);
                        clienteSocket.sendMsg("true");
                    }
                    else{
                        clienteSocket.sendMsg("false");
                    }
                }

               
            }
        }
        finally{
            clienteSocket.close();
        }
    }

    private void verifyOnDB(SocketCliente sender, String msg) {
        // tratar a mensagem recebida (separando o login e a senha)
        String[] log_senha = msg.split("-");
        System.out.println(log_senha[0]);
        System.out.println(log_senha[1]);
        String sMsgClara = log_senha[1];
        String sMsgCifrada = null;
        String sMsgDecifrada = null;
        byte[] bMsgClara = null;
        byte[] bMsgCifrada = null;
        byte[] bMsgDecifrada = null;
        // Instancia objeto da classe Impressora
        Impressora prn = new Impressora(); 
        System.out.println(">>> Cifrando com o algoritmo AES...");
        System.out.println("");
        // Instancia um objeto da classe CryptoAES
        CryptoAES caes = new CryptoAES();
        try{
        // Converte a String em um vetor de bytes
        bMsgClara = sMsgClara.getBytes("ISO-8859-1");
             // Gera a Chave criptografica AES simetrica e o nome do arquivo onde será
        // armazenada
        // Gera a cifra AES da mensagem dada, com a chave simetrica dada
        caes.geraCifra(bMsgClara, new File("chave.simetrica"));
        // Recebe o texto cifrado
        bMsgCifrada = caes.getTextoCifrado();
        // Converte o texto byte[] no equivalente String
        sMsgCifrada = (new String(bMsgCifrada, "ISO-8859-1"));
        // Imprime cabecalho da mensagem
        System.out.println("Mensagem Cifrada (Hexadecimal):");
        // Imprime o texto cifrado em Hexadecimal
        System.out.println(prn.hexBytesToString(bMsgCifrada));
        System.out.println("Em String: "+sMsgCifrada);
        }
        catch(Exception e){
            System.out.println("Erro: "+e.getMessage());
        }
       
        
        Connection con = null;        
        ConnFactory conn = new ConnFactory();
        con = conn.getConn();
        String email = db.emailVerify(con, log_senha[0]);
        System.out.println("E-mail: "+email);
        if(email != null){
            String senha = db.passVerify(con, log_senha[0]);
            System.out.println("Senha: "+senha);
            senha = senha.replace(" ", "");
            sMsgCifrada = sMsgCifrada.replace(" ", "");

            if(senha!=null){
                if(sMsgCifrada.equals(senha)){
                    sender.sendMsg("true");
                    System.out.println("Login realizado com sucesso!");
                }
                else{
                    sender.sendMsg("false");
                    System.out.println("Senha incorreta!");
                }
            }
            else{
                sender.sendMsg("false");
                System.out.println("Senha incorreta!");
            }
               
        }
        else{
            sender.sendMsg("false");
            System.out.println("E-mail incorreto!");
        }
    }

    private void sendName(SocketCliente sender, String msg) {
        Connection con = null;        
        ConnFactory conn = new ConnFactory();
        con = conn.getConn();
        String name = db.getName(con, msg);
        sender.sendMsg(name);
    }

    private void DelImg(SocketCliente sender, String msg) {
        Connection con = null;        
        ConnFactory conn = new ConnFactory();
        con = conn.getConn();
        db.deleteImg(con, msg);
        sender.sendMsg("true");
    }

    private void DelWav(SocketCliente sender, String msg) {
        Connection con = null;        
        ConnFactory conn = new ConnFactory();
        con = conn.getConn();
        db.deleteAudio(con, msg);
        sender.sendMsg("true");
    }

    private void DelTxt(SocketCliente sender, String msg) {
        System.out.println(msg);
        Connection con = null;        
        ConnFactory conn = new ConnFactory();
        con = conn.getConn();
        db.deleteTxt(con, msg);
        sender.sendMsg("true");
    }

    private void mandartudotexto(SocketCliente sender) {
        String lista;
        Connection con = null;        
        ConnFactory conn = new ConnFactory();
        con = conn.getConn();
        lista = db.sendAllTxt(con);
        sender.sendMsg(lista);
    }

    private void mandartudoimagem(SocketCliente sender) {
        String lista;
        Connection con = null;        
        ConnFactory conn = new ConnFactory();
        con = conn.getConn();
        lista = db.sendAllImg(con);
        sender.sendMsg(lista);
    }

    private void mandartudoaudio(SocketCliente sender) {
        String lista;
        Connection con = null;        
        ConnFactory conn = new ConnFactory();
        con = conn.getConn();
        lista = db.sendAllWav(con);
        sender.sendMsg(lista);
    }

   

    

   


    
    
}
    