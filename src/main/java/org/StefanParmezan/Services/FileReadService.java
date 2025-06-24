package org.StefanParmezan.Services;

import java.io.File;
import java.util.Scanner;

public class FileReadService {
    public static final FileReadService instance = new FileReadService();
    private FileReadService(){

    }

    public static FileReadService getInstance(){
        return instance;
    }

    public String readFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return content.toString().trim();
    }
}
