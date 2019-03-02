import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
//        String userName = "root";
//        String password = "1234";
//
//        String connectionUrl = "jdbc:mysql://localhost:3306/words?useSSL=false";
//
//        Class.forName("com.mysql.jdbc.Driver");
//        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//        try (Connection connectoin = DriverManager.getConnection(connectionUrl, userName, password);
//             Statement statement = connectoin.createStatement()) {
//            System.out.println("We are connected");
//
////            statement.executeUpdate("create table Dera(id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL , PRIMARY KEY (id));");
//            System.out.println("______________________________________");
//        }
//
//        URL url = new URL("https://wooordhunt.ru/word/property");
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
//            String line;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//        }

//        Document document = Jsoup.connect("https://wooordhunt.ru/word/oceanfront").get();
//        System.out.println(document.title());
//
//
//        Elements forms = document.select(".t_inline_en");
//        System.out.println(forms.text() + "______________________________");
//
//        Elements transcription = document.select(".transcription");
//        System.out.println(transcription);
//
//        for (Element t : transcription) {
//            System.out.println(t.attr("title"));
//        }
//
//        for (Element t : transcription) {
//            System.out.println(t.text());
//        }
//
//        Element rank = document.getElementById("rank_box");
//        System.out.println(rank.text());


        Scanner scanner = new Scanner(System.in);



        int i;
        do {
            System.out.println("Выбери операцию:");
            System.out.println("1-обработать файл");
            System.out.println("2-добавить новые слова в словарь");
            System.out.println("3-заполнить новые слова");
            System.out.println("4-обработка не найденных слов");
            System.out.println("5-добавить слова со страницы сайта");
            System.out.println("n-создать текст для обработки со страницы сайта");
            System.out.println("0-выход");
            i = scanner.nextInt();
            if (i == 0) {
                return;
            }
            if (i == 1) {
                new WordFreq().processingTetx();
            }
            if (i == 2) {
                new AddWords().addWords();
            }
            if (i == 3) {
                new FillingOfDict().fillingOfDict();
            }
            if (i == 4) {
                System.out.println("1-вывод всех слов для которых не найден перевод");
                System.out.println("2-НЕ ГОТОВО вывод слов для которых не найден перевод, пословно с обработкой");
                i = scanner.nextInt();
                if (i == 1) {
                    new WithoutTranslate().allWords();
                }
            }
            if (i == 5) {
                new GetWordsFromUrl().getWordsFromUrl();
            }
        } while (i < 10);


//        new FillingOfDict().fillingOfDict();
    }
}