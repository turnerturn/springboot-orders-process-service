package cafe.shop.service;

import cafe.shop.model.dto.RecipeDto;

import java.util.List;
import java.util.UUID;

public interface RecipeService {

    List<RecipeDto> createRecipe(UUID terminalId, List<RecipeDto> recipeDtos);

    RecipeDto updateRecipe(UUID terminalId, UUID recipeId, RecipeDto recipeDto);

    void deleteRecipe(UUID terminalId, UUID recipeId);

    List<RecipeDto> getRecipes(UUID terminalId);
}
