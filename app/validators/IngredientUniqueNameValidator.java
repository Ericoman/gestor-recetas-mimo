package validators;

import models.Ingredient;
import play.data.validation.Constraints;
import play.libs.F;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IngredientUniqueNameValidator extends Constraints.Validator<Ingredient> implements ConstraintValidator<IngredientUniqueName,Ingredient> {
    @Override
    public F.Tuple<String, Object[]> getErrorMessageKey() {
        return new F.Tuple<String, Object[]>(
                "Pepe* is not valid", new Object[]{""});
    }
    @Override
    public boolean isValid(Ingredient value) {
        Ingredient ingredient = Ingredient.findByName(value.getName());
        if (ingredient != null && ingredient.getId() != value.getId()) {
            return false;
        }
        return true;
    }
}
//Referencias http://dolszewski.com/java/custom-parametrized-validation-annotation/
