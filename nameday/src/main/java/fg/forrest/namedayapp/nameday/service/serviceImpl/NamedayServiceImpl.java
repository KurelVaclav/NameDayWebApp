package fg.forrest.namedayapp.nameday.service.serviceImpl;

import fg.forrest.namedayapp.nameday.model.Nameday;
import fg.forrest.namedayapp.nameday.exception.FileParsingException;
import fg.forrest.namedayapp.nameday.service.NamedayService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class NamedayServiceImpl implements NamedayService {

    public List<Nameday> getNameday() throws IOException{
        LocalDate today = LocalDate.now();
        List<Nameday> todayNamedays;
        todayNamedays = loadNamedaysFromFile(today);
        return todayNamedays;
    }

    @Scheduled(fixedDelay = 10000)
    public List<Nameday> loadNamedaysFromFile(LocalDate date) throws IOException{
        List<Nameday> namedays = new ArrayList<>();
        String namedayFileContent;
        String[] namedayLines;
        File file = ResourceUtils.getFile("src/main/resources/namedays.txt"); //classpath:namedays.txt
        InputStream inputStream = Files.newInputStream(Path.of(file.getPath()));
//        InputStream inputStream = Files.newInputStream(Path.of("C:\\SpringsProject\\NameDayWebApp\\nameday\\src\\main\\resources\\namedays.txt"));
        try{
            namedayFileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            namedayLines = namedayFileContent.split("\\r?\\n");
            for (String namedayLine : namedayLines) {
                String[] parts = namedayLine.split(":");
                if (parts.length == 2) {
                    LocalDate namedayDate = LocalDate.parse(parts[1].trim());
                    if (namedayDate.getMonth() == date.getMonth() && namedayDate.getDayOfMonth() == date.getDayOfMonth()) {
                        String stringNameday = parts[0].trim();
                        namedays.add(new Nameday(namedayDate, stringNameday));
                    }
                }
            }
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            throw new FileParsingException("Error in parsing file - "+e.getMessage(), e);
        }
        return namedays;
    }

    @Override
    public List<Nameday> parseNamedays(String contents) {
        List<Nameday> namedays = new ArrayList<>();
        String[] lines = contents.split("\\r?\\n");
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                String dateString = parts[0].trim();
                String namedayString = parts[1].trim();
                LocalDate date = LocalDate.parse(dateString);
                namedays.add(new Nameday(date,namedayString));
            }
        }
        return namedays;
    }

    @Override
    public boolean validateNamedays(List<Nameday> namedays) {
        Set<LocalDate> uniqueDate = new HashSet<>();
        for (Nameday nameday : namedays) {
            if (!uniqueDate.add(nameday.getDate())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveNamedays(List<Nameday> namedays, MultipartFile file) {
        try {
            namedays.sort(Comparator.comparing(Nameday::getDate));
            StringBuilder stringBuilder = new StringBuilder();
            for (Nameday nameday : namedays) {
                stringBuilder.append(nameday.getNameday()).append(":").append(nameday.getDate()).append(System.lineSeparator());
            }
            Files.writeString(Path.of(file.getName()), stringBuilder.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
