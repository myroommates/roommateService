package controllers;

import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.CategoryToCategoryDTOConverter;
import dto.CategoryDTO;
import dto.ListDTO;
import dto.technical.ResultDTO;
import entities.Category;
import entities.Roommate;
import play.mvc.Result;
import play.mvc.Security;
import services.CategoryService;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

import java.util.Set;

/**
 * Created by florian on 10/11/14.
 */
public class CategoryController extends AbstractController {

    private CategoryService categoryService = new CategoryService();
    private CategoryToCategoryDTOConverter categoryToCategoryDTOConverter = new CategoryToCategoryDTOConverter();

    /**
     * Expected DTO :
     * AuthenticationDTO
     * Return :
     * CategoryDTO or ExceptionDTO
     */
    @Security.Authenticated(SecurityController.class)
    public Result getById(Long id){

        //load
        Category category = categoryService.findById(id);
        Roommate currentUser = securityController.getCurrentUser();

        //control
        if(category==null || !category.getHome().equals(currentUser.getHome())){
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_CATEGORY, id));
        }

        //convert
        return ok(categoryToCategoryDTOConverter.convert(category));
    }

    /**
     * Expected DTO :
     * AuthenticationDTO
     * Return :
     * ListDTO<CategoryDTO> or ExceptionDTO
     * @return
     */
    @Security.Authenticated(SecurityController.class)
    public Result getAll() {

        //load
        Set<Category> categoryList = securityController.getCurrentUser().getHome().getCategories();

        //convert
        ListDTO<CategoryDTO> result = new ListDTO<>();
        for (Category category : categoryList) {
            result.addElement(categoryToCategoryDTOConverter.convert(category));
        }

        //return
        return ok(result);
    }

    /**
     * Expected DTO :
     * AuthenticationDTO + CategoryDTO
     * Return :
     * ListDTO<CategoryDTO> or ExceptionDTO
     * @return
     */
    @Security.Authenticated(SecurityController.class)
    public Result create() {

        CategoryDTO dto = extractDTOFromRequest(CategoryDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //control name
        Category category = categoryService.findByHomeAndName(securityController.getCurrentUser().getHome(), dto.getName()  );

        if(category!=null){
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.CATEGORY_WITH_SAME_NAME_ALREADY_EXISTS,dto.getName()));
        }

        //build category
        category = new Category();
        category.setName(dto.getName());
        category.setHome(currentUser.getHome());

        //operation
        categoryService.saveOrUpdate(category);

        //return
        CategoryToCategoryDTOConverter converter = new CategoryToCategoryDTOConverter();
        return ok(converter.convert(category));
    }

    @Security.Authenticated(SecurityController.class)
    public Result edit(Long id) {

        CategoryDTO dto = extractDTOFromRequest(CategoryDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //load category
        Category category = categoryService.findById(id);

        //control
        if (!category.getHome().equals(currentUser.getHome())) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_CATEGORY, id));
        }
        //control name
        Category categoryWithSameName = categoryService.findByHomeAndName(securityController.getCurrentUser().getHome(), dto.getName()  );

        if(categoryWithSameName!=null && !categoryWithSameName.equals(category)){
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.CATEGORY_WITH_SAME_NAME_ALREADY_EXISTS,dto.getName()));
        }

        //edit
        category.setName(dto.getName());

        //operation
        categoryService.saveOrUpdate(category);

        //return
        CategoryToCategoryDTOConverter converter = new CategoryToCategoryDTOConverter();
        return ok(converter.convert(category));
    }

    @Security.Authenticated(SecurityController.class)
    public Result remove(Long id) {
        Roommate currentUser = securityController.getCurrentUser();

        //load category
        Category category = categoryService.findById(id);

        if (category == null) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.ENTITY_NOT_FOUND, Category.class.getName(), id));
        }

        //control usage
        if(category.getTicketList()!=null && category.getTicketList().size()>0){
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.CATEGORY_USED,category.getName()));
        }

        //control
        if (!category.getHome().equals(currentUser.getHome())) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_CATEGORY, id));
        }

        //operation
        categoryService.remove(category);

        //return
        return ok(new ResultDTO());
    }
}
