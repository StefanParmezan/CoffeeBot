package org.StefanParmezan.Services;

import java.util.Scanner;

public class FileReader {
    public static final FileReader instance = new FileReader();
    private FileReader(){

    }

    public static FileReader getInstance(){
        return instance;
    }

    public String readFile(String path){
        StringBuilder context = new StringBuilder();

        try(Scanner reader = new Scanner(path)){
            while(reader.hasNextLine()){
                context.append(reader.nextLine());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return context.toString();
    }
}
