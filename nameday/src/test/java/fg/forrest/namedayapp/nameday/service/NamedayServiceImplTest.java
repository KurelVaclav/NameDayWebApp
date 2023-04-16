package fg.forrest.namedayapp.nameday.service;

import fg.forrest.namedayapp.nameday.model.Nameday;
import fg.forrest.namedayapp.nameday.service.serviceImpl.NamedayServiceImpl;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

/**
 *  Testing default file with namedays+dates in format nameda:yyyy-mm-dd\n
 */
class NamedayServiceImplTest {

    Nameday nameday;

    @Test
    void getNameday_parsingDB_ExceptionIfWrongFileFormat() throws IOException {
        //given
        NamedayService namedayService = new NamedayServiceImpl();
        List<Nameday> nameday = namedayService.getNameday();
    }
}