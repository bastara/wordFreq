import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

class AddWords {
    void addWords() throws IOException, SQLException, ClassNotFoundException {
        try (//BufferedWriter bw = new BufferedWriter(new FileWriter("c:/myProgDict/addResult.txt"));
             BufferedReader br = new BufferedReader(new FileReader("c:/myProgDict/output.txt"))) {

            String userName = "root";
            String password = "1234";
            String connectionUrl = "jdbc:mysql://localhost:3306/words?useSSL=false";//отрубили SSL иначе exception
            Class.forName("com.mysql.jdbc.Driver");//драйвер для mysql
//            DriverManager.registerDriver(new com.mysql.jdbc.Driver());//видимо тоже драйвер для mysql

            try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);//класс для установки соединения с базой
                 Statement statement = connection.createStatement()) {//класс для выполнения sql запросов
                System.out.println("We are connected");

                String line;
                int count = 0;
                int countExisrWords = 0;
                while ((line = br.readLine()) != null) {
                    int pos = line.indexOf(" ");
                    String wordS = line.substring(0, pos);
                    ResultSet resultSet = statement.executeQuery("SELECT word FROM dictionary where word='" + wordS + "';");//класс для получения результатов запросов.

                    if (!resultSet.next()) {
                        System.out.println(wordS);
                        statement.executeUpdate("insert into dictionary (word, newWord,renew) values ('" + wordS + "', true, false );");
                        count++;
                    } else {
                        countExisrWords++;
                    }
                }
                System.out.println("Добавлено слов-" + count);
                System.out.println("Ранее были добавлены-" + countExisrWords);
            }
        }
    }
}