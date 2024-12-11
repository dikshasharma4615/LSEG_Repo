package org.stock;

import java.util.*;
import java.util.stream.Collectors;

public class OutlierClass {

    public List<Map<String, Object>> identifyOutliers(List<Map<String, String>> sampledData) {
        List<Double> prices = new ArrayList<>();
        for (Map<String, String> map : sampledData) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    try {
                        prices.add(Double.parseDouble(value));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format: " + value);
                        prices.add(0.0);
                    }
                } else {
                    System.err.println("Null value encountered");
                    prices.add(0.0);
                }
            }
        }
        System.out.println("Prices size: " + prices.size()+"---");
        System.out.println("SampledData size: " + sampledData.size());

            double mean = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double stdDev = Math.sqrt(prices.stream()
                    .mapToDouble(p -> Math.pow(p - mean, 2))
                    .average().orElse(0.0));
        double positiveThreshold = mean + 2 * stdDev;
        double negativeThreshold = mean - 2 * stdDev;

            List<Map<String, Object>> outliers = new ArrayList<>();
            for (int i = 0; i < prices.size(); i++) {
                double price = prices.get(i);
                if (price > positiveThreshold || price < negativeThreshold) {
                    Map<String, Object> outlier = new HashMap<>(sampledData.get(0));
                    outlier.put("Mean Price", mean);
                    outlier.put("Deviation", price - mean);
                    outlier.put("% Deviation", ((price - mean) / mean) * 100);
                    outliers.add(outlier);
                }
            }


            return outliers;      }

}
