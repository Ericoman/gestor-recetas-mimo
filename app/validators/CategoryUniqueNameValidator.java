package validators;

import models.Category;
import models.Ingredient;
import play.data.validation.Constraints;
import play.libs.F;

import javax.validation.ConstraintValidator;

public class CategoryUniqueNameValidator extends Constraints.Validator<Category> implements ConstraintValidator<CategoryUniqueName,Category> {
    @Override
    public F.Tuple<String, Object[]> getErrorMessageKey() {
        return new F.Tuple<String, Object[]>(
                "Pepe* is not valid", new Object[]{""});
    }
    @Override
    public boolean isValid(Category value) {
        Category category = Category.findByName(value.getName());
        if (category != null && category.getId() != value.getId()) {
            return false;
        }
        return true;
    }
}
//Referencias http://dolszewski.com/java/custom-parametrized-validation-annotation/
