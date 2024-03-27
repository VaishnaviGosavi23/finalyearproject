import java.io.*;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class KaggleApiDownloader {
    public void Api(String datasets) {
        try {
            // Replace with your actual Kaggle credentials
            String kaggleApiKey = "9e80cdeeb16c8ee34f17a86b264d004f";
            String kaggleUsername = "silent833";
            String dataset = datasets;
            // String dataset = "jp797498e/twitter-entity-sentiment-analysis";
            String outputPath = "C:\\Users\\vishu\\OneDrive\\Desktop\\Sentiment-Analysis--PS4\\Sentiment-Analysis--PS4\\Sentiment-Analysis--PS4\\PS4";

            // Build the command, ensuring correct path to kaggle.exe
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "C:\\Users\\vishu\\AppData\\Local\\Programs\\Python\\Python312\\Scripts\\kaggle.exe", // Verify path
                                                                                                          // to
                                                                                                          // kaggle.exe
                    "datasets", "download", "-p", outputPath, dataset);

            // Set Kaggle credentials securely as environment variables
            processBuilder.environment().put("KAGGLE_USERNAME", kaggleUsername);
            processBuilder.environment().put("KAGGLE_KEY", kaggleApiKey);

            // Run the command and capture output/error streams
            Process process = processBuilder.start();
            try (Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\\A");
                    Scanner errorScanner = new Scanner(process.getErrorStream()).useDelimiter("\\A")) {

                // Print output and error streams for debugging
                System.out.println("Output: " + (scanner.hasNext() ? scanner.next() : ""));
                // System.err.println("Error: " + (errorScanner.hasNext() ? errorScanner.next()
                // : ""));
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Download complete.");

                // Extract the downloaded ZIP file
                String zipFilePath = outputPath + File.separator + dataset.split("/")[1] + ".zip";
                String extractionPath = outputPath;

                extractZipFile(zipFilePath, extractionPath);
            } else {
                System.err.println("Download failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void extractZipFile(String zipFilePath, String extractionPath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                File entryFile = new File(extractionPath, entryName);

                if (entry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    File parent = entryFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }

                    // Extract the file
                    try (BufferedOutputStream outputStream = new BufferedOutputStream(
                            new FileOutputStream(entryFile))) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }

                zipInputStream.closeEntry();
            }
            System.out.println("Extraction complete.");
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }
}
