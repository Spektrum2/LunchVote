package graduation.lunchvote.restaurant.model;

import graduation.lunchvote.common.model.NamedEntity;
import graduation.lunchvote.common.validation.NoHtml;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurant_unique_name_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Restaurant extends NamedEntity {
    @NotBlank
    @Size(min = 2, max = 128)
    @Column(name = "address", nullable = false)
    @NoHtml
    private String address;

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }
}