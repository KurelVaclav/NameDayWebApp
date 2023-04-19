package fg.forrest.namedayapp.nameday.repository;

import fg.forrest.namedayapp.nameday.exception.FileParsingException;
import fg.forrest.namedayapp.nameday.model.Nameday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NamedayRepository extends JpaRepository<Nameday,LocalDate> {
    @Query("SELECT n FROM Nameday n WHERE n.date = :date")
    List<Nameday> findByDate(LocalDate date);

}
