import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        String userName = "root";
        String password = "1234";



//        String connectionUrl = "jdbc:mysql://localhost:3306/coworker" +
//                "useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
//        String connectionUrl = "jdbc:mysql://localhost:3306/coworker?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String connectionUrl = "jdbc:mysql://localhost:3306/coworker?useSSL=false";




        Class.forName("com.mysql.jdbc.Driver");
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        try (Connection connectoin = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connectoin.createStatement()) {
            System.out.println("We con");

            statement.executeUpdate("create table Dera(id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL , PRIMARY KEY (id));");
            System.out.println("______________________________________");
        }

    }


    //        System.out.println("Выбери операцию:");
//        System.out.println("1-обработать файл");
//        System.out.println("2-добавить новые слова в словарь");
//
//        Scanner scanner = new Scanner(System.in);
//        int i = scanner.nextInt();
//        if (i == 1) {
//            new WordFreq().processingTetx();
//        }
}

