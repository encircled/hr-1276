import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class User {

    @Id
    public Long id;

    @Column
    public String name;

    public User() {

    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
