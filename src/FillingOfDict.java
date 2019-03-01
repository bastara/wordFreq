import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;

class FillingOfDict {
    void fillingOfDict() throws ClassNotFoundException, SQLException, IOException {
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
                    statement.executeUpdate("update dictionary set renew=true, src='" + src + "', cause='используется как мн.ч. для существительного' WHERE word='" + wordFromDict + "';");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }

                if (allText.indexOf("- является 2-й формой глагола") > 0) {
                    src = allText.substring(allText.indexOf("- является 2-й формой глагола") + 33, 35 + allText.substring(allText.indexOf("- является 2-й формой глагола") + 35, allText.indexOf("- является 2-й формой глагола") + 130).indexOf(" ") + allText.indexOf("- является 2-й формой глагола"));
                    statement.executeUpdate("update dictionary set renew=true, src='" + src + "', cause='- является 2-й формой глагола' WHERE word='" + wordFromDict + "';");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }

                if (allText.indexOf("- используется как present tense(he/she/it) для глагола") > 0) {
                    src = allText.substring(allText.indexOf("- используется как present tense(he/she/it) для глагола") + 59, 61 + allText.substring(allText.indexOf("- используется как present tense(he/she/it) для глагола") + 61, allText.indexOf("- используется как present tense(he/she/it) для глагола") + 130).indexOf(" ") + allText.indexOf("- используется как present tense(he/she/it) для глагола"));
                    statement.executeUpdate("update dictionary set renew=true, src='" + src + "', cause='- используется как present tense(he/she/it) для глагола' WHERE word='" + wordFromDict + "';");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }

                if (allText.indexOf("- используется как срав. степ.(comparative) для прилагательного") > 0) {
                    src = allText.substring(allText.indexOf("- используется как срав. степ.(comparative) для прилагательного") + 64, 66 + allText.substring(allText.indexOf("- используется как срав. степ.(comparative) для прилагательного") + 66, allText.indexOf("- используется как срав. степ.(comparative) для прилагательного") + 130).indexOf(" ") + allText.indexOf("- используется как срав. степ.(comparative) для прилагательного"));
                    statement.executeUpdate("update dictionary set renew=true, src='" + src + "', cause='- используется как срав. степ.(comparative) для прилагательного' WHERE word='" + wordFromDict + "';");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }

                if (allText.indexOf("- является 2-й формой неправильного глагола") > 0) {
                    src = allText.substring(allText.indexOf("- является 2-й формой неправильного глагола") + 47, 49 + allText.substring(allText.indexOf("- является 2-й формой неправильного глагола") + 49, allText.indexOf("- является 2-й формой неправильного глагола") + 130).indexOf(" ") + allText.indexOf("- является 2-й формой неправильного глагола"));
                    statement.executeUpdate("update dictionary set renew=true, src='" + src + "', cause='- является 2-й формой неправильного глагола' WHERE word='" + wordFromDict + "';");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }

                if (allText.indexOf("Перевод от Yandex Translate / Google Translate") > 0) {
                    Elements yandex = document.select(".light_tr");//яндекс перевод
                    statement.executeUpdate("update dictionary set renew=true, translate='" + yandex.text() + " - Перевод от Yandex Translate / Google Translate" + "' WHERE word='" + wordFromDict + "';");
                    resultSet = statement.executeQuery("SELECT word, renew FROM dictionary WHERE renew=false");
                    continue;
                }

                if (allText.indexOf("Другие формы слова:") > 0) {
                    src = allText.substring(allText.indexOf("Другие формы слова:") + 23, 25 + allText.substring(allText.indexOf("Другие формы слова:") + 25, allText.indexOf("Другие формы слова:") + 130).indexOf(" ") + allText.indexOf("Другие формы слова:"));
                    statement.executeUpdate("update dictionary set renew=true, src='" + src + "', cause='Другие формы слова:' WHERE word='" + wordFromDict + "';");
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