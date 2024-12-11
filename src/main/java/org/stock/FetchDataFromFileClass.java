package org.stock;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FetchDataFromFileClass {
    public List<Map<String, String>> extractDataPoints(String filePath) throws Exception {
        List<Map<String, String>> records = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            // Use CSVFormat with no header

            Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT
                    .withHeader()
                    .withSkipHeaderRecord(false)
                    .parse(reader);

            List<CSVRecord> dataList = new ArrayList<>();
            for (CSVRecord record : csvRecords) {
                dataList.add(record);

            }

            if (dataList.size() < 30) {
                throw new Exception("File has less than 30 rows: " + filePath);
            }

            int startIdx = new Random().nextInt(dataList.size() - 29);
            for (int i = startIdx; i < startIdx + 30; i++) {
                CSVRecord record = dataList.get(i);

                // Create a new map for each record and add it to the list
                Map<String, String> map = new LinkedHashMap<>();
                map.put("Index", String.valueOf(i));
                map.put("Value", record.get(2)); // Assuming column 2 exists in your data
                records.add(map);
            }
        }
        catch (IOException e) {
            throw new Exception("Error reading file: " + filePath, e);
        }
        System.out.println("records size "+records.size());
        return records;
    }
}
