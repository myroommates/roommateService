package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by florian on 22/11/14.
 */
public class CategoryController extends Controller{

    public Result getById(Long id){
        return ok();
    }

    public Result getAll() {
        return ok();
    }

    public Result create() {
        return ok();
    }

    public Result edit(Long id) {
        return ok();
    }

    public Result remove(Long id) {
        return ok();
    }
}
