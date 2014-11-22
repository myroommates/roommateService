package converter;

import dto.CategoryDTO;
import entities.Category;

/**
 * Created by florian on 11/11/14.
 */
public class CategoryToCategoryDTOConverter {

    public CategoryDTO convert(Category category){

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setId(category.getId());

        categoryDTO.setName(category.getName());

        return categoryDTO;

    }
}
