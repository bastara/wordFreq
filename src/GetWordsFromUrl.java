import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class GetWordsFromUrl {
    void getWordsFromUrl() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Веедите URL сайта");
        String url = scanner.nextLine();

//        String url = "https://www.really-learn-english.com/english-short-stories-level-05-story-01.html";

        Document document = Jsoup.connect(url).get();

        String allText = document.text();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("c:/myProgDict/input.txt"))) {
            bw.write(allText);
        }

        new WordFreq().processingTetx();
    }
}
