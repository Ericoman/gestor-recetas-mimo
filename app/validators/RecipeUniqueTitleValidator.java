package validators;

import models.Ingredient;
import models.Recipe;
import play.data.validation.Constraints;
import play.libs.F;

import javax.validation.ConstraintValidator;

public class RecipeUniqueTitleValidator extends Constraints.Validator<Recipe> implements ConstraintValidator<RecipeUniqueTitle,Recipe> {
    @Override
    public F.Tuple<String, Object[]> getErrorMessageKey() {
        return new F.Tuple<String, Object[]>(
                "Pepe* is not valid", new Object[]{""});
    }
    @Override
    public boolean isValid(Recipe value) {
        Recipe recipe = Recipe.findByTitle(value.getTitle());
        if (recipe != null && recipe.getId() != value.getId()) {
            return false;
        }
        return true;
    }
}
//Referencias http://dolszewski.com/java/custom-parametrized-validation-annotation/
