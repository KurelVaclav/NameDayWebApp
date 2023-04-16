package fg.forrest.namedayapp.nameday.logic;

import fg.forrest.namedayapp.nameday.Nameday;
import fg.forrest.namedayapp.nameday.exception.FileParsingException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class NamedayDatabase {
    //    private File file = new File(System.getProperty("user.dir") + File.separator + "data/test.txt");
    private File file;

    public NamedayDatabase(File file) {
        this.file = file;
    }

    public List<Nameday> getNameday() {
        LocalDate today = LocalDate.now();
        List<Nameday> todayNamedays;
        todayNamedays = loadNamedaysFromFile(today);
        return todayNamedays;
    }

    public List<Nameday> loadNamedaysFromFile(LocalDate date) {
        List<Nameday> namedays = new ArrayList<>();
        String namedayFileContent;
        String[] namedayLines;
        try {
            namedayFileContent = Files.readString(Paths.get(file.getPath()), StandardCharsets.UTF_8);
            namedayLines = namedayFileContent.split("\\r?\\n");
            for (String namedayLine : namedayLines) {
                String[] parts = namedayLine.split(":");
                if (parts.length == 2) {
                    LocalDate namedayDate = LocalDate.parse(parts[1].trim());
                    if (namedayDate.getMonth() == date.getMonth() && namedayDate.getDayOfMonth() == date.getDayOfMonth()) {
                        String stringNameday = parts[0].trim();
                        Nameday nameday = new Nameday(namedayDate, stringNameday);
                        namedays.add(nameday);
                    }
                }
            }
        } catch (IOException | DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            throw new FileParsingException("Error in parsing", e);
        }
        return namedays;
    }


    public List<Nameday> parseNameday(String contents) {
        List<Nameday> namedays = new ArrayList<>();
        String[] lines = contents.split("\\r?\\n");
        for(String line : lines){
            String[] parts = line.split(":");
            if (parts.length == 2){
                String dateString = parts[0].trim();
                String namedayString = parts[1].trim();
                LocalDate date = LocalDate.parse(dateString);
                for (Nameday nameday: namedays){
                    namedays.add(new Nameday());
                }
            }
        }
        return namedays;
    }

    public boolean validateNamedys(List<Nameday> namedays) {
        Set<LocalDate> uniqueDate = new HashSet<>();
        for(Nameday nameday : namedays){
            if(!uniqueDate.add(nameday.getDate())){
                return false;
            }
        }
        return true;
    }

    public boolean saveToFile(List<Nameday> namedays) {
        try{
            namedays.sort(Comparator.comparing(Nameday::getDate));
            StringBuilder stringBuilder = new StringBuilder();
            for(Nameday nameday : namedays){
                stringBuilder.append(nameday.getNameday()).append(":").append(nameday.getDate()).append(System.lineSeparator());

            }
            Files.write(Path.of(file.getPath()),stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e){
            return false;
        }
    }
}
