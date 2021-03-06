package be.flo.roommateService.converter;

import be.flo.roommateService.dto.LoginSuccessDTO;
import be.flo.roommateService.models.entities.*;
import be.flo.roommateService.services.FaqService;
import be.flo.roommateService.services.impl.FaqServiceImpl;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateToLoginSuccessConverter implements ConverterInterface<Roommate, LoginSuccessDTO> {

    //be.flo.roommateService.converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private FaqToFaqDTOConverter faqToFaqDTOConverter = new FaqToFaqDTOConverter();
    private SurveyToSurveyDTOConverter surveyToSurveyDTOConverter = new SurveyToSurveyDTOConverter();

    //service
    private FaqService faqService = new FaqServiceImpl();


    public LoginSuccessDTO convert(Roommate roommate) {

        LoginSuccessDTO dto = new LoginSuccessDTO();


        HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter(roommate);
        TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter(roommate);
        ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter = new ShoppingItemToShoppingItemDTOConverter(roommate);

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
        for (Faq faq : faqService.findAll()) {
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
