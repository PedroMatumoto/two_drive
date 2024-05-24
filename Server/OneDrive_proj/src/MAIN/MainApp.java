/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MAIN;

import java.io.IOException;

/**
 *
 * @author Chambs
 */
public class MainApp {
    public static void main(String[] args) {
        System.out.println("*v*v*v* Servidor de Bate-papo *v*v*v*");
        try {
            ServidorBatepapo server = new ServidorBatepapo();
            server.start();
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: "+e.getMessage());
        }
        System.out.println("Servidor finalizado!");
    }
    
    
}
