package fg.forrest.namedayapp.nameday.service;

import fg.forrest.namedayapp.nameday.model.Nameday;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;

/**
 * Interface of NamedayService to handle business logic
 */
public interface NamedayService {

    public List<Nameday> getTodayNameday() throws IOException;

    public List<Nameday> parseNamedays(String contents);

    public boolean validateNamedays(List<Nameday> namedays);

    public boolean saveNamedays(List<Nameday> namedays, MultipartFile file);

    public void updateNamedayDatabase(List<Nameday> namedays);

}
