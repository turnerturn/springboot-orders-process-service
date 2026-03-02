package cafe.shop.repository;

import cafe.shop.model.entities.ProductAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductAuthorizationRepository extends JpaRepository<ProductAuthorization, UUID> {

    List<ProductAuthorization> findByProductId(UUID productId);
}
