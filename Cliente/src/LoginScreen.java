package src;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame implements ActionListener{

    private ResourceBundle messages = null;

    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnExit;

    private JLabel lblLogin;
    private JLabel lblPassword;

    private JTextField txtLogin;
    private JPasswordField txtPassword;

    private JLabel lblImage;

    private boolean visivel = true;

    private JMenu menuLanguage;
    private JMenuItem menuItemPortuguese;
    private JMenuItem menuItemEnglish;
    private JMenuItem menuItemSpanish;
    private JMenuItem menuItemFrench;
    private JMenuItem menuItemGerman;

    public LoginScreen(){
        super("Login Screen");

        Locale.setDefault(new Locale("pt", "BR"));
        messages = ResourceBundle.getBundle("src/messages");
        menuLanguage = new JMenu("Language");

        // Adicione itens de menu para cada idioma
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

        setSize(500, 500);  // Ajusta o tamanho conforme necessário
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new FlowLayout());
        JPanel southPanel = new JPanel(new FlowLayout());
        JPanel centerPanel = new JPanel(new GridLayout(4, 1));
        // Adiciona a imagem ao norte
        lblImage = new JLabel();
        setImageIcon("logo.png");
        northPanel.add(lblImage);

        // Adiciona os componentes ao sul
        
        
        lblLogin = new JLabel("Login: ");
        centerPanel.add(lblLogin);

        txtLogin = new JTextField(20);
        centerPanel.add(txtLogin);

        lblPassword = new JLabel("Password: ");
        centerPanel.add(lblPassword);

        txtPassword = new JPasswordField(20);
        centerPanel.add(txtPassword);

        
        

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(this);
        southPanel.add(btnLogin);

        btnRegister = new JButton("Register");
        btnRegister.addActionListener(this);
        southPanel.add(btnRegister);

        btnExit = new JButton("Exit");
        btnExit.addActionListener(this);
        southPanel.add(btnExit);

        pane.add(northPanel, BorderLayout.NORTH);
        pane.add(centerPanel, BorderLayout.CENTER);
        pane.add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnLogin){
            setVisible(false);
            visivel = false;
        }
        else if(e.getSource() == btnRegister){
            RegisterScreen registerScreen = new RegisterScreen();
            registerScreen.setVisible(true);
        }
        else if(e.getSource() == btnExit){
           System.exit(0);
        }
        else if(e.getSource() == menuItemPortuguese){
            Locale.setDefault(new Locale("pt", "BR"));
            updateTranslations(Locale.getDefault());
        }
        else if(e.getSource() == menuItemEnglish){
            Locale.setDefault(Locale.US);
            updateTranslations(Locale.getDefault());
        }
        else if(e.getSource() == menuItemSpanish){
            Locale.setDefault(Locale.forLanguageTag("es-ES"));
            updateTranslations(Locale.getDefault());
        }
        else if(e.getSource() == menuItemFrench){
            Locale.setDefault(Locale.FRANCE);
            updateTranslations(Locale.getDefault());
        }
        else if(e.getSource() == menuItemGerman){
            Locale.setDefault(Locale.GERMANY);
            updateTranslations(Locale.getDefault());
        }
    }

    public String getLogin(){
        return txtLogin.getText();
    }

    public String getPassword(){
        return txtPassword.getText();
    }

    public boolean isVisivel(){
        return this.isVisible();
    }

    public void setImageIcon(String imagePath) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        lblImage.setIcon(imageIcon);
    }
    
    private void updateTranslations(Locale locale) {
        try {
        messages = ResourceBundle.getBundle("src.messages", locale);
        lblLogin.setText(messages.getString("login.label"));
        lblPassword.setText(messages.getString("password.label"));
        btnLogin.setText(messages.getString("login.button"));
        btnRegister.setText(messages.getString("register.button"));
        btnExit.setText(messages.getString("exit.button"));
    } catch (MissingResourceException e) {
        e.printStackTrace();
        // Lidar com a exceção (por exemplo, exibir uma mensagem de erro)
    }
    }

    
    
}
