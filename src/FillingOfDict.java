import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;

public class FillingOfDict {
    public void fillingOfDict() throws ClassNotFoundException, SQLException, IOException {
        String userName = "root";
        String password = "1234";
        String connectionUrl = "jdbc:mysql://localhost:3306/words?useSSL=false";
        Class.forName("com.mysql.jdbc.Driver");
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
            System.out.println("We are connected");


//        URL url = new URL("https://wooordhunt.ru/word/property");
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
//            String line;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//        }


//            ResultSet resultSet = statement.executeQuery("SELECT word, renew ,frequencyP FROM dictionary WHERE renew=false AND frequencyP=9");
            ResultSet resultSet = statement.executeQuery("SELECT word, renew ,frequencyP FROM dictionary WHERE renew=false");

            while (resultSet.next()) {
                String wordFromDict = resultSet.getString("word");
                System.out.println(wordFromDict);

                String url = "https://wooordhunt.ru/word/" + wordFromDict;
                Document document = Jsoup.connect(url).get();

                Elements forms = document.select(".t_inline_en");
                System.out.println(forms.text() + "______________________________");

                Elements transcription = document.select(".transcription");
                System.out.println(transcription);

//                for (Element t : transcription) {
//                    System.out.println(t.attr("title"));
//                }

                String[] transcriptionArray = new String[2];
                int count = 0;
                for (Element t : transcription) {
                    transcriptionArray[count] = t.text();
                    System.out.println(transcriptionArray[count]);
                    count++;
                }

                Element rank = document.getElementById("rank_box");
                String freq = "НД";
                if (rank != null) {
                    freq = rank.text();
                }

                statement.executeUpdate("update dictionary set transcriptionA='" + transcriptionArray[0] + "', transcriptionE='" + transcriptionArray[1] + "', translate='" + forms.text() + "', frequencyW='" + freq + "', renew=true WHERE word='" + wordFromDict + "';");
                resultSet = statement.executeQuery("SELECT word, renew ,frequencyP FROM dictionary WHERE renew=false");
            }


//        System.out.println(document.title());


        }
    }
}