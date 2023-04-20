package fg.forrest.namedayapp.nameday.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

/**
 * Class Nameday represents nameday (holiday of name in the Czech Republic)
 */
@Entity
public class Nameday{

    @Id
    @Convert(converter = Jsr310JpaConverters.LocalTimeConverter.class)
    private LocalDate date;
    private String nameday;

    public Nameday(){}

    public Nameday(LocalDate date, String nameday) {
        this.date = date;
        this.nameday = nameday;
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
