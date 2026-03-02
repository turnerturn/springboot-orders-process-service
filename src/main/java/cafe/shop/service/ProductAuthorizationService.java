package cafe.shop.service;

import cafe.shop.model.dto.ProductAuthorizationDto;

import java.util.List;
import java.util.UUID;

public interface ProductAuthorizationService {

    ProductAuthorizationDto createProductAuthorization(ProductAuthorizationDto dto);

    List<ProductAuthorizationDto> getProductAuthorizations(UUID productId);

    void deleteProductAuthorization(UUID authorizationId);
}
