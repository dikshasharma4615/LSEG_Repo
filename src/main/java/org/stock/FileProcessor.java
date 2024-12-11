package org.stock;

import org.apache.commons.csv.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileProcessor {

    private final DataSampler dataSampler = new DataSampler();
    private final OutlierClass outlierDetector = new OutlierClass();

    public void processFiles(String inputDir, String outputDir, int numFiles) {
        try {
            Files.createDirectories(Paths.get(outputDir));
            List<Path> csvFiles = Files.list(Paths.get(inputDir))
                    .filter(path -> path.toString().endsWith(".csv"))
                    .limit(numFiles)
                    .collect(Collectors.toList());

            if (csvFiles.isEmpty()) {
                throw new Exception("No CSV files found in the input directory.");
            }

            for (Path file : csvFiles) {
                System.out.println("Processing file: " + file.getFileName());
                List<Map<String, String>> sampledData = dataSampler.extractDataPoints(file.toString());
                List<Map<String, Object>> outliers = outlierDetector.identifyOutliers(sampledData);

                if (!outliers.isEmpty()) {
                    writeOutliersToCsv(file.getFileName().toString(), outputDir, outliers);
                } else {
                    System.out.println("No outliers found in file: " + file.getFileName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeOutliersToCsv(String fileName, String outputDir, List<Map<String, Object>> outliers) throws IOException {
        String outputFilePath = Paths.get(outputDir, "outliers_" + fileName).toString();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("Stock-ID", "Timestamp", "Stock Price", "Mean Price", "Deviation", "% Deviation"))) {

            for (Map<String, Object> outlier : outliers) {
                csvPrinter.printRecord(
                        outlier.get("Stock-ID"),
                        outlier.get("Timestamp"),
                        outlier.get("Stock Price"),
                        outlier.get("Mean Price"),
                        outlier.get("Deviation"),
                        outlier.get("% Deviation")
                );
            }
        }
    }
}
