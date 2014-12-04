package util;

/**
 * Created by florian on 11/11/14.
 */
public enum ErrorMessage {

    WRONG_AUTHORIZATION("You have not the authorization"),
    LOGIN_WRONG_PASSWORD_LOGIN("wrong password / login"),
    NOT_CONNECTED("You are not connected"),
    HOME_NAME_ALREADY_USED("This home name is already used. Please choose an other"),
    EMAIL_ALREADY_USED("This email is already used. Please choose an other"),
    NOT_YOU_ROOMMATE("this is not your roommate : {O}"),
    ENTITY_NOT_FOUND("this entity {0} was not found : {1}"),
    NOT_YOU_CATEGORY("this is not your category : {O}"),
    NOT_YOU_TICKET("this is not your ticket : {O}"),
    NOT_TICKET_CREATOR("You are not the creator of the ticket {0} and you cannot edit it"),
    CANNOT_REMOVE_YOURSELF("You cannot remove yourself"),
    NOT_YOU_HOME("this is not your home : {0}"),
    WRONG_PASSWORD("The password must be composed of 6-18 letters or numbers"),
    UNIQUE_CONSTRAIN_VIOLATION("a unique constraint was not respected on the table {0}, constraint {1}, ids {2}"),
    CATEGORY_WITH_SAME_NAME_ALREADY_EXISTS("A category named {0} already exists. Please choose an other one."),
    CATEGORY_USED("The category {0} is already used by a ticket and cannot be removed"),
    ROOMMATE_USED("{0} is used by a ticket and cannot removed"),
    NOT_YOU_EVENT("this is not your event : {0}"),
    NOT_EVENT_CREATOR("You are not the creator of the event {0} and you cannot edit it"),
    NOT_YOU_SHOPPING_ITEM("this is not your shopping item : {0}"),
    NOT_ITEM_CREATOR("You are not the creator of the shopping item {0} and you cannot edit it");

    private final String message;

    private ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
