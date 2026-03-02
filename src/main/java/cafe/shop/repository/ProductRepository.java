package cafe.shop.repository;

import cafe.shop.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByRecipeId(UUID recipeId);
}
