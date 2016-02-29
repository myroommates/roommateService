package be.flo.roommateService.controllers.technical;

import be.flo.roommateService.models.entities.Roommate;

/**
 * Created by florian on 20/02/15.
 */
public class SuperAdminSecurityController extends SecurityRestController {

    @Override
    public boolean testRight(Roommate currentUser){
        return currentUser.getIsSuperAdmin();
    }

}
