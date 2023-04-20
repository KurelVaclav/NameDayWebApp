package fg.forrest.namedayapp.nameday.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Testing NamedayController for POST request on uri /update
 * posting new DBfile with namedays
 */
@SpringBootTest
public class NamedayControllerUpdateTest {

    @Autowired
    private NamedayController namedayController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(namedayController).build();
    }

    /**
     * Testing updateNamdays for empty .txt file - bad request
     */
    @Test
    public void testUpdateNamedays_EmptyTxtFile_BadRequest() throws Exception {
        // create a mock file
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test data".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/update").file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid nameday file format: Namedays are empty"));
    }

    /**
     * Testing updateNamdays for .txt file with wrong date data fromat - bad request
     */
    @Test
    public void testUpdateNamedays_TxtFileWithWrongDate_BadRequest() throws Exception {
        String content = "Karina:2023-01-02\n" +
                "Radmila:2023-01-03\n" +
                "Diana:2023-01.04";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/update").file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid nameday file format: Error in parsing file Text '2023-01.04' could not be parsed at index 7"));
    }

    /**
     * Testing updateNamdays for .txt file with wrong fromat data (namedays are null) - bad request
     */
    @Test
    public void testUpdateNamedays_TxtFileWithWrongFormat_BadRequest() throws Exception {
        String content = "asfas\n" + "asfas\n" + "fgg";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/update").file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid nameday file format: Namedays are empty"));
    }

    /**
     * Testing updateNamdays for .dat file (unsupported file fromat) - bad request
     */
    @Test
    public void testUpdateNamedays_FileInWrongFormat_BadRequest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.dat", "application/octet-stream","test data".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/update").file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Unsupported file format, must be .txt"));
    }
}

