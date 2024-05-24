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
import javax.swing.JOptionPane;
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

public class RegisterScreen extends JFrame implements ActionListener{
    
        private JButton btnRegister;
        private JButton btnExit;
    
        private JLabel name;
        private JLabel lblLogin;
        private JLabel lblPassword;
        private JLabel lblConfirmPassword;

    
        private JTextField txtLogin;
        private JTextField txtName;
        private JPasswordField txtPassword;
        private JPasswordField txtConfirmPassword;
    
    
        public RegisterScreen(){
            super("Register Screen");
    
            setSize(400, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            Container pane = getContentPane();
            pane.setLayout(new GridLayout(5, 2));

            name = new JLabel("Nome");
            pane.add(name);

            txtName = new JTextField();
            pane.add(txtName);
    
            lblLogin = new JLabel("Email");
            pane.add(lblLogin);
    
            txtLogin = new JTextField();
            pane.add(txtLogin);
    
            lblPassword = new JLabel("Password");
            pane.add(lblPassword);
    
            txtPassword = new JPasswordField();
            pane.add(txtPassword);
    
            lblConfirmPassword = new JLabel("Confirm Password");
            pane.add(lblConfirmPassword);
    
            txtConfirmPassword = new JPasswordField();
            pane.add(txtConfirmPassword);
    
            btnRegister = new JButton("Register");
            btnRegister.addActionListener(this);
            pane.add(btnRegister);
    
            btnExit = new JButton("Exit");
            btnExit.addActionListener(this);
            pane.add(btnExit);
    
    
            setVisible(true);
        }
    
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == btnRegister){
                if(txtPassword.getText().equals(txtConfirmPassword.getText())){
                    ClienteBatepapo cliente = new ClienteBatepapo();
                    try{
                        cliente.start("cadastro:"+txtLogin.getText()+"-"+txtPassword.getText()+"-"+txtName.getText());
                    if (cliente.getMsg().equals("true")) {
                        JOptionPane.showMessageDialog(null, "Cadastro feito!");
                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Cadastro não deu certo!");
                    }
                    }
                    catch(Exception ex){
                        JOptionPane.showMessageDialog(null, "Cadastro não deu certo!");
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Senhas não conferem!");
                }
                
            }else if(e.getSource() == btnExit){
                System.exit(0);
            }
        }
    
        
    
}
