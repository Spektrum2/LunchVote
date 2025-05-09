package graduation.lunchvote.user.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class ItemsDTO {
    @NotEmpty()
    private Map<@NotBlank String, @NotNull Double> items;

    public ItemsDTO(Map<String, Double> items) {
        this.items = items;
    }
}
