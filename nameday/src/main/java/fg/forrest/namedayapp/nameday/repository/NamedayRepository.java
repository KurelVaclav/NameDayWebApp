package fg.forrest.namedayapp.nameday.repository;

import fg.forrest.namedayapp.nameday.model.Nameday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NamedayRepository extends JpaRepository<Nameday,LocalDate> {
    @Query("SELECT n FROM Nameday n WHERE n.date = :date")
    List<Nameday> findByDate(LocalDate date);

//    @Modifying
//    @Transactional
//    @Query("UPDATE nameday n SET n.namedays =: namedays WHERE n.date =: date")
//    void updateNamedaysByDate(@Param("namedays") List<String> namedays, @Param("date") LocalDate date);

}
