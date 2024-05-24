package DAO;

import DTO.*;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandsDB extends GeneralData {
    
    /// inserir usuario
    public void insertUser(Connection conn, String username, String email, String password){
        String sqlInsert = "INSERT INTO USER_LOG(username, email, password) VALUES(?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = ConnFactory.getConn().prepareStatement(sqlInsert);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.setAutoCommit(false);
                conn.rollback();
            } catch (SQLException e1) {
                System.out.println("Erro ao incluir os dados " + e1.toString());
                throw new RuntimeException(e1);
            }
        }
    }
    
    /// excluir usuario
    public void deleteUser(Connection conn){
        String sqlDelete = "DELETE FROM USER_LOG WHERE email = ?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlDelete);
            stmt.setString(1, getEmail());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.setAutoCommit(false);
                conn.rollback();
            } catch (SQLException e1) {
                System.out.println("Erro ao excluir os dados " + e1.toString());
                throw new RuntimeException(e1);
            }
        }
    }
    
    /// validar email
    public static String emailVerify(Connection conn, String uEmail) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String check = null;
        try {
            String sqlSelect = "SELECT email FROM USER_LOG WHERE email = ?";
            stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, uEmail);

            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    check = res.getString("email");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar os dados " + e.toString());
            throw new RuntimeException(e);
        }
       
        return check;
    }
    
    /// validar senha
    public static String passVerify(Connection conn, String uEmail){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String check = null;
        try {
            String sqlSelect = "SELECT password FROM USER_LOG WHERE email = ?";
            stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, uEmail);

            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    check = res.getString("password");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar os dados " + e.toString());
            throw new RuntimeException(e);
        }
       
        return check;
    }

    public static String getName(Connection conn, String uEmail){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String check = null;
        try {
            String sqlSelect = "SELECT username FROM USER_LOG WHERE email = ?";
            stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, uEmail);

            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    check = res.getString("username");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar os dados " + e.toString());
            throw new RuntimeException(e);
        }
       
        return check;
    }
    
    public static void uploadAudio(Connection conn, String audioname,String path) throws IOException {
        FileInputStream stream = null;
        try {
    
                File arq = new File(path);
                // String audioname = arq.getName();
                stream = new FileInputStream(arq);
                String sqlInsert = "INSERT INTO audio_files (audioname, audiofile) VALUES (?,?)";
                PreparedStatement stmt = conn.prepareStatement(sqlInsert);
                stmt.setString(1, audioname);
                stmt.setBinaryStream(2, stream, (int) arq.length());
                stmt.executeUpdate();
            }
         catch (SQLException e) {
            e.printStackTrace();
        }
        
    }


    
    /// excluir audio
    public void deleteAudio(Connection conn, String audioname){
        String sqlDelete = "DELETE FROM audio_files WHERE audioname = ?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlDelete);
            stmt.setString(1,audioname );
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.setAutoCommit(false);
                conn.rollback();
            } catch (SQLException e1) {
                System.out.println("Erro ao excluir os dados " + e1.toString());
                throw new RuntimeException(e1);
            }
        }
    }

    public void downloadAudio(Connection conn, String audioname){
        String sqlSelect = "SELECT audiofile FROM audio_files WHERE audioname = ?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, audioname);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                byte[] audioBytes = rs.getBytes("audiofile");
                InputStream audioStream = new ByteArrayInputStream(audioBytes);
                AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, false);
                AudioInputStream audioInputStream = new AudioInputStream(audioStream, format, audioBytes.length / format.getFrameSize());
                File audioFile = new File(audioname+".wav");
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
            }
            stmt.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    /// inserir texto
    public void uploadTxt(Connection connection, String textname,String path) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder conteudo = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                conteudo.append(linha).append("\n");
            }
            String sqlInsert = "INSERT INTO text_files (textname, textfile) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
            preparedStatement.setString(1,  textname);
            preparedStatement.setString(2, conteudo.toString());
            preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Arquivo de texto inserido com sucesso!");
        }
        catch (SQLException | IOException e) {
            e.printStackTrace();
        }
                
        
    }
    
    /// excluir texto
    public void deleteTxt(Connection conn, String textname){
        String sqlDelete = "DELETE FROM text_files WHERE textname = ?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlDelete);
            stmt.setString(1,textname);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.setAutoCommit(false);
                conn.rollback();
            } catch (SQLException e1) {
                System.out.println("Erro ao excluir os dados " + e1.toString());
                throw new RuntimeException(e1);
            }
        }
    }
    
    public void downloadTxt(Connection conn, String textname){
        String sqlSelect = "SELECT textfile FROM text_files WHERE textname = ?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, textname);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String conteudo = rs.getString("textfile");
                FileWriter arq = new FileWriter(textname+".txt");
                PrintWriter gravarArq = new PrintWriter(arq);
                gravarArq.printf(conteudo);
                arq.close();
            }
            stmt.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /// inserir imagem
    public void uploadImg(Connection conn, String imagename,String imagePath) {
        try {
            File imageFile = new File(imagePath);
            FileInputStream inputStream = new FileInputStream(imageFile);

            String insertSQL = "INSERT INTO image_files (imagename, imagefile) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertSQL);
            stmt.setString(1, imagename);
            stmt.setBinaryStream(2, inputStream, (int) imageFile.length());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Imagem inserida com sucesso!");
            } else {
                System.out.println("Falha ao inserir imagem!");
            }
            stmt.close();
            inputStream.close();
            
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /// excluir imagem
    public void deleteImg(Connection conn, String imagename){
        String sqlDelete = "DELETE FROM image_files WHERE imagename = ?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlDelete);
            stmt.setString(1, imagename);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.setAutoCommit(false);
                conn.rollback();
            } catch (SQLException e1) {
                System.out.println("Erro ao excluir os dados " + e1.toString());
                throw new RuntimeException(e1);
            }
        }
    }

    public void downloadImg(Connection conn, String imagename){
        String sqlSelect = "SELECT imagefile FROM image_files WHERE imagename = ?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, imagename);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes("imagefile");
                ImageIcon format = new ImageIcon(imgBytes);
                BufferedImage image = new BufferedImage(format.getIconWidth(), format.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                image.createGraphics().drawImage(format.getImage(), 0, 0, null);
                File imageFile = new File(imagename+".png");
                ImageIO.write(image, "png", imageFile);
            }
            stmt.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    //envia todos os arquivos de texto
    public String sendAllTxt(Connection conn) {
       
        try {
            String textname="alltxt";
            String sqlSelect = "SELECT textname FROM text_files";
            PreparedStatement stmt = conn.prepareStatement(sqlSelect);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                textname += ";"+rs.getString("textname");
            }
            return textname;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //envia todos os arquivos de audio
    public String sendAllWav(Connection conn) {
       
        try {
            String audioname="allwav";
            String sqlSelect = "SELECT audioname FROM audio_files";
            PreparedStatement stmt = conn.prepareStatement(sqlSelect);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                audioname += ";"+rs.getString("audioname");
            }
            return audioname;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //envia todos os arquivos de imagem
    public String sendAllImg(Connection conn) {
       
        try {
            String imagename="allimg";
            String sqlSelect = "SELECT imagename FROM image_files";
            PreparedStatement stmt = conn.prepareStatement(sqlSelect);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                imagename += ";"+rs.getString("imagename");
            }
            return imagename;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}