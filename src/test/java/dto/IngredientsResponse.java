package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class IngredientsResponse {
    @Getter @Setter
    public String success;
    @Getter @Setter
    public List<Ingredient> data;

    public IngredientsResponse() { }

    public IngredientsResponse(String success, List<Ingredient> data) {
        this.success = success;
        this.data = data;
    }
}
