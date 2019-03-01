package old;

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
            ResultSet resultSet = statement.executeQuery("SELECT word, renew ,frequencyP FROM dictionary WHERE renew=false");
//            ResultSet resultSet = statement.executeQuery("SELECT word, renew ,frequencyP FROM dictionary WHERE frequencyP=6 and renew=false ");

            while (resultSet.next()) {
                String wordFromDict = resultSet.getString("word");
                System.out.println(wordFromDict);

                String url = "https://wooordhunt.ru/word/" + wordFromDict;
                Document document = Jsoup.connect(url).get();

                Elements forms = document.select(".t_inline_en");
                System.out.println(forms.text());

                Elements transcription = document.select(".transcription");
                System.out.println(transcription);

                String allText = document.text();
                String iyouwethey = null;
                if (allText.indexOf("I/you/we/they: ") > 0 && allText.indexOf("he/she/it:") > 0) {
                    iyouwethey = allText.substring(allText.indexOf("I/you/we/they: ") + 15, allText.indexOf("he/she/it:") - 1);
//                System.out.println(iyouwethey);
                }
                String hesheit = null;
                if (allText.indexOf("he/she/it: ") > 0 && allText.indexOf("ing ф") > 0) {
                    hesheit = allText.substring(allText.indexOf("he/she/it: ") + 11, allText.indexOf("ing ф") - 1);
//                System.out.println(hesheit);
                }
                String ing = null;
                if (allText.indexOf("ing ф") > 0 && allText.indexOf("2-я ф. (past tense):") > 0) {
                    ing = allText.substring(allText.indexOf("ing ф") + 29, allText.indexOf("2-я ф. (past tense):") - 1);
//                System.out.println(ing);
                }
                String pastTense2 = null;
                if (allText.indexOf("2-я ф. (past tense): ") > 0 && allText.indexOf("3-я ф. (past participle):") > 0) {
                    pastTense2 = allText.substring(allText.indexOf("2-я ф. (past tense): ") + 21, allText.indexOf("3-я ф. (past participle):") - 1);
//                System.out.println(pastTense2);
                }
                String pastParticiple3 = null;
                if (allText.indexOf("3-я ф. (past participle): ") > 0) {
                    if (allText.indexOf("3-я ф. (past participle): ") < allText.indexOf("noun ед. ч.")) {
                        pastParticiple3 = allText.substring(allText.indexOf("3-я ф. (past participle): ") + 26, allText.indexOf("noun ед. ч.") - 1);
                    } else {
                        pastParticiple3 = allText.substring(allText.indexOf("3-я ф. (past participle): ") + 26, 26 + allText.substring(allText.indexOf("3-я ф. (past participle): "), allText.indexOf("3-я ф. (past participle): ") + 130).indexOf(" ") + allText.indexOf("3-я ф. (past participle): "));
                    }
//                System.out.println(pastParticiple3);
                }
                String singular = null;
                if (allText.indexOf("ед. ч.(singular): ") > 0 && allText.indexOf("мн. ч.(plural): ") > 0) {
                    singular = allText.substring(allText.indexOf("ед. ч.(singular): ") + 18, allText.indexOf("мн. ч.(plural): ") - 1);
//                System.out.println(singular);
                }
                String plural = null;
                if (allText.indexOf("мн. ч.(plural): ") > 0) {
                    if (allText.substring(allText.indexOf("мн. ч.(plural): "), 200 + allText.indexOf("мн. ч.(plural): ")).indexOf("adjective") > 0) {
                        plural = allText.substring(allText.indexOf("мн. ч.(plural): ") + 16, allText.indexOf("мн. ч.(plural): ") + allText.substring(allText.indexOf("мн. ч.(plural): "), 200 + allText.indexOf("мн. ч.(plural): ")).indexOf("adjective"));
                    } else {
                        plural = allText.substring(allText.indexOf("мн. ч.(plural): ") + 16, allText.indexOf("Дополнение / ") - 1);
//                System.out.println(plural);
                    }

                }
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

                String form2 = null;
                if (allText.indexOf("является 2-й формой глагола") > 0) {
//                    form2 = allText.substring(allText.indexOf("является 2-й формой глагола") + 31, allText.indexOf("- является 3-й формой глагола") - 1);
                    form2 = allText.substring(allText.indexOf("является 2-й формой глагола") + 31, 33 + allText.substring(allText.indexOf("- является 2-й формой глагола") + 33, allText.indexOf("- является 2-й формой глагола") + 130).indexOf(" ") + allText.indexOf("- является 2-й формой глагола"));
//                System.out.println(form2);
                }

                String form3 = null;
                if (allText.indexOf("является 3-й формой глагола") > 0) {
                    form3 = allText.substring(allText.indexOf("является 3-й формой глагола") + 31, 33 + allText.substring(allText.indexOf("- является 3-й формой глагола") + 33, allText.indexOf("- является 3-й формой глагола") + 130).indexOf(" ") + allText.indexOf("- является 3-й формой глагола"));
//                System.out.println(form2);
                }


                statement.executeUpdate("update dictionary set transcriptionA='" + transcriptionArray[0] + "', transcriptionE='" + transcriptionArray[1] + "', translate='" + forms.text() + "', frequencyW='" + freq + "', renew=true, iyouwethey='" + iyouwethey + "', hesheit='" + hesheit + "', ing='" + ing + "',pastTense2='" + pastTense2 + "',pastParticiple3='" + pastParticiple3 + "',singular='" + singular + "',plural='" + plural + "',form2='" + form2 + "',form3='" + form3 + "' WHERE word='" + wordFromDict + "';");


                resultSet = statement.executeQuery("SELECT word, renew ,frequencyP FROM dictionary WHERE renew=false");
//                resultSet = statement.executeQuery("SELECT word, renew ,frequencyP FROM dictionary WHERE frequencyP=6 and renew=false ");
            }


//        System.out.println(document.title());


        }
    }
}