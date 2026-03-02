package cafe.shop.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {

    private String id;
    private String name;
    private String supplier;
    private String loadingControl;
    private List<AdditiveDto> additives;
}
