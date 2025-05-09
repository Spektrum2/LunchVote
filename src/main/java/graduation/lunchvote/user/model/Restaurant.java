package graduation.lunchvote.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NamedQueries({
        @NamedQuery(name = Restaurant.DELETE, query = "DELETE FROM Restaurant r WHERE r.id=:id"),
        @NamedQuery(name = Restaurant.ALL, query = "SELECT r FROM Restaurant r"),
})
@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractNamedEntity {

    public static final String DELETE = "Restaurant.delete";
    public static final String ALL = "Restaurant.getAll";

    @NotBlank
    @Size(min = 2, max = 128)
    @Column(name = "address", nullable = false)
    private String address;

    public Restaurant() {
    }

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.address);
    }

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
