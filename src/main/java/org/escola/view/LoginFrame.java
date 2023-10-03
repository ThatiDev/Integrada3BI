package org.escola.view;

import org.escola.Database;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginFrame extends JFrame {

    public LoginFrame(){
        super("Login - Escola"); // chama o construtor da classe JFrame
        this.setSize(new Dimension(640, 480)); // seta o tamanho da tela
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //  seta que o butao de fechar é pra fechar
        this.setLayout(new GridBagLayout()); //  layout para deixar o painel CENTER no meio

        JPanel center = new JPanel(); // Cria um painel central para organizar os componentes
        BoxLayout box = new BoxLayout(center, BoxLayout.Y_AXIS); // deixa os componente de cima pra baixo
        center.setLayout(box);

        // criar titulo
        JPanel tp = new JPanel(new FlowLayout(FlowLayout.CENTER)); // painel para deixar o titulo no meio
        JLabel title = new JLabel("LOGIN"); // Define um layout de caixa vertical para organizar os componentes de cima para baixo
        title.setFont(title.getFont().deriveFont(24f)); // define o tamanho do titulo
        tp.add(title);
        center.add(tp); // Adiciona um painel vazio para criar um espaço em branco

        // as entradas
        JTextField username = new JTextField();
        username.setToolTipText("Insira o username"); // Define uma dica para o campo de texto
        center.add(username);
        username.setPreferredSize(new Dimension(240, 24));
        center.add(username);
        // pulo
        center.add(new JPanel());

        JPasswordField pass = new JPasswordField();
        pass.setToolTipText("Insira a senha"); // Define uma dica para o campo de senha
        pass.setPreferredSize(new Dimension(240, 24)); // Define o tamanho preferencial do campo de texto
        center.add(pass);

        center.add(new JPanel());

        JButton button = new JButton("Logar");
        // lambda -> FUNCAO ANONIMA
        // e -> {}
        button.addActionListener(e -> { // Define o comportamento do botão de login quando clicado
            Connection connection = Database.getConnection();
            try{
                Statement statement = connection.createStatement();
                ResultSet set = statement.executeQuery("SELECT password FROM admin WHERE username = '"+username.getText()+"';"); //  procura se existe uma senha para este usuario
                while (set.next()){
                    String password = set.getString("password"); // se existir um usuario, verifica se as senhas conferem
                    if(password.equals(pass.getText())){
                        MenuFrame menu = new MenuFrame(); // inicia o menu
                        this.dispose(); //  fecha este frame
                        return; // retorna
                    }
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
            // se nao existir um usuario ou a senha estiver errada
            pass.setText(""); // apaga senha
            pass.requestFocus(); // pede foco na senha
        });
        center.add(button);// Adiciona o botão de login ao painel central
        this.add(center); // Adiciona o painel central à janela
        this.setVisible(true); // mostra o frame
    }
}
