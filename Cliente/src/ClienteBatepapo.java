package src;

import java.io.*;
import java.net.*;

public class ClienteBatepapo{
    public SocketCliente clienteSocket;
    private String msg;

    public ClienteBatepapo() {

    }

    public void start(String msg) throws IOException {
        
            clienteSocket = new SocketCliente(new Socket("localhost", 4000));
           
            send(msg);

            this.msg = getMessage();

            System.out.println("Mensagem recebida: " + this.msg);

            clienteSocket.close();
    }

    


    public void send(String msg) throws IOException {
        clienteSocket.sendMsg(msg);
    }

    public String getMessage() {
        return clienteSocket.getMessage();
    }

    public String getMsg() {
        return msg;
    }

    

}
