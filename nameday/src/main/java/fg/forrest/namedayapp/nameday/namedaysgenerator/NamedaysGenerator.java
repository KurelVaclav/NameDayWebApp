package fg.forrest.namedayapp.nameday.namedaysgenerator;

import org.springframework.cglib.core.Local;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class NamedaysGenerator {

    static File file = new File("C:\\SpringsProject\\NameDayWebApp\\nameday\\data\\nds.txt");
    static final Locale LOCALE = new Locale("cs","CZ");

    public static void generateNamedaysToFile() {
        StringBuilder stringBuilder = new StringBuilder();
        Locale locale = LOCALE;
        List<String> names = modifyNamedaysFile();
        for (int i = 1; i <= 365; i++) {
            LocalDate date = LocalDate.ofYearDay(2023, i);
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
            File file = new File("C:\\SpringsProject\\NameDayWebApp\\nameday\\data\\namesitnetwork.txt");
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

}
