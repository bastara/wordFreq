import java.sql.*;

class WithoutTranslate {

    void allWords() throws ClassNotFoundException, SQLException {
        String userName = "root";
        String password = "1234";
        String connectionUrl = "jdbc:mysql://localhost:3306/words?useSSL=false";
        Class.forName("com.mysql.jdbc.Driver");
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
            System.out.println("We are connected");

            ResultSet resultSet = statement.executeQuery("SELECT * FROM dictionary WHERE translate='' and src IS NULL ;");

            while (resultSet.next()) {
                String wordFromDict = resultSet.getString("word");
                System.out.println(wordFromDict);
            }
        }
    }
}
