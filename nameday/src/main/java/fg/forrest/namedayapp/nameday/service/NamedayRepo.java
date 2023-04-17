package fg.forrest.namedayapp.nameday.service;

import fg.forrest.namedayapp.nameday.model.Nameday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NamedayRepo extends JpaRepository<Nameday,Long> {



}
