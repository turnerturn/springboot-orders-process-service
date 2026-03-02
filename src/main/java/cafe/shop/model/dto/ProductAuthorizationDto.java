package cafe.shop.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductAuthorizationDto {

    private UUID id;
    private UUID productId;
    private String productName;
    private String supplier;
    private String loadingControl;
    private String destination;
}
