package cafe.shop.service.impl;

import cafe.shop.model.dto.ProductAuthorizationDto;
import cafe.shop.model.entities.Product;
import cafe.shop.model.entities.ProductAuthorization;
import cafe.shop.exception.ProductAuthorizationNotFoundException;
import cafe.shop.exception.ProductNotFoundException;
import cafe.shop.repository.ProductAuthorizationRepository;
import cafe.shop.repository.ProductRepository;
import cafe.shop.service.BaseService;
import cafe.shop.service.ProductAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductAuthorizationServiceImpl extends BaseService implements ProductAuthorizationService {

    @Autowired
    private ProductAuthorizationRepository productAuthorizationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductAuthorizationDto createProductAuthorization(ProductAuthorizationDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + dto.getProductId()));

        ProductAuthorization auth = new ProductAuthorization();
        auth.setProduct(product);
        auth.setSupplier(dto.getSupplier());
        auth.setLoadingControl(dto.getLoadingControl());
        auth.setDestination(dto.getDestination());

        auth = productAuthorizationRepository.save(auth);
        return convertToDto(auth);
    }

    @Override
    public List<ProductAuthorizationDto> getProductAuthorizations(UUID productId) {
        return productAuthorizationRepository.findByProductId(productId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProductAuthorization(UUID authorizationId) {
        ProductAuthorization auth = productAuthorizationRepository.findById(authorizationId)
                .orElseThrow(() -> new ProductAuthorizationNotFoundException("Product authorization not found with id: " + authorizationId));
        productAuthorizationRepository.delete(auth);
    }

    private ProductAuthorizationDto convertToDto(ProductAuthorization auth) {
        ProductAuthorizationDto dto = new ProductAuthorizationDto();
        dto.setId(auth.getId());
        dto.setProductId(auth.getProduct().getId());
        dto.setProductName(auth.getProduct().getName());
        dto.setSupplier(auth.getSupplier());
        dto.setLoadingControl(auth.getLoadingControl());
        dto.setDestination(auth.getDestination());
        return dto;
    }
}
