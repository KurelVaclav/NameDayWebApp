package fg.forrest.namedayapp.nameday.model;

import java.time.LocalDate;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;

@Entity
public class Nameday{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String nameday;

    public Nameday(){

    }

    public Nameday(LocalDate date, String nameday) {
        this.date = date;
        this.nameday = nameday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNameday() {
        return nameday;
    }

    public void setNameday(String nameday) {
        this.nameday = nameday;
    }

    @Override
    public String toString() {
        return "Nameday{" +
                ", date=" + date +
                ", nameday='" + nameday + '\'' +
                '}';
    }

    public String toJSONString() {
        return "[{" + "\"date\"" + ":" + '\"' + date + '\"' + "," + "\"nameday\"" + ":" + '\"' + nameday + '\"' + "}]";
    }
}
