package src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectScreen extends JFrame implements ActionListener{
    private JButton btnText;
    private JButton btnAudio;
    private JButton btnImg;
    private JButton btnView;


    private JLabel lblWelcome;

    private JLabel lblImage;

    private ResourceBundle messages = null;
    private JMenuItem menuItemPortuguese;
    private JMenuItem menuItemEnglish;
    private JMenuItem menuItemSpanish;
    private JMenuItem menuItemFrench;
    private JMenuItem menuItemGerman;

    private JMenu menuLanguage;

    public SelectScreen(){
        super("Option Screen");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Locale.setDefault(new Locale("pt", "BR"));
        messages = ResourceBundle.getBundle("src/messagesselect");

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
        pane.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());
        JPanel southPanel = new JPanel(new GridLayout(4, 1));

        lblImage = new JLabel();
        setImageIcon("logopequeno.png");
        northPanel.add(lblImage, BorderLayout.WEST);

        lblWelcome = new JLabel("Welcome to TwoDrive");
        lblWelcome.setHorizontalAlignment(JLabel.CENTER);
        northPanel.add(lblWelcome, BorderLayout.CENTER);

        btnText = new JButton("Text");
        btnText.addActionListener(this);
        southPanel.add(btnText);

        btnAudio = new JButton("Audio");
        btnAudio.addActionListener(this);
        southPanel.add(btnAudio);

        btnImg = new JButton("Image");
        btnImg.addActionListener(this);
        southPanel.add(btnImg);

        btnView = new JButton("View");
        btnView.addActionListener(this);
        southPanel.add(btnView);

        pane.add(northPanel, BorderLayout.CENTER);
        pane.add(southPanel, BorderLayout.SOUTH);



        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnText){
            OptionScreenText textScreen = new OptionScreenText();
            setVisible(false);
        }
        if(e.getSource() == btnAudio){
            OptionScreenAudio audioScreen = new OptionScreenAudio();
            setVisible(false);
        }
        if(e.getSource() == btnImg){
            OptionScreenImg imgScreen = new OptionScreenImg();
            setVisible(false);
        }
        if(e.getSource() == btnView){
            OptionScreenView viewScreen = new OptionScreenView();
            setVisible(false);
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

    public void setname(String name) {
        lblWelcome.setText("Welcome to TwoDrive, " + name);
    }

     public void setImageIcon(String imagePath) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        lblImage.setIcon(imageIcon);
    }

    private void updateTranslations(Locale locale) {
        try{
            messages = ResourceBundle.getBundle("src.messagesselect", locale);
        lblWelcome.setText(messages.getString("welcome.label"));
        btnText.setText(messages.getString("btnText.label"));
        btnAudio.setText(messages.getString("btnAudio.label"));
        btnImg.setText(messages.getString("btnImg.label"));
        btnView.setText(messages.getString("btnView.label"));
        }
        catch(Exception e){
            System.out.println("Erro ao atualizar as traduções: "+e.getMessage());
        }
        
    }


    
}
