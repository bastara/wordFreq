import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WordFreq {
    public void processingTetx() throws IOException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("c:/myProgDict/output.txt"));
//             BufferedWriter bw1 = new BufferedWriter(new FileWriter("c:/myProgDict/output1.txt"));
             BufferedReader br = new BufferedReader(new FileReader("c:/myProgDict/input.txt"))) {

            int length = 20;
            StringBuilder stringBuilder = new StringBuilder(length);
            HashMap<String, Integer> listWordsMap = new HashMap<>();
            int c;
            int count = 0;
//            int tmpCount = 1;
            while ((c = br.read()) != -1) {
//                bw1.write((char) c);
                if (c > 96 && c < 123) {
                    stringBuilder.append((char) c);
                } else if (c + 32 > 96 && c + 32 < 123) {
                    stringBuilder.append((char) (c + 32));
                } else if (stringBuilder.length() > 2) {
                    if (!listWordsMap.containsKey(stringBuilder.toString())) {
                        listWordsMap.put(stringBuilder.toString(), 0);
                    }
                    listWordsMap.put(stringBuilder.toString(), listWordsMap.get(stringBuilder.toString()) + 1);
                    count++;
                    length = stringBuilder.capacity();
                    stringBuilder.delete(0, length);
                } else {
                    length = stringBuilder.capacity();
                    stringBuilder.delete(0, length);
                }
            }

//            System.out.println((listWordsMap));
//            System.out.println(count);

            System.out.println(listWordsMap.size());


            String[][] words = new String[listWordsMap.size()][2];
            Set<Map.Entry<String, Integer>> set = listWordsMap.entrySet();
            c = 0;
            for (Map.Entry<String, Integer> el : set) {
                words[c][1] = String.valueOf(el.getValue());
                words[c][0] = el.getKey();
                c++;
            }

            getSortArray(words);
            for (int i = listWordsMap.size() - 1; i > -1; i--) {
//            for (int i = 0; i < listWordsMap.size(); i++) {
                System.out.println(words[i][0] + " " + words[i][1]);
                bw.write(words[i][0] + " " + words[i][1] + "\n");
            }
        }
    }

    public static String[][] getSortArray(String[][] array) {
        for (int i = 0; i < array.length - 1; i++) {
            boolean isArraySorted = true;
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (Integer.parseInt(array[j][1]) < Integer.parseInt(array[j + 1][1])) {
                    isArraySorted = false;
                    String tmp0 = array[j][0];
                    String tmp1 = array[j][1];
                    array[j][0] = array[j + 1][0];
                    array[j][1] = array[j + 1][1];
                    array[j + 1][0] = tmp0;
                    array[j + 1][1] = tmp1;
                }
            }
            if (isArraySorted) {
                return array;
            }
        }
        return array;
    }
}

