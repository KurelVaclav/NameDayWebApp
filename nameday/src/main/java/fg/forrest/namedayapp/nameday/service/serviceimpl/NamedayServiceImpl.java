package fg.forrest.namedayapp.nameday.service.serviceimpl;

import fg.forrest.namedayapp.nameday.model.Nameday;
import fg.forrest.namedayapp.nameday.exception.FileParsingException;
import fg.forrest.namedayapp.nameday.repository.NamedayRepository;
import fg.forrest.namedayapp.nameday.service.NamedayService;
import org.springframework.beans.factory.annotation.Autowired;
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
//@Qualifier("txtNamedayService")
public class NamedayServiceImpl implements NamedayService {

    private List<Nameday> namedays;

    private final String FILE_PATH = "src/main/resources/namedays.txt";

    @Autowired
    NamedayRepository namedayRepository;

    /**
     * Method to get today's nameday from MySQL database
     * in comment there is older code to get today's nameday from txt file
     * @return
     * @throws IOException
     */
    @Override
    public List<Nameday> getTodayNameday() throws IOException {
        LocalDate today = LocalDate.now();
        List<Nameday> todayNamedays;
        // finding in txt file
//        try {
//            todayNamedays = loadNamedaysFromFile(today);
//        } catch (NullPointerException  e) {
//            throw new FileParsingException("Today's nameday is null" + e.getMessage());
//        }
        // now finding in MySQL db
        todayNamedays = namedayRepository.findByDate(today);
        if (todayNamedays.isEmpty()){
            throw new FileParsingException("Today's nameday is null");
        }
        return todayNamedays;
    }

    /**
     * Method for loading namedays from file (used before working with MySQL database)
     * @param date
     * @return List<Nameday> namedays - loaded from file
     * @throws IOException
     */
    @Scheduled(fixedDelay = 10000)
    public List<Nameday> loadNamedaysFromFile(LocalDate date) throws IOException {
        List<Nameday> namedays = new ArrayList<>();
        String namedayFileContent;
        String[] namedayLines;
        File file = ResourceUtils.getFile(FILE_PATH); //classpath:namedays.txt
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
            if (namedays.isEmpty()) {
                throw new FileParsingException("Namedays are empty");
            }
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            throw new FileParsingException("Error in parsing file - " + e.getMessage(), e);
        }
        return namedays;
    }

    /**
     * Method to parse namedays during update txt DBfile
     * Looking for file format <nameday>:yyyy-mm-dd\n
     * @param contents - String with namedays and dates in format <nameday>:yyyy-mm-dd\n
     * @return list of namedays
     */
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
            if (namedays.isEmpty()) {
                throw new FileParsingException("Namedays are empty");
            }
        } catch (DateTimeParseException e) {
            throw new FileParsingException("Error in parsing file " + e.getMessage(), e);
        }
        return namedays;
    }

    /**
     * Method to validate namedays - checking for unique date
     * @param namedays - list of namedays and dates
     * @return true if dates are unique
     */
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

    /**
     * Method to update - save new file of namedays to local repository - path="src/main/resources/namedays.txt"
     * @param namedays - list of namedays and dates
     * @param file - file with namedays in format <nameday>:yyyy-mm-dd\n
     * @return true if file was saved successfully
     */
    @Override
    public boolean saveNamedays(List<Nameday> namedays, MultipartFile file) {
        try {
            namedays.sort(Comparator.comparing(Nameday::getDate));
            StringBuilder stringBuilder = new StringBuilder();
            for (Nameday nameday : namedays) {
                stringBuilder.append(nameday.getNameday()).append(":").append(nameday.getDate()).append(System.lineSeparator());
            }
            Path folderpath = Path.of(ResourceUtils.getFile(FILE_PATH).toURI());
            Files.writeString(folderpath, stringBuilder.toString().trim(), StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Method to update MySQL database with new namedays
     * @param namedays - list of namedays and dates
     */
    @Override
    public void updateNamedayDatabase(List<Nameday> namedays) {
        namedayRepository.deleteAll();
        namedayRepository.saveAll(namedays);
    }

}
