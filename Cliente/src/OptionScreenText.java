package src;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTree;
public class OptionScreenText extends JFrame implements ActionListener{
    private JButton btnUpload;
    private JButton btnDownload;
    private JButton btnExibir;
    private JButton remover;
    private JButton btnVoltar;

     private ResourceBundle messages = null;
    private JMenuItem menuItemPortuguese;
    private JMenuItem menuItemEnglish;
    private JMenuItem menuItemSpanish;
    private JMenuItem menuItemFrench;
    private JMenuItem menuItemGerman;

    private JMenu menuLanguage;


    public OptionScreenText(){
        super("Option Screen");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Locale.setDefault(new Locale("pt", "BR"));
        messages = ResourceBundle.getBundle("src/messagestext");

        menuLanguage = new JMenu("Language");

        menuItemPortuguese = new JMenuItem("Português");
        menuItemPortuguese.addActionListener(this);
        menuLanguage.add(menuItemPortuguese);

        menuItemEnglish = new JMenuItem("English");
        menuItemEnglish.addActionListener(this);
        menuLanguage.add(menuItemEnglish);

        menuItemSpanish = new JMenuItem("Español");
        menuItemSpanish.addActionListener(this);
        menuLanguage.add(menuItemSpanish);

        menuItemFrench = new JMenuItem("Français");
        menuItemFrench.addActionListener(this);
        menuLanguage.add(menuItemFrench);

        menuItemGerman = new JMenuItem("Deutsch");
        menuItemGerman.addActionListener(this);
        menuLanguage.add(menuItemGerman);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuLanguage);
        setJMenuBar(menuBar);

        Container pane = getContentPane();
        // colocar em south
        pane.setLayout(new GridLayout(5, 1));

        btnUpload = new JButton("Upload");
        btnUpload.addActionListener(this);
        pane.add(btnUpload);

        btnDownload = new JButton("Download");
        btnDownload.addActionListener(this);
        pane.add(btnDownload);

        btnExibir = new JButton("Exibir");
        btnExibir.addActionListener(this);
        pane.add(btnExibir);

        remover = new JButton("Remover");
        remover.addActionListener(this);
        pane.add(remover);

        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(this);
        pane.add(btnVoltar);



        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnUpload){
            try {
                String name = JOptionPane.showInputDialog("Digite o nome do arquivo");
                JFileChooser fc = new JFileChooser();
                int res = fc.showOpenDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    ClienteBatepapo client = new ClienteBatepapo();
                    client.start("sendtxt:"+name);
                    File arq = fc.getSelectedFile();
                    ServerSocket servsock = new ServerSocket(13267);
                    while (!servsock.isClosed()) {
                    Socket sock = servsock.accept();
                    System.out.println("Conexão aceita: " + sock);
                    // envia o arquivo (transforma em byte array)
                    File myFile = new File(arq.getAbsolutePath());
                    byte[] mybytearray = new byte[(int) myFile.length()];
                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(mybytearray, 0, mybytearray.length);
                    OutputStream os = sock.getOutputStream();
                    System.out.println("Enviando...");
                    System.out.println(mybytearray);
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                    sock.close();
                    servsock.close();
                    setVisible(false);
                    OptionScreenText textScreen = new OptionScreenText();
                }
                }

                
            } catch (Exception ex) {
                System.out.println("Erro ao fechar o socket: " + ex.getMessage());
            }
           
        }
        else if(e.getSource() == btnDownload){
            String filename = JOptionPane.showInputDialog( "Digite o nome do arquivo que deseja baixar");
            ClienteBatepapo client = new ClienteBatepapo();
            try{
                client.start("downloadtxt:"+filename);
            }
            catch(Exception e1){
                e1.printStackTrace();
            }
            int filesize=6022386;
            long start = System.currentTimeMillis();
            int bytesRead;
            int current = 0;
            try{
                Socket sock = new Socket("127.0.0.1",13267);

                // recebendo o arquivo
                byte [] mybytearray  = new byte [filesize];
                
                if(filename == null){
                    JOptionPane.showMessageDialog(null, "Arquivo não encontrado!");
                }
                else if(filename.equals("")){ 
                    JOptionPane.showMessageDialog(null, "Arquivo não encontrado!");
                }
                else{
                    
                InputStream is = sock.getInputStream();
                FileOutputStream fos = new FileOutputStream(filename+".txt");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bytesRead = is.read(mybytearray,0,mybytearray.length);
                current = bytesRead;

                do {
                bytesRead =
                    is.read(mybytearray, current, (mybytearray.length-current));
                if(bytesRead >= 0) current += bytesRead;
                } while(bytesRead > -1);

                bos.write(mybytearray, 0 , current);
                long end = System.currentTimeMillis();
                System.out.println(end-start);
                bos.close();
                sock.close();
                }
                
            }
            catch(Exception e1){
                e1.printStackTrace();
            }
            
            
        }
        else if(e.getSource() == btnExibir){
            ClienteBatepapo client = new ClienteBatepapo();
            try {
                client.start("sendalltxt");
                String tudoconcatenado = client.getMsg();
                // tira oo all txt
                tudoconcatenado = tudoconcatenado.replace("alltxt;", "");
                String[] tudo = tudoconcatenado.split(";");
                // mostrar em uma JTree
                JTree tree = new JTree(tudo);
                JOptionPane.showMessageDialog(null, tree);


                    

                
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            
            
        }
        else if(e.getSource() == remover){
            String arqname = JOptionPane.showInputDialog("Digite o nome do arquivo que deseja remover");
            ClienteBatepapo client = new ClienteBatepapo();
            try {
                client.start("removerTXT:"+arqname);
                if (client.getMsg().equals("true")) {
                    JOptionPane.showMessageDialog(null, "TXT apagado com sucesso!");

                } else {
                    JOptionPane.showMessageDialog(null, "Txt não encontrado!");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            
        }
        else if(e.getSource() == btnVoltar){
            setVisible(false);
            SelectScreen selectScreen = new SelectScreen();
        }
        if(e.getSource() == menuItemPortuguese){
            Locale.setDefault(new Locale("pt", "BR"));
            updateTranslations(Locale.getDefault());
        }
        if(e.getSource() == menuItemEnglish){
            Locale.setDefault(new Locale("en", "US"));
            updateTranslations(Locale.getDefault());
        }
        if(e.getSource() == menuItemSpanish){
            Locale.setDefault(new Locale("es", "ES"));
            updateTranslations(Locale.getDefault());
        }
        if(e.getSource() == menuItemFrench){
            Locale.setDefault(new Locale("fr", "FR"));
            updateTranslations(Locale.getDefault());
        }
        if(e.getSource() == menuItemGerman){
            Locale.setDefault(new Locale("de", "DE"));
            updateTranslations(Locale.getDefault());
        }

    }
    private void updateTranslations(Locale locale) {
        messages = ResourceBundle.getBundle("src.messagestext", locale);
        setTitle(messages.getString("option.screen.title"));
        btnUpload.setText(messages.getString("upload.button"));
        btnDownload.setText(messages.getString("download.button"));
        btnExibir.setText(messages.getString("exibir.button"));
        remover.setText(messages.getString("remover.button"));
        btnVoltar.setText(messages.getString("voltar.button"));
    }
}
