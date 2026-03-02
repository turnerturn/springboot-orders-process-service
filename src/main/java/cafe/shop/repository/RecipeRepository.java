package cafe.shop.repository;

import cafe.shop.model.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    List<Recipe> findByTerminalId(UUID terminalId);
}
