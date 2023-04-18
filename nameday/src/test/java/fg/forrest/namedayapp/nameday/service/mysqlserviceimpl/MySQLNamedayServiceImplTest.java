package fg.forrest.namedayapp.nameday.service.mysqlserviceimpl;

import fg.forrest.namedayapp.nameday.model.Nameday;
import fg.forrest.namedayapp.nameday.repository.NamedayRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MySQLNamedayServiceImplTest {

    @Mock
    NamedayRepository namedayRepository;
    @InjectMocks
    MySQLNamedayServiceImpl mySQLNamedayService;

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void getNameday() throws Exception {
        List<Nameday> namedays = new ArrayList<>();
        namedays.add(new Nameday(LocalDate.now(), "Jan"));
        namedays.add(new Nameday(LocalDate.now(), "Petr"));
        when(namedayRepository.findAll()).thenReturn(namedays);
        List<Nameday> result = mySQLNamedayService.getNameday();
        Assertions.assertEquals(namedays.size(), result.size());
        Assertions.assertEquals(namedays.get(0).getDate(), result.get(0).getDate());
        Assertions.assertEquals(namedays.get(0).getNameday(), result.get(0).getNameday());
        Assertions.assertEquals(namedays.get(1).getDate(), result.get(1).getDate());
        Assertions.assertEquals(namedays.get(1).getNameday(), result.get(1).getNameday());
    }

    @Test
    void parseNamedays() {
    }

    @Test
    void validateNamedays() {
    }

    @Test
    void saveNamedays() {
    }
}