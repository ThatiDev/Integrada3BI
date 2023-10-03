package org.escola.view;

import org.ajbrown.namemachine.Gender;
import org.ajbrown.namemachine.NameGenerator;
import org.escola.Database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MenuFrame extends JFrame {

    private CardLayout card = new CardLayout(); // Gerenciador de layout para exibir diferentes telas.

    public MenuFrame(){
        super("Menu - Escola"); // Define o título da janela
        this.setSize(640, 480);// Define o tamanho da janela
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);// Fecha o aplicativo quando a janela é fechada
        this.setLayout(new BorderLayout());// Define o layout principal da janela como BorderLayout

        JPanel left = new JPanel();// Painel à esquerda
        left.setBorder(new EmptyBorder(10, 10, 10, 10));// Define uma margem vazia ao redor do painel
        left.setBackground(new Color(3, 198, 252));// Define a cor de fundo do painel
        BoxLayout box = new BoxLayout(left, BoxLayout.Y_AXIS); // Define um layout de caixa vertical para o painel
        left.setLayout(box);
        JButton turma = new JButton("Gerenciar Turmas");// Botão "Gerenciar Turmas"
        left.add(turma);
        JButton salas = new JButton("Gerenciar Salas");// Botão "Gerenciar Salas"
        left.add(salas);
        JButton alunos = new JButton("Ver alunos");// Botão "Gerenciar Salas"
        left.add(alunos);
        JButton resp = new JButton("Ver responsaveis");// Botão "Gerenciar Salas"
        left.add(resp);

        JPanel center = new JPanel();
        center.setLayout(card);// Define o layout do centro como CardLayout
        this.add(left, BorderLayout.WEST);// Adiciona o painel esquerdo à esquerda da janela
        this.add(center, BorderLayout.CENTER); // Adiciona o painel central ao centro da janela

        // ADICIONA AS OUTRAS TELAS
        center.add(getHome(), "HOME");// Adiciona o painel HOME ao centro com o nome "HOME"
        center.add(getTurmas(), "TURMAS");//Adiciona o painel de Turmas ao centro com o nome "TURMAS"
        center.add(getSalas(), "SALAS");// Adiciona o painel de Salas ao centro com o nome "SALAS"
        center.add(getAlunos(), "ALUNOS");// Adiciona o painel de Salas ao centro com o nome "SALAS"
        center.add(getResponsavel(), "RESPONSAVEL");// Adiciona o painel de Salas ao centro com o nome "SALAS"

        // ADICIONA OS EVENTOS DE CLICK NO BOTAO

        turma.addActionListener(e -> {
            card.show(center, "TURMAS"); // Mostra o painel de Turmas quando o botão é clicado
        });
        salas.addActionListener(e -> {
            card.show(center, "SALAS");// Mostra o painel de Salas quando o botão é clicado
        });

        alunos.addActionListener(e -> {
            card.show(center, "ALUNOS");// Mostra o painel de Salas quando o botão é clicado
        });
        resp.addActionListener(e -> {
            card.show(center, "RESPONSAVEL");// Mostra o painel de Salas quando o botão é clicado
        });


        this.setVisible(true); // Torna a janela visível
        card.show(center, "HOME"); // EXIBE A TELA PRINCIPAL
    }

    private JPanel getHome(){
        JPanel home = new JPanel(new GridBagLayout());
        home.add(new JLabel("Seja bem-vindo ao painel!"));// Adiciona um rótulo de boas-vindas ao painel
        return home;
    }

    private JPanel getTurmas(){
        JPanel content = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton add = new JButton("Add Turma");// Botão para adicionar turma
        add.addActionListener(e -> {
            try{
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement();
                // add turma
                statement.executeUpdate("INSERT OR IGNORE INTO turmas (sala, ano) VALUES ('"+JOptionPane.showInputDialog("Qual é a sala em que a turma vai estar?")+"', '2 TI');");
                statement.close();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        });
        JButton remove = new JButton("Remove Turma");// Botão para remover turma
        remove.addActionListener(e -> {
            try {
                String sala = JOptionPane.showInputDialog(null, "Em qual sala a turma que deseja remover está?");
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("DELETE FROM turmas WHERE sala = '"+sala+"';"); // remove turma
                statement.close();
            } catch (Exception xe){
                xe.printStackTrace();
            }
        });
        buttons.add(add);
        buttons.add(remove);
        content.add(buttons, BorderLayout.NORTH);
        Vector<String> cols = new Vector<>(List.of("Ano", "Sala"));// Cabeçalhos da tabela
        Vector<Vector<String>> data = new Vector<>();
        try{
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM turmas;"); // Consulta SQL para obter turmas
            List<Object[]> turmas = new ArrayList<>();
            while(set.next()){
                turmas.add(new Object[]{ set.getInt("sala"), set.getString("ano") });
            }

            for (Object[] turma : turmas) {
                Vector<String> val = new Vector<>();
                ResultSet set2 = statement.executeQuery("SELECT name FROM salas WHERE id = '"+turma[0]+"';");// Consulta SQL para obter nomes das salas
                val.add((String) turma[1]);
                val.add(set2.getString("name"));
                data.add(val);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        JTable table = new JTable(data, cols);// Cria uma tabela com os dados e cabeçalhos
        content.add(new JScrollPane(table), BorderLayout.CENTER);// Adiciona a tabela a um painel rolável e o coloca no centro
        return content;
    }

    private JPanel getSalas(){
        JPanel content = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton add = new JButton("Add Sala");// Botão para adicionar sala
        add.addActionListener(e -> {
            try{
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement();
                // Insere uma nova sala
                statement.executeUpdate("INSERT OR IGNORE INTO salas (name, cadeiras) VALUES ('A"+JOptionPane.showInputDialog("Qual é o número da sala?")+"', '"+10+"');");
                statement.close();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        });
        JButton remove = new JButton("Remove Sala");// Botão para remover sala
        remove.addActionListener(e -> {
            try {
                String sala = JOptionPane.showInputDialog(null, "Qual sala deseja remover?");
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("DELETE FROM salas WHERE name = '"+sala+"';"); // deleta a sala
                statement.close();
            } catch (Exception xe){
                xe.printStackTrace();
            }
        });
        buttons.add(add);
        buttons.add(remove);
        content.add(buttons, BorderLayout.NORTH);
        Vector<String> cols = new Vector<>(List.of("Sala", "Qnt. Cadeiras")); // Cabeçalhos da tabela
        Vector<Vector<String>> data = new Vector<>();
        try{
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM salas;");// Consulta SQL para obter salas
            while(set.next()){
                Vector<String> val = new Vector<>();
                val.add(set.getString("name"));
                val.add(String.valueOf(set.getInt("cadeiras")));
                data.add(val);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        JTable table = new JTable(data, cols);// Cria uma tabela com os dados e cabeçalhos
        content.add(new JScrollPane(table), BorderLayout.CENTER);// Adiciona a tabela a um painel rolável e o coloca no centro
        return content;
    }

    private JPanel getAlunos(){
        NameGenerator nameGenerator = new NameGenerator();
        JPanel content = new JPanel(new BorderLayout());
        Vector<String> cols = new Vector<>(List.of("Nome", "Sala"));
        Vector<Vector<String>> data = new Vector<>();
        try{
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM alunos;");// Consulta SQL para obter salas
            while(set.next()){
                Vector<String> val = new Vector<>();
                val.add(set.getString("nome"));
                val.add(String.valueOf(set.getInt("sala")));
                data.add(val);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        JTable table = new JTable(data, cols);// Cria uma tabela com os dados e cabeçalhos
        content.add(new JScrollPane(table), BorderLayout.CENTER);// Adiciona a tabela a um painel rolável e o coloca no centro
        return content;
    }

    private JPanel getResponsavel(){
        JPanel content = new JPanel(new BorderLayout());
        Vector<String> cols = new Vector<>(List.of("Pai", "Mãe", "ID Aluno"));
        Vector<Vector<String>> data = new Vector<>();
        try{
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM responsavel;");// Consulta SQL para obter salas
            while(set.next()){
                Vector<String> val = new Vector<>();
                val.add(set.getString("mae"));
                val.add(set.getString("pai"));
                val.add(String.valueOf(set.getInt("aluno")));
                data.add(val);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        JTable table = new JTable(data, cols);// Cria uma tabela com os dados e cabeçalhos
        content.add(new JScrollPane(table), BorderLayout.CENTER);// Adiciona a tabela a um painel rolável e o coloca no centro
        return content;
    }
}
