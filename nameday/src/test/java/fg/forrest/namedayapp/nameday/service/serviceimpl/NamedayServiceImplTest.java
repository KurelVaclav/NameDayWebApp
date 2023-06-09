package fg.forrest.namedayapp.nameday.service.serviceimpl;

import fg.forrest.namedayapp.nameday.model.Nameday;
import fg.forrest.namedayapp.nameday.repository.NamedayRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class NamedayServiceImplTest {


    private NamedayServiceImpl namedayServiceImpl = new NamedayServiceImpl();

    @Mock
    NamedayRepository namedayRepository;
    @InjectMocks
    NamedayServiceImpl namedayService;

    /**
     * OLD
     * Testing getTodayNameday method for default txt DBfile with namedays+dates
     * You have to fill expected data according to file data and today's date
     * in format: <nameday>:yyyy-mm-dd\n
     */
//    @Test
//    void getTodayNameday_parsingDBFile_TodayDateAndNameday() throws IOException {
//        List<Nameday> nameday = namedayService.getTodayNameday();
//        assertEquals("[{\"date\":\"2023-04-19\",\"nameday\":\"Rostislav\"}]", nameday.get(0).toJSONString());
//    }

    /**
     * Testing  getTodayNameday method for MySQL DBfile with namedays+dates using Mockito
     * You have to fill expected data (expectedNamedays) according to file data and today's date
     */
    @Test
    public void gtestGetTodayNameday_MockitoRepo_Equals() throws Exception {
        LocalDate today = LocalDate.now();
        List<Nameday> expectedNamedays = new ArrayList<>();
        expectedNamedays.add(new Nameday(today, "Marcela"));
        Mockito.when(namedayRepository.findByDate(today)).thenReturn(expectedNamedays);
        List<Nameday> actualNamedays = namedayService.getTodayNameday();
        assertEquals(actualNamedays, expectedNamedays);
        Mockito.verify(namedayRepository).findByDate(today);
    }


    /**
     * Testing loadNamedaysFromFile method for loading namedays from default txt DB file
     */
    @Test
    void loadNamedaysFromFile_SimpleValues_Parsed() throws IOException {
        List<Nameday> namedays = namedayServiceImpl.loadNamedaysFromFile(LocalDate.of(2023, 2, 14));
        assertEquals(1, namedays.size());
        assertEquals("Valentýn", namedays.get(0).getNameday());
    }

    /**
     * Testing parseNamedays method for parsing right simple value
     */
    @Test
    void parseNamedays_SimpleValues_Parsed() {
        String contents = "Vincenc:2023-04-14\nAnastázie:2023-04-15\n";
        List<Nameday> namedays = namedayServiceImpl.parseNamedays(contents);
        assertEquals(namedays.size(), 2);
        Nameday firstNameday = namedays.get(0);
        assertEquals(firstNameday.getDate(), LocalDate.of(2023, 4, 14));
        assertEquals(firstNameday.getNameday(), "Vincenc");
    }

    /**
     * Testing validateNamedays method for unique dates - unnique => true
     */
    @Test
    void validateNamedays_WithUniqueDates_True() {
        //nameday list with unique dates
        List<Nameday> namedays = new ArrayList<>();
        namedays.add(new Nameday(LocalDate.of(2023, 4, 14), "Vincenc"));
        namedays.add(new Nameday(LocalDate.of(2023, 4, 15), "Anastázie"));
        namedays.add(new Nameday(LocalDate.of(2023, 4, 16), "Irena"));
        assertTrue(namedayServiceImpl.validateNamedays(namedays));
    }

    /**
     * Testing validateNamedays method for unique dates - duplicate => false
     */
    @Test
    public void validateNamedays_WithDuplicateDates_False() {
        //nameday list with duplicate dates
        List<Nameday> namedays = new ArrayList<>();
        namedays.add(new Nameday(LocalDate.of(2023, 4, 14), "Vincenc"));
        namedays.add(new Nameday(LocalDate.of(2023, 4, 14), "Vincenc"));
        namedays.add(new Nameday(LocalDate.of(2023, 4, 16), "Irena"));
        assertFalse(namedayServiceImpl.validateNamedays(namedays));
    }

    /**
     * Testing saveNamedays method for correct .txt file, expected true
     * More testing is in NamedayControllerUpdateTest
     */
    @Test
    void saveNamedays_CorrectFile_True() throws IOException {
        List<Nameday> namedays = new ArrayList<>();
        namedays.add(new Nameday(LocalDate.of(2023, 1, 1), "Nový_rok"));
        namedays.add(new Nameday(LocalDate.of(2023, 2, 14), "Valentýn"));
        namedays.add(new Nameday(LocalDate.of(2023, 3, 17), "Vlastimil"));
        //mock MultipartFile object with a name
        MultipartFile file = mock(MultipartFile.class);
        when(file.getName()).thenReturn("namedays.txt");
        boolean result = namedayServiceImpl.saveNamedays(namedays, file);
        String contents = Files.readString(Path.of(file.getName()));
        assertEquals("Nový_rok:2022-01-01\r\n" + "Valentýn:2022-02-14\r\n" + "Vlastimil:2022-03-17", contents.trim());
        assertTrue(result);
    }


}