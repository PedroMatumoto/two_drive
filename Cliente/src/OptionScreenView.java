package src;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class OptionScreenView extends JFrame implements ActionListener{
    private JButton btnSair;

    private JButton btnView;


    public OptionScreenView(){
        super("Option Screen");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(2, 1));



        btnSair = new JButton("Sair");
        btnSair.addActionListener(this);
        pane.add(btnSair);

        btnView = new JButton("View");
        btnView.addActionListener(this);
        pane.add(btnView);


        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnSair){
            dispose();
            new SelectScreen();
        }
        else if(e.getSource() == btnView){
            ClienteBatepapo cliente = new ClienteBatepapo();
        try{
            cliente.start("sendallimg");
            String tudoconcatenadoimg = cliente.getMsg();
            // tira oo all txt
            tudoconcatenadoimg = tudoconcatenadoimg.replace("alltxt;", "");
            String[] tudo = tudoconcatenadoimg.split(";");
            cliente.start("sendallwav");
            String tudoconcatenadowav= cliente.getMsg();
            tudoconcatenadowav = tudoconcatenadowav.replace("allwav;", "");
            String[] tudo2 = tudoconcatenadowav.split(";");
            cliente.start("sendalltxt");
            String tudoconcatenadotxt = cliente.getMsg();
            tudoconcatenadotxt = tudoconcatenadotxt.replace("alltxt;", "");
            String[] tudo3 = tudoconcatenadotxt.split(";");

            String[] tudoconcatenado = new String[tudo.length+tudo2.length+tudo3.length];
            int i = 0;
            for(String s : tudo){
                tudoconcatenado[i] = s;
                i++;
            }
            for(String s : tudo2){
                tudoconcatenado[i] = s;
                i++;
            }
            for(String s : tudo3){
                tudoconcatenado[i] = s;
                i++;
            }
            tudo = tudoconcatenado;


            // mostrar em uma JTree
            JTree tree = new JTree(tudo);
            JOptionPane.showMessageDialog(null, tree);


        }
        catch(Exception ex){
            System.out.println("Erro ao enviar mensagem");
        }
        }
        
        

        
    }
}
