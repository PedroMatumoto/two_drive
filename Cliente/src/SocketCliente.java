package src;

import java.io.*;
import java.net.*;

public class SocketCliente {
    public final Socket socket;
    public final BufferedReader entrada;
    public final PrintWriter saida;

    public SocketCliente(final Socket socket) throws IOException {
        this.socket = socket;
        System.out.println("Conectado com: " + socket.getRemoteSocketAddress()+"!");
        this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.saida = new PrintWriter(socket.getOutputStream(), true);
    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    public void close(){
        try {
            entrada.close();
            saida.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao fechar o socket: "+ e.getMessage());
        }
    }

    public String getMessage(){
        try {
            return entrada.readLine();
        } catch (IOException e) {
            return null;
        }
    }


    public boolean sendMsg(String msg){
        saida.println(msg);
        return !saida.checkError();
    }

    
    
    
}
