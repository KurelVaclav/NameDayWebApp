package fg.forrest.namedayapp.nameday.logic;

import fg.forrest.namedayapp.nameday.Nameday;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class NamedayDatabaseTest {

    Nameday nameday;
    @Test
    void getNameday_parsingDB_ExceptionIfWrongFileFormat(){
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("namedays.txt").getFile());
//        File file = new File("classpath:namedays.txt");
        //given
        NamedayDatabase namedayDatabase = new NamedayDatabase(file);
        List<Nameday> nameday = namedayDatabase.getNameday();

    }
}