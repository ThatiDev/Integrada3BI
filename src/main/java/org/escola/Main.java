package org.escola;

import com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme;
import org.ajbrown.namemachine.Gender;
import org.ajbrown.namemachine.Name;
import org.ajbrown.namemachine.NameGenerator;
import org.escola.view.LoginFrame;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        try{
            UIManager.setLookAndFeel(new FlatGradiantoMidnightBlueIJTheme());
        }catch (Exception e){
            e.printStackTrace();
        }
        NameGenerator nameGenerator = new NameGenerator(); // gerador de nomes
        Database.startConnection(); // inicia a conexão no banco de dados
        Connection connection = Database.getConnection();
        System.out.println("Carregando os dados... (isto pode e vai demorar)");
        try{
            Statement statement = connection.createStatement();
            // cria as tabelas e insere os dados padrao
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS admin(username VARCHAR(255), password VARCHAR(255));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS turmas(id INTEGER PRIMARY KEY AUTOINCREMENT, sala INTEGER(255), ano VARCHAR(255));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS salas(id INTEGER PRIMARY KEY AUTOINCREMENT, name INTEGER(255), cadeiras INTEGER(255));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS alunos(id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR(255), dataDeNascimento BIGINT, sala INTEGER(255));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS responsavel(mae VARCHAR(255), pai VARCHAR(255), aluno INTEGER(255) UNIQUE);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS boletim(id INTEGER PRIMARY KEY AUTOINCREMENT, notas TEXT, aluno INTEGER(255));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CalendarioEscolar (id INTEGER PRIMARY KEY AUTOINCREMENT, data VARCHAR(255), evento VARCHAR(255));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Biblioteca (id INTEGER PRIMARY KEY AUTOINCREMENT, quantidade INTEGER, autor VARCHAR(255), livro VARCHAR(255));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ContatoEmergencia (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR(255), telefone VARCHAR(255));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Funcionarios (id INTEGER PRIMARY KEY AUTOINCREMENT, cargo VARCHAR(255), salario VARCHAR(255), nome VARCHAR(255));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Cantinas (id INTEGER PRIMARY KEY AUTOINCREMENT, alimento VARCHAR(255), valores VARCHAR(255));");

            statement.close();

            statement = connection.createStatement();
            statement.executeUpdate("INSERT OR IGNORE INTO admin (username, password) VALUES ('admin', 'admin');");
            int ts = 100;
            Random rd = new Random(); //  cria um random para gerar números aleatorios
            List<String> eventos = Arrays.asList("Colação de grau", "Dia D","Dia dos Pais", "Dia das crianças", "Reunião de Pais", "Conselho de Classe");
            SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
            for (int i = 1; i < ts; i++) {
                statement.executeUpdate("INSERT OR IGNORE INTO CalendarioEscolar (data, evento) VALUES ('"+SDF.format(new Date())+"', '"+eventos.get(rd.nextInt(eventos.size()))+"');");
                // cria os dados padrão
                int cadeiras = 5;
                statement.executeUpdate("INSERT OR IGNORE INTO turmas (sala, ano) VALUES ('"+i+"', '"+rd.nextInt(1, 4)+" TI');");
                statement.executeUpdate("INSERT OR IGNORE INTO salas (name, cadeiras) VALUES ('A"+i+"', '"+cadeiras+"');");
                List<Name> names = nameGenerator.generateNames(cadeiras); // pede nomes aleatorios
                for (Name name : names) { // adiciona os alunos com nomes aleatorios
                    // faz a query para add o aluno com nome gerado e com data de nascimento do de agora na sala que esta sendo trabalhada no momento
                    statement.executeUpdate("INSERT OR IGNORE INTO alunos (nome, dataDeNascimento, sala) VALUES ('"+name.getFirstName()+"', '"+System.currentTimeMillis()+"', '"+i+"')");
                    statement.executeUpdate("INSERT OR IGNORE INTO ContatoEmergencia (nome, telefone) VALUES ('"+name.getFirstName()+"', '"+rd.nextInt(111111111, 999999999)+"')");
                }
            }

            ResultSet set = statement.executeQuery("SELECT * FROM alunos;");
            List<Integer> ids = new ArrayList<>();
            while(set.next()){
                // ADICIONA UM RESPONSAVEL PARA CADA ALUNO
                int i = set.getInt("id");
                ids.add(i);
            }

            for (Integer id : ids) {
                statement.executeUpdate("INSERT OR IGNORE INTO responsavel (mae, pai, aluno) VALUES ('"+nameGenerator.generateName(Gender.FEMALE).getFirstName()+"', '"+nameGenerator.generateName(Gender.MALE).getFirstName()+"','"+id+"');");
            }
            statement.close();
        } catch (Exception e){
            e.printStackTrace(); // se der erro printar
        }
        LoginFrame loginFrame = new LoginFrame(); // inicia a tela de login
    }
}
