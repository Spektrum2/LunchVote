package graduation.lunchvote.restaurant.model;

import graduation.lunchvote.common.validation.NoHtml;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    @Column(name = "dish_name", nullable = false)
    @NotBlank
    @Size(min = 2, max = 64)
    @NoHtml
    private String name;

    @Column(name = "price", nullable = false)
    @NotNull
    private BigDecimal price;
}