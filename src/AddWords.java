import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AddWords {
    public void addWords() throws IOException, ClassNotFoundException, SQLException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("c:/myProgDict/addResult.txt"));
             BufferedReader br = new BufferedReader(new FileReader("c:/myProgDict/output.txt"))) {


            String userName = "root";
            String password = "1234";

            String connectionUrl = "jdbc:mysql://localhost:3306/words?useSSL=false";

            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            try (Connection connectoin = DriverManager.getConnection(connectionUrl, userName, password);
                 Statement statement = connectoin.createStatement()) {
                System.out.println("We are connected");

//            statement.executeUpdate("create table Dera(id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL , PRIMARY KEY (id));");
                System.out.println("______________________________________");

                String line;
                while ((line = br.readLine()) != null) {
                    System.out.print(line);

                    int pos = line.indexOf(" ");
                    String wordS = line.substring(0, pos);
                    System.out.print("-" + wordS);
                    System.out.print("-" + pos);

                    int freq = Integer.parseInt(line.substring(pos + 1));
                    System.out.println("-" + freq);

                    statement.executeUpdate("insert into dictionary (word) values ('" + wordS + "');");

                }
            }
        }
    }
}
