package fg.forrest.namedayapp.nameday.namedaysgenerator;

import fg.forrest.namedayapp.nameday.model.Nameday;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * NamedaysGenerator is a class to make default txt file with namedays database
 * you run it in main
 * generate txt file with namedays and date in format
 * <nameday>:yyyy-mm-dd\n
 * generate txt file with MySQL queries to insert default namedays to database
 */
public class NamedaysGenerator {

    static File file = new File("C:\\SpringsProject\\NameDayWebApp\\nameday\\data\\newnds.txt");
    static final Locale LOCALE = new Locale("cs","CZ");

    /**
     * Txt file generation with nameday db in format <nameday>:yyyy-mm-dd\n
     */
    public static void generateNamedaysToFile() {
        StringBuilder stringBuilder = new StringBuilder();
        Locale locale = LOCALE;
        List<String> names = modifyNamedaysFile();
        for (int i = 1; i <= 365; i++) {
            LocalDate date = LocalDate.ofYearDay(2020, i);
            String nameday = names.get(i-1);
            stringBuilder.append(nameday).append(":").append(date).append(System.lineSeparator());
        }
        try {
            Files.write(Path.of(file.getPath()), stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Failed to write namedays file" + e.getMessage());
        }
    }


    public static List<String> modifyNamedaysFile() {
        List<String> names = new ArrayList<>();
        try {
            File file = new File("C:\\SpringsProject\\NameDayWebApp\\nameday\\data\\newnamesitnetwork.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            Scanner scanner = new Scanner(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            scanner.useLocale(LOCALE);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] words = line.split(" ");
                String lastWord = words[words.length - 1];
                names.add(lastWord);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return names;
    }

//    public static void main(String[] args) {
//        NamedaysGenerator namedaysGenerator = new NamedaysGenerator();
//        namedaysGenerator.generateNamedaysToFile();
//        List<String> names = namedaysGenerator.modifyNamedaysFile();
//        System.out.println(names.toString());
//    }

    /**
     * txt file generation with MySQL queries to build nameday database
     */
    public static List<Nameday> getNamedaysFromTxtFile() throws IOException {
        List<Nameday> namedays = new ArrayList<>();
        String content;
        String[] lines;
        String[] parts;
        InputStream inputStream = new FileInputStream(file);
        content = new String(inputStream.readAllBytes(),StandardCharsets.UTF_8);
        lines = content.split("\\r?\\n");
        for(String line : lines){
            parts = line.split(":");
            if(parts.length == 2){
                LocalDate date = LocalDate.parse(parts[1].trim());
                String nameday = parts[0].trim();
                namedays.add(new Nameday(date, nameday));
            }
        }
        return namedays;
    }

    public static String generateQueries(List<Nameday> namedays) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String query = "INSERT INTO nameday (nameday, date) VALUES ";
        stringBuilder.append(query);
        for(Nameday nameday : namedays){
            String name = nameday.getNameday();
            String date = nameday.getDate().toString();
            stringBuilder.append("(").append("\"").append(name).append("\"").append(",").append("\"").append(date).append("\"").append(")").append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        File file = new File("C:\\SpringsProject\\NameDayWebApp\\nameday\\data\\MySQL_queryINSERTNameday.txt");
        Files.write(Path.of(file.getPath()), stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws IOException {
        NamedaysGenerator namedaysGenerator = new NamedaysGenerator();
        List<Nameday> namedays = namedaysGenerator.getNamedaysFromTxtFile();
        System.out.println(namedays.toString());
        String query = namedaysGenerator.generateQueries(namedays);
        System.out.println(query);

    }


}
