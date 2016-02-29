package be.flo.roommateService.controllers.technical;

import be.flo.roommateService.models.entities.Roommate;

/**
 * Created by florian on 10/11/14.
 */
public class AdminSecurityRestController extends SecurityRestController {

    @Override
    public boolean testRight(Roommate currentUser){
        return currentUser.getIsAdmin();
    }
}
