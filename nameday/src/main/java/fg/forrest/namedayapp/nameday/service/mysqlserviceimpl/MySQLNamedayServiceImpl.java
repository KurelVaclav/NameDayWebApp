package fg.forrest.namedayapp.nameday.service.mysqlserviceimpl;

import fg.forrest.namedayapp.nameday.model.Nameday;
import fg.forrest.namedayapp.nameday.repository.NamedayRepository;
import fg.forrest.namedayapp.nameday.service.NamedayService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Qualifier("mysqlNamedayService")
public class MySQLNamedayServiceImpl implements NamedayService {

    private final NamedayRepository namedayRepository;

    //TODO comunicate with MySQL

    @Autowired
    public MySQLNamedayServiceImpl(NamedayRepository namedayRepository) {
        this.namedayRepository = namedayRepository;
    }

    @Override
    public List<Nameday> getNameday() throws IOException {
        LocalDate today = LocalDate.now();
        return namedayRepository.findByDate(today);
    }

    @Override
    public List<Nameday> parseNamedays(String contents) {
        return null;
    }

    @Override
    public boolean validateNamedays(List<Nameday> namedays) {
        return false;
    }

    @Override
    public boolean saveNamedays(List<Nameday> namedays, @Nullable MultipartFile file) {
        try {
            namedayRepository.saveAll(namedays);
            return true;
        } catch (Throwable e){
            return false;
        }
    }

}
