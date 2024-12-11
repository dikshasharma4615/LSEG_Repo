package org.stock;

public class Main {
    public static void main(String[] args) {
        FileProcessor processor = new FileProcessor();
        processor.processFiles("src/main/resources", "src/main/output", 2);
    }
}
