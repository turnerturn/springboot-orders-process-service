package cafe.shop.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderRequest {

    private UUID terminalId;
    private UUID recipeId;
    private double volume;
    private String destinationId;
}
