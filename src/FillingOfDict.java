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
//        DriverManager.registerDriver(new com.mysql.jdbc.Driver());

        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
            System.out.println("We are connected");
//            ResultSet resultSet = statement.executeQuery("SELECT word, renew ,frequencyP FROM dictionary WHERE renew=false AND frequencyP=9");
            ResultSet resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
//            ResultSet resultSet = statement.executeQuery("SELECT word, renew ,frequencyP FROM dictionary WHERE frequencyP=6 and renew=false ");

            while (resultSet.next()) {
                String wordFromDict = resultSet.getString("word");
                System.out.println(wordFromDict);

                String url = "https://wooordhunt.ru/word/" + wordFromDict;
                Document document = Jsoup.connect(url).get();

                String allText = document.text();
                if (allText.indexOf("проверьте нет ли опечатки") > 0) {
                    statement.executeUpdate("delete from dictionary where word='" + wordFromDict + "'");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }

                String src;
                if (allText.indexOf("используется как мн.ч. для существительного") > 0) {
                    src = allText.substring(allText.indexOf("используется как мн.ч. для существительного") + 44, 44 + allText.substring(allText.indexOf("используется как мн.ч. для существительного") + 44, allText.indexOf("используется как мн.ч. для существительного") + 130).indexOf(" ") + allText.indexOf("используется как мн.ч. для существительного"));
                    statement.executeUpdate("update dictionary set renew=true, src='" + src + "' WHERE word='" + wordFromDict + "';");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }

                if (allText.indexOf("- является 3-й формой глагола") > 0) {
                    src = allText.substring(allText.indexOf("- является 3-й формой глагола") + 33, 35 + allText.substring(allText.indexOf("- является 3-й формой глагола") + 35, allText.indexOf("- является 3-й формой глагола") + 130).indexOf(" ") + allText.indexOf("- является 3-й формой глагола"));
                    statement.executeUpdate("update dictionary set renew=true, src='" + src + "' WHERE word='" + wordFromDict + "';");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }

                if (allText.indexOf("- используется как present tense(he/she/it) для глагола") > 0) {
                    src = allText.substring(allText.indexOf("- используется как present tense(he/she/it) для глагола") + 59, 61 + allText.substring(allText.indexOf("- используется как present tense(he/she/it) для глагола") + 61 , allText.indexOf("- используется как present tense(he/she/it) для глагола") + 130).indexOf(" ") + allText.indexOf("- используется как present tense(he/she/it) для глагола"));
                    statement.executeUpdate("update dictionary set renew=true, src='" + src + "' WHERE word='" + wordFromDict + "';");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }





                Elements forms = document.select(".t_inline_en");//перевод
                if (forms.text().length() == 0) {
                    statement.executeUpdate("update dictionary set renew=true WHERE word='" + wordFromDict + "';");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }
                System.out.println(forms.text());



                Elements transcription = document.select(".transcription");//транскрипция
                String transcript = transcription.first().text();

                Element rank = document.getElementById("rank_box");
                String freq = "НД";
                if (rank != null) {
                    freq = rank.text();
                }

                statement.executeUpdate("update dictionary set transcriptionA='" + transcript + "',  translate='" + forms.text() + "', frequencyW='" + freq + "', renew=true WHERE word='" + wordFromDict + "';");

                resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
            }
        }
    }
}