import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Preprocessing {

    public ArrayList<ArrayList<String>> positiveList;
    public ArrayList<ArrayList<String>> negativeList;
    public ArrayList<ArrayList<String>> neutralList;

    public ArrayList<ArrayList<String>> posOutput = new ArrayList<>();
    public ArrayList<ArrayList<String>> negOutput = new ArrayList<>();
    public ArrayList<ArrayList<String>> neutralOutput = new ArrayList<>();

    private Set<String> stopwords;

    public Preprocessing() throws IOException {
        stopwords = readStopwords(new File("stopwords.txt"));
    }

    private Set<String> readStopwords(File stopwordsFile) throws IOException {
        Set<String> stopwordsSet = new HashSet<>();
        BufferedReader br = new BufferedReader(new FileReader(stopwordsFile));
        String stopword;

        while ((stopword = br.readLine()) != null) {
            stopwordsSet.add(stopword.trim());
        }

        br.close();

        return stopwordsSet;
    }

    public ArrayList<ArrayList<String>> readFile(File f) throws Exception {
        ArrayList<ArrayList<String>> words = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(f));
        String rec;

        while ((rec = br.readLine()) != null) {
            ArrayList<String> data = new ArrayList<>();
            String[] line = rec.split(" ");
            for (String word : line)
                if (!stopwords.contains(word)) {
                    data.add(word);
                }
            words.add(data);
        }
        System.out.println("*************");
        System.out.println(words);
        System.out.println("*************");
        br.close();

        return words;
    }

    public void tfidf(ArrayList<ArrayList<String>> words, int emotion) throws Exception {
        double threshold = 0.3;
        TFIDF tfidf = new TFIDF();

        for (ArrayList<String> list : words) {
            ArrayList<String> passed = new ArrayList<>();

            for (String w : list) {
                if (!stopwords.contains(w)) { // Check if the word is not a stopword
                    double weight = tfidf.tfidf(words, w);

                    if (threshold > weight && !w.equals(""))
                        passed.add(w);
                }
            }

            if (!passed.isEmpty() && emotion == 1) {
                posOutput.add(passed);
            }
            if (!passed.isEmpty() && emotion == -1) {
                negOutput.add(passed);
            }
            if (!passed.isEmpty() && emotion == 0) {
                neutralOutput.add(passed);
            }
        }
    }

    public void loadFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("posOutput.txt"));
        BufferedWriter bw1 = new BufferedWriter(new FileWriter("negOutput.txt"));
        BufferedWriter bw2 = new BufferedWriter(new FileWriter("neutralOutput.txt"));

        for (ArrayList<String> words : posOutput) {
            for (String w : words) {
                bw.write(w + " ");
            }
            bw.newLine();
        }
        for (ArrayList<String> words : negOutput) {
            for (String w : words) {
                bw1.write(w + " ");
            }
            bw1.newLine();
        }
        for (ArrayList<String> words : neutralOutput) {
            for (String w : words) {
                bw2.write(w + " ");
            }
            bw2.newLine();
        }
        bw.flush();
        bw.close();

        bw1.flush();
        bw1.close();

        bw2.flush();
        bw2.close();
    }
}
