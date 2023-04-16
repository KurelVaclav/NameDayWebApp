package fg.forrest.namedayapp.nameday;
import java.time.LocalDate;

public class Nameday {
    private LocalDate date;
    private String nameday;

    public Nameday(){

    }

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
}
