package converter;

import dto.LoginSuccessDTO;
import models.entities.*;
import services.FaqService;
import services.impl.FaqServiceImpl;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateToLoginSuccessConverter implements ConverterInterface<Roommate, LoginSuccessDTO> {

    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter();
    private TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter();
    private ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter = new ShoppingItemToShoppingItemDTOConverter();
    private FaqToFaqDTOConverter faqToFaqDTOConverter = new FaqToFaqDTOConverter();
    private SurveyToSurveyDTOConverter surveyToSurveyDTOConverter = new SurveyToSurveyDTOConverter();

    //service
    private FaqService faqService = new FaqServiceImpl();

    public LoginSuccessDTO convert(Roommate roommate) {

        LoginSuccessDTO dto = new LoginSuccessDTO();

        dto.setCurrentRoommate(roommateToRoommateDTOConverter.convert(roommate));
        dto.setHome(homeToHomeConverter.convert(roommate.getHome()));
        dto.setAuthenticationKey(roommate.getAuthenticationKey());

        //convert other roommate
        for (Roommate otherRoommate : roommate.getHome().getRoommateList()) {
            dto.addRoommate(roommateToRoommateDTOConverter.convert(otherRoommate));
        }

        //convert ticket
        for (Ticket ticket : roommate.getHome().getTickets()) {
            dto.addTicket(ticketToTicketConverter.convert(ticket));
        }

        //convert shopping items
        for (ShoppingItem shoppingItem : roommate.getHome().getShoppingItems()) {
            dto.addShoppingItem(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));
        }

        //convert faq
        for (Faq faq : faqService.getAll()) {
            dto.addFaq(faqToFaqDTOConverter.convert(faq));
        }

        //survey
        for (SurveyValue surveyValue : roommate.getSurveyValues()) {
            if (!surveyValue.wasSubmit()) {
                dto.setSurveyDTO(surveyToSurveyDTOConverter.convert(surveyValue.getSurvey()));
                break;
            }
        }


        dto.addRoommate(roommateToRoommateDTOConverter.convert(roommate));

        return dto;
    }
}
