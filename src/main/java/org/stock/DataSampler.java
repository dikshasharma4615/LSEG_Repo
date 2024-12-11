package org.stock;

import org.apache.commons.csv.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DataSampler {

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
            LinkedHashMap<String,String> map=new LinkedHashMap<>();
                int j=0;
                for(int i=startIdx;i<startIdx+30;i++){
                    CSVRecord record = dataList.get(i);
                    map.put(String.valueOf(i),record.get(2));
                }
            System.out.println(map);
                records.add(map);
            }
        catch (IOException e) {
            throw new Exception("Error reading file: " + filePath, e);
        }

        return records;
    }
}
