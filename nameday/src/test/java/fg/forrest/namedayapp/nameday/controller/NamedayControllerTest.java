package fg.forrest.namedayapp.nameday.controller;

import fg.forrest.namedayapp.nameday.exception.FileParsingException;
import fg.forrest.namedayapp.nameday.model.Nameday;
import fg.forrest.namedayapp.nameday.service.NamedayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NamedayControllerTest {

    @Mock
    private NamedayService namedayService;

    @InjectMocks
    private NamedayController namedayController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Testing getNameday method for communication between namedayService and namedayController
     * Expected right communication between controller and service
     */
    @Test
    public void getNameday_ActualDayToday_RightCommunication() throws IOException {
        List<Nameday> expectedNamedays = new ArrayList<>();
        expectedNamedays.add(new Nameday(LocalDate.now(),"Rudolf"));
        when(namedayService.getNameday()).thenReturn(expectedNamedays);
        List<Nameday> actualNamedays = namedayController.getNameday();
        verify(namedayService, times(1)).getNameday();
        assertEquals(expectedNamedays, actualNamedays);
    }

    /**
     * Testing updateNamedys to correct file with correct data, expected OK status and update new file
     */
    @Test
    public void updateNamedays_CorrectFile_OK() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain",
                "Mary:2023-04-16\nGeorge:2023-04-17\nJohn:2023-04-18".getBytes(StandardCharsets.UTF_8));

        List<Nameday> namedays = new ArrayList<>();
        namedays.add(new Nameday(LocalDate.now(),"George"));

        when(namedayService.parseNamedays(anyString())).thenReturn(namedays);
        when(namedayService.validateNamedays(namedays)).thenReturn(true);
        when(namedayService.saveNamedays(namedays, file)).thenReturn(true);

        ResponseEntity<String> responseEntity = namedayController.updateNamedays(file);

        verify(namedayService, times(1)).parseNamedays(anyString());
        verify(namedayService, times(1)).validateNamedays(namedays);
        verify(namedayService, times(1)).saveNamedays(namedays, file);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Nameday file successfully updated", responseEntity.getBody());
    }
}

