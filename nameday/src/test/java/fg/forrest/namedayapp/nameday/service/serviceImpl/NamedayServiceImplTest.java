package fg.forrest.namedayapp.nameday.service.serviceImpl;

import fg.forrest.namedayapp.nameday.model.Nameday;
import org.junit.jupiter.api.Test;
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

class NamedayServiceImplTest {

    NamedayServiceImpl namedayService = new NamedayServiceImpl();

    /**
     * Testing default txt DBfile with namedays+dates
     * in format:
     * <nameday>:yyyy-mm-dd\n
     *
     * @throws IOException
     */
    @Test
    void getNameday_parsingDBFile_TodayDateAndNameday() throws IOException {
        List<Nameday> nameday = namedayService.getNameday();
        assertEquals("[{\"date\":\"2023-04-16\",\"nameday\":\"Irena\"}]", nameday.get(0).toJSONString());
    }

    /**
     * Testing loading namedays from default txt DB file
     *
     * @throws IOException
     */
    @Test
    void loadNamedaysFromFile_SimpleValues_Parsed() throws IOException {
        List<Nameday> namedays = namedayService.loadNamedaysFromFile(LocalDate.of(2023, 4, 14));
        assertEquals(1, namedays.size());
        assertEquals("Vincenc", namedays.get(0).getNameday());
    }

    @Test
    void parseNamedays_SimpleValues_Parsed() {
        String contents = "Vincenc:2023-04-14\nAnastázie:2023-04-15\n";
        List<Nameday> namedays = namedayService.parseNamedays(contents);
        assertEquals(namedays.size(), 2);
        Nameday firstNameday = namedays.get(0);
        assertEquals(firstNameday.getDate(), LocalDate.of(2023, 4, 14));
        assertEquals(firstNameday.getNameday(), "Vincenc");
    }

    @Test
    void validateNamedays_WithUniqueDates_True() {
        //nameday list with unique dates
        List<Nameday> namedays = new ArrayList<>();
        namedays.add(new Nameday(LocalDate.of(2023, 4, 14), "Vincenc"));
        namedays.add(new Nameday(LocalDate.of(2023, 4, 15), "Anastázie"));
        namedays.add(new Nameday(LocalDate.of(2023, 4, 16), "Irena"));
        assertTrue(namedayService.validateNamedays(namedays));
    }

    @Test
    public void validateNamedays_WithDuplicateDates_False() {
        //nameday list with duplicate dates
        List<Nameday> namedays = new ArrayList<>();
        namedays.add(new Nameday(LocalDate.of(2023, 4, 14), "Vincenc"));
        namedays.add(new Nameday(LocalDate.of(2023, 4, 14), "Vincenc"));
        namedays.add(new Nameday(LocalDate.of(2023, 4, 16), "Irena"));
        assertFalse(namedayService.validateNamedays(namedays));
    }

    @Test
    void saveNamedays_CorrectFile_True() throws IOException {
        List<Nameday> namedays = new ArrayList<>();
        namedays.add(new Nameday(LocalDate.of(2022, 1, 1), "Nový_rok"));
        namedays.add(new Nameday(LocalDate.of(2022, 2, 14), "Valentýn"));
        namedays.add(new Nameday(LocalDate.of(2022, 3, 17), "Vlastimil"));
        //mock MultipartFile object with a name
        MultipartFile file = mock(MultipartFile.class);
        when(file.getName()).thenReturn("namedays.txt");
        boolean result = namedayService.saveNamedays(namedays, file);
        String contents = Files.readString(Path.of(file.getName()));
        assertEquals("Nový_rok:2022-01-01\r\n" + "Valentýn:2022-02-14\r\n" + "Vlastimil:2022-03-17", contents.trim());
        assertTrue(result);
    }
}