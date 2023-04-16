package fg.forrest.namedayapp.nameday.controller;

import fg.forrest.namedayapp.nameday.model.Nameday;
import fg.forrest.namedayapp.nameday.service.NamedayService;
import fg.forrest.namedayapp.nameday.exception.FileParsingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.IOException;

@RestController
@RequestMapping(path = "/")
public class NamedayController {

    @Autowired
    private NamedayService namedayService;
//    private final NamedayService namedayService;
//
////    @Autowired
////    public NamedayController(NamedayService namedayService) {
////        this.namedayService = namedayService;
////    }

    @GetMapping("/test")
    public List<Nameday> getNameday() throws IOException {
        return namedayService.getNameday();
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateNamedays(@RequestParam("file") @NotNull MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("File is empty");
        }
        String filename = file.getOriginalFilename();
        if(!filename.toString().endsWith(".txt")){
            return ResponseEntity.badRequest().body("Unsupported file format, must be .txt");
        }
        try{

            //TODO: parse file + validate contents + save to disk
            String contents = new String(filename.getBytes(), StandardCharsets.UTF_8);
            List<Nameday> namedays = namedayService.parseNamedays(contents);
            if (!namedayService.validateNamedays(namedays)){
                return ResponseEntity.badRequest().body("Duplicate name for the same data");
            }
            if (namedayService.saveNamedays(namedays, file)){
                return ResponseEntity.ok().body("Nameday file successfully updated");
            } else {
                return ResponseEntity.badRequest().body("Failed to update nameday file");
            }
        } catch (FileParsingException e){
            return ResponseEntity.badRequest().body("Invalid nameday file format: " + e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Failed to update nameday file" + e.getMessage());
        }
    }

}
