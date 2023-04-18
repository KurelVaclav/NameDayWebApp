package fg.forrest.namedayapp.nameday.service.txtserviceimpl;

import fg.forrest.namedayapp.nameday.model.Nameday;
import fg.forrest.namedayapp.nameday.exception.FileParsingException;
import fg.forrest.namedayapp.nameday.service.NamedayService;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Qualifier("txtNamedayService")
public class TxtNamedayServiceImpl implements NamedayService {


    public List<Nameday> getNameday() throws IOException {
        LocalDate today = LocalDate.now();
        List<Nameday> todayNamedays;
        try {
            todayNamedays = loadNamedaysFromFile(today);
        }catch (FileParsingException e){
            throw new FileParsingException("Today's nameday is null");
        }
        return todayNamedays;
    }

    @Scheduled(fixedDelay = 10000)
    public List<Nameday> loadNamedaysFromFile(LocalDate date) throws IOException {
        List<Nameday> namedays = new ArrayList<>();
        String namedayFileContent;
        String[] namedayLines;
        File file = ResourceUtils.getFile("src/main/resources/namedays.txt"); //classpath:namedays.txt
        InputStream inputStream = Files.newInputStream(Path.of(file.getPath()));
        try {
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
            if(namedays.isEmpty()){
                throw new FileParsingException("Namedays are empty");
            }
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            throw new FileParsingException("Error in parsing file - " + e.getMessage(), e);
        }
        return namedays;
    }

    @Override
    public List<Nameday> parseNamedays(String contents) {
        List<Nameday> namedays = new ArrayList<>();
        try {
            String[] lines = contents.split("\\r?\\n");
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String namedayString = parts[0].trim();
                    String dateString = parts[1].trim();
                    LocalDate date = LocalDate.parse(dateString);
                    namedays.add(new Nameday(date, namedayString));
                }
            }
            if (namedays.isEmpty()){
                throw new FileParsingException("Namedays are empty");
            }
        }catch (DateTimeParseException e){
            throw new FileParsingException("Error in parsing file " + e.getMessage(),e);
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
            Path folderpath = Path.of(ResourceUtils.getFile("src/main/resources/namedays.txt").toURI());
//            Path path = Paths.get(folderpath + file.getOriginalFilename()); //Path.of(file.getName())
            Files.write(folderpath, stringBuilder.toString().trim().getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
