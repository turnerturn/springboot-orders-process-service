package cafe.shop.controller;

import cafe.shop.model.dto.ProductAuthorizationDto;
import cafe.shop.service.ProductAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/product-authorizations")
public class ProductAuthorizationController {

    @Autowired
    private ProductAuthorizationService productAuthorizationService;

    @PostMapping
    public ResponseEntity<ProductAuthorizationDto> createProductAuthorization(
            @RequestBody ProductAuthorizationDto dto) {
        ProductAuthorizationDto created = productAuthorizationService.createProductAuthorization(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductAuthorizationDto>> getProductAuthorizations(
            @RequestParam UUID productId) {
        List<ProductAuthorizationDto> authorizations = productAuthorizationService.getProductAuthorizations(productId);
        return ResponseEntity.ok(authorizations);
    }

    @DeleteMapping("/{authorizationId}")
    public ResponseEntity<Void> deleteProductAuthorization(@PathVariable UUID authorizationId) {
        productAuthorizationService.deleteProductAuthorization(authorizationId);
        return ResponseEntity.noContent().build();
    }
}
