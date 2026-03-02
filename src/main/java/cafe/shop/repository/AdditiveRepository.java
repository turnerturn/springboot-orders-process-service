package cafe.shop.repository;

import cafe.shop.model.entities.Additive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdditiveRepository extends JpaRepository<Additive, UUID> {

    List<Additive> findByProductId(UUID productId);
}
