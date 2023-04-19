package fg.forrest.namedayapp.nameday.controller;

import fg.forrest.namedayapp.nameday.model.Nameday;
import fg.forrest.namedayapp.nameday.service.NamedayService;
import fg.forrest.namedayapp.nameday.exception.FileParsingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.IOException;

@RestController
@RequestMapping(path = "/")
public class NamedayController {

    private final NamedayService txtNamedayService;

    public NamedayController(NamedayService txtNamedayService){
        this.txtNamedayService = txtNamedayService;
    }

    //    private final NamedayService mysqlNamedayService;
//
//    public NamedayController(@Qualifier("txtNamedayService") NamedayService txtNamedayService,
//                             @Qualifier("mysqlNamedayService") NamedayService mysqlNamedayService) {
//        this.txtNamedayService = txtNamedayService;
//        this.mysqlNamedayService = mysqlNamedayService;
//    }


    // GET and POST for DB from txt file
    @GetMapping("/test")
    public List<Nameday> getTodayNameday() throws IOException {
        return txtNamedayService.getTodayNameday();
    }

    //TODO check for update and update GET
    @PostMapping("/update")
    public ResponseEntity<String> updateNamedays(@RequestParam("file") @NotNull MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (!filename.endsWith(".txt")) {
            return ResponseEntity.badRequest().body("Unsupported file format, must be .txt");
        }
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try {
            String contents = new String(file.getBytes(), StandardCharsets.UTF_8);
            List<Nameday> namedays = txtNamedayService.parseNamedays(contents);
            if (!txtNamedayService.validateNamedays(namedays)) {
                return ResponseEntity.badRequest().body("Duplicate dates for the same data");
            }
            if (txtNamedayService.saveNamedays(namedays, file)) {
                txtNamedayService.updateNamedayDatabase(namedays);
                return ResponseEntity.ok().body("Nameday file and database successfully updated");

            } else {
                return ResponseEntity.badRequest().body("Failed to update nameday file and db");
            }
        } catch (FileParsingException e) {
            return ResponseEntity.badRequest().body("Invalid nameday file format: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update nameday file" + e.getMessage());
        }
    }

    @ExceptionHandler(FileParsingException.class)
    public ResponseEntity<String> handleFileParsingException(FileParsingException fileParsingException) {
        return ResponseEntity.badRequest().body("Error in parsing file - " + fileParsingException.getMessage());
    }

//    // GET and POST for DB from MySQL
//    @GetMapping("/testDB")
//    public List<Nameday> getNamedayDB() throws IOException {
//        return mysqlNamedayService.getTodayNameday();
//    }
//
//    @PostMapping("/updateDB")
//    public ResponseEntity<String> updateNamedaysDB(@RequestBody List<Nameday> namedays) {
//        mysqlNamedayService.saveNamedays(namedays, null);
//        return ResponseEntity.ok().body("Succesfully uploaded to MySQL database");
//    }

}
