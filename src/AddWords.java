import java.io.*;
import java.sql.*;

public class AddWords {
    public void addWords() throws IOException, SQLException, ClassNotFoundException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("c:/myProgDict/addResult.txt"));
             BufferedReader br = new BufferedReader(new FileReader("c:/myProgDict/output.txt"))) {
//            statement.executeUpdate("create table Dera(id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL , PRIMARY KEY (id));");

            String userName = "root";
            String password = "1234";
            String connectionUrl = "jdbc:mysql://localhost:3306/words?useSSL=false";
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
                 Statement statement = connection.createStatement()) {
                System.out.println("We are connected");

                String line;
                int count = 0;
                while ((line = br.readLine()) != null) {
                    ResultSet resultSet = statement.executeQuery("SELECT word, frequencyW FROM dictionary");
                    int pos = line.indexOf(" ");
                    String wordS = line.substring(0, pos);
                    int freq = Integer.parseInt(line.substring(pos + 1));

                    boolean wordInDict = wordInDict(resultSet, wordS);
                    System.out.println(wordInDict + " " + wordS);

                    if (!wordInDict) {
                        statement.executeUpdate("insert into dictionary (word, frequencyP, newWord) values ('" + wordS + "','" + freq + "',true);");
                        count++;
                    }
                }
                System.out.println("Добавлено слов-" + count);
            }
        }
    }

    private boolean wordInDict(ResultSet resultSet, String wordS) throws SQLException {
        while (resultSet.next()) {
            String wordFromDict = resultSet.getString("word");
            if (wordS.equals(wordFromDict)) {
                return true;
            }
        }
        System.out.println(wordS + " добавлено в словарь");
        return false;
    }
}