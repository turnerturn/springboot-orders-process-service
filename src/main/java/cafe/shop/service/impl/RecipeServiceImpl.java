package cafe.shop.service.impl;

import cafe.shop.model.dto.AdditiveDto;
import cafe.shop.model.dto.ProductDto;
import cafe.shop.model.dto.RecipeDto;
import cafe.shop.model.entities.Additive;
import cafe.shop.model.entities.Product;
import cafe.shop.model.entities.Recipe;
import cafe.shop.model.entities.Terminal;
import cafe.shop.exception.RecipeNotFoundException;
import cafe.shop.exception.TerminalNotFoundException;
import cafe.shop.repository.RecipeRepository;
import cafe.shop.repository.TerminalRepository;
import cafe.shop.service.BaseService;
import cafe.shop.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl extends BaseService implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Override
    public List<RecipeDto> createRecipe(UUID terminalId, List<RecipeDto> recipeDtos) {
        Terminal terminal = terminalRepository.findById(terminalId)
                .orElseThrow(() -> new TerminalNotFoundException("Terminal not found with id: " + terminalId));

        List<Recipe> recipes = recipeDtos.stream().map(dto -> {
            Recipe recipe = new Recipe();
            recipe.setTerminal(terminal);
            recipe.setName(dto.getName());
            if (dto.getProducts() != null) {
                List<Product> products = dto.getProducts().stream().map(pdto -> {
                    Product product = new Product();
                    product.setName(pdto.getName());
                    product.setSupplier(pdto.getSupplier());
                    product.setLoadingControl(pdto.getLoadingControl());
                    product.setRecipe(recipe);
                    if (pdto.getAdditives() != null) {
                        List<Additive> additives = pdto.getAdditives().stream().map(adto -> {
                            Additive additive = new Additive();
                            additive.setName(adto.getName());
                            additive.setProduct(product);
                            return additive;
                        }).collect(Collectors.toList());
                        product.setAdditives(additives);
                    }
                    return product;
                }).collect(Collectors.toList());
                recipe.setProducts(products);
            }
            return recipe;
        }).collect(Collectors.toList());

        recipes = recipeRepository.saveAll(recipes);
        return recipes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public RecipeDto updateRecipe(UUID terminalId, UUID recipeId, RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));

        if (!recipe.getTerminal().getId().equals(terminalId)) {
            throw new TerminalNotFoundException("Recipe does not belong to terminal with id: " + terminalId);
        }

        recipe.setName(recipeDto.getName());
        recipe = recipeRepository.save(recipe);
        return convertToDto(recipe);
    }

    @Override
    public void deleteRecipe(UUID terminalId, UUID recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));

        if (!recipe.getTerminal().getId().equals(terminalId)) {
            throw new TerminalNotFoundException("Recipe does not belong to terminal with id: " + terminalId);
        }

        recipeRepository.delete(recipe);
    }

    @Override
    public List<RecipeDto> getRecipes(UUID terminalId) {
        return recipeRepository.findByTerminalId(terminalId).stream()
                .map(this::convertToDto)
                .toList();
    }

    private RecipeDto convertToDto(Recipe recipe) {
        RecipeDto dto = new RecipeDto();
        dto.setId(recipe.getId().toString());
        dto.setName(recipe.getName());
        if (recipe.getProducts() != null) {
            dto.setProducts(recipe.getProducts().stream().map(this::convertProductToDto).collect(Collectors.toList()));
        }
        return dto;
    }

    private ProductDto convertProductToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId().toString());
        dto.setName(product.getName());
        dto.setSupplier(product.getSupplier());
        dto.setLoadingControl(product.getLoadingControl());
        if (product.getAdditives() != null) {
            dto.setAdditives(product.getAdditives().stream().map(a -> {
                AdditiveDto adto = new AdditiveDto();
                adto.setId(a.getId().toString());
                adto.setName(a.getName());
                return adto;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}
