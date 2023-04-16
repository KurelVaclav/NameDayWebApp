package fg.forrest.namedayapp.nameday;

import fg.forrest.namedayapp.nameday.logic.NamedayDatabase;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.io.IOException;

@Service
public class NamedayService {

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("namedays.txt").getFile()); //z resources/namedays.txt
    NamedayDatabase namedayDatabase = new NamedayDatabase(file);

    public List<Nameday> getNameday() throws IOException {
        return namedayDatabase.getNameday();
    }

    public List<Nameday> parseNamedays(String contents) {
        return namedayDatabase.parseNameday(contents);
    }

    public boolean validateNamedays(List<Nameday> namedays) {
        return namedayDatabase.validateNamedys(namedays);
    }

    public boolean saveNamedays(List<Nameday> namedays) {
        return namedayDatabase.saveToFile(namedays);
    }
}
