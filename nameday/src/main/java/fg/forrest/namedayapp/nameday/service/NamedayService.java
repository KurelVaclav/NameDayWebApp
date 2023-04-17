package fg.forrest.namedayapp.nameday.service;

import fg.forrest.namedayapp.nameday.exception.FileParsingException;
import fg.forrest.namedayapp.nameday.model.Nameday;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.List;
import java.io.IOException;

public interface NamedayService {

    public List<Nameday> getNameday() throws IOException;

    public List<Nameday> parseNamedays(String contents);

    public boolean validateNamedays(List<Nameday> namedays);

    public boolean saveNamedays(List<Nameday> namedays, MultipartFile file);

}
