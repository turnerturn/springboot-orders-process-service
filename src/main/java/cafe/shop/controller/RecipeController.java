package cafe.shop.controller;

import cafe.shop.model.dto.RecipeDto;
import cafe.shop.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/terminals/{terminalId}/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public ResponseEntity<List<RecipeDto>> createRecipe(
            @PathVariable UUID terminalId,
            @RequestBody List<RecipeDto> recipeDtos) {
        List<RecipeDto> createdRecipes = recipeService.createRecipe(terminalId, recipeDtos);
        return new ResponseEntity<>(createdRecipes, HttpStatus.CREATED);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<RecipeDto> updateRecipe(
            @PathVariable UUID terminalId,
            @PathVariable UUID recipeId,
            @RequestBody RecipeDto recipeDto) {
        RecipeDto updatedRecipe = recipeService.updateRecipe(terminalId, recipeId, recipeDto);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(
            @PathVariable UUID terminalId,
            @PathVariable UUID recipeId) {
        recipeService.deleteRecipe(terminalId, recipeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<RecipeDto>> getRecipes(@PathVariable UUID terminalId) {
        List<RecipeDto> recipes = recipeService.getRecipes(terminalId);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }
}
