import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVToTextConverter {

    public void convert(String csv, String txt, int column) {
        convertCSVtoText(csv, txt, column);
        // convertCSVtoText("twitter_validation.csv", "twitter_validation.txt");
    }

    public static void convertCSVtoText(String csvFilePath, String outputTextFilePath, int column) {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath));
                FileWriter writer = new FileWriter(outputTextFilePath)) {

            String[] nextRecord;
            while (true) {
                try {
                    if ((nextRecord = reader.readNext()) == null)
                        break;

                    if (nextRecord.length >= 4) {
                        // Assuming the fourth column contains the text
                        String text = nextRecord[column - 1];
                        // String text = nextRecord[3];

                        // Write the text to the output text file
                        writer.write(text);
                        writer.write(System.lineSeparator()); // Add a newline after each entry
                    }
                } catch (CsvValidationException e) {
                    // Handle CSV validation exception (e.g., log or print an error message)
                    e.printStackTrace();
                }
            }

            System.out.println("Conversion complete.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
