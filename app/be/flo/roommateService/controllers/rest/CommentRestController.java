package be.flo.roommateService.controllers.rest;

import be.flo.roommateService.controllers.AboutController;
import be.flo.roommateService.controllers.technical.SecurityRestController;
import be.flo.roommateService.converter.CommentToCommentDTOConverter;
import be.flo.roommateService.dto.CommentDTO;
import be.flo.roommateService.dto.technical.ResultDTO;
import be.flo.roommateService.models.entities.Comment;
import be.flo.roommateService.models.entities.CommentLastVisualization;
import be.flo.roommateService.models.entities.ShoppingItem;
import be.flo.roommateService.models.entities.Ticket;
import play.db.jpa.Transactional;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import be.flo.roommateService.services.CommentLastVisualizationService;
import be.flo.roommateService.services.CommentService;
import be.flo.roommateService.services.ShoppingItemService;
import be.flo.roommateService.services.TicketService;
import be.flo.roommateService.services.impl.CommentLastVisualizationServiceImpl;
import be.flo.roommateService.services.impl.CommentServiceImpl;
import be.flo.roommateService.services.impl.ShoppingItemServiceImpl;
import be.flo.roommateService.services.impl.TicketServiceImpl;
import be.flo.roommateService.util.ErrorMessage;
import be.flo.roommateService.util.exception.MyRuntimeException;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by florian on 18/03/15.
 */
public class CommentRestController extends AboutController {


    //service
    private ShoppingItemService shoppingItemService = new ShoppingItemServiceImpl();
    private TicketService ticketService = new TicketServiceImpl();
    private CommentService commentService = new CommentServiceImpl();
    private CommentLastVisualizationService commentLastVisualizationService= new CommentLastVisualizationServiceImpl();

    //be.flo.roommateService.converter
    private CommentToCommentDTOConverter commentToCommentDTOConverter = new CommentToCommentDTOConverter();

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result addCommentForShoppingItem(Long shoppingItemId) {

        //control shopping item
        ShoppingItem shoppingItem = shoppingItemService.findById(shoppingItemId);
        if (!shoppingItem.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_SHOPPING_ITEM, shoppingItemId);
        }

        //add comment
        Comment comment = createComment(extractDTOFromRequest(CommentDTO.class));
        comment.setShoppingItem(shoppingItem);

        //save
        commentService.saveOrUpdate(comment);

        //read
        hasReadCommentForShoppingItem(shoppingItemId);


        //result
        return Results.ok(commentToCommentDTOConverter.convert(comment));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result addCommentForHome() {

        //add comment
        Comment comment = createComment(extractDTOFromRequest(CommentDTO.class));
        comment.setHome(securityController.getCurrentUser().getHome());

        //save
        commentService.saveOrUpdate(comment);

        //read
        hasReadCommentForHome();

        //result
        return Results.ok(commentToCommentDTOConverter.convert(comment));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result addCommentForTicket(Long ticketId) {

        //control shopping item
        Ticket ticket = ticketService.findById(ticketId);
        if (!ticket.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_TICKET, ticketId);
        }

        //add comment
        Comment comment = createComment(extractDTOFromRequest(CommentDTO.class));
        comment.setTicket(ticket);

        //save
        commentService.saveOrUpdate(comment);

        //read
        hasReadCommentForTicket(ticketId);

        //result
        return Results.ok(commentToCommentDTOConverter.convert(comment));
    }

    private Comment createComment(CommentDTO commentDTO) {

        Comment comment = new Comment();

        comment.setComment(commentDTO.getComment());
        comment.setCreator(securityController.getCurrentUser());
        comment.setDateCreation(new Date());

        //parent
        if (commentDTO.getParentId() != null) {
            Comment commentParent = commentService.findById(commentDTO.getParentId());
            if (commentParent != null && commentParent.getCreator().getHome().equals(securityController.getCurrentUser().getHome())) {
                comment.setParent(commentParent);
            }
        }

        return comment;
    }


    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result editComment(Long commentId) {

        Comment comment = commentService.findById(commentId);

        //control comment
        if (comment == null || !comment.getCreator().equals(securityController.getCurrentUser())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_COMMENT, commentId);
        }

        //edit
        CommentDTO commentDTO = extractDTOFromRequest(CommentDTO.class);

        comment.setComment(commentDTO.getComment());
        comment.setDateEdit(new Date());

        //save
        commentService.saveOrUpdate(comment);

        //result
        return Results.ok(new ResultDTO());
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result remove(Long commentId) {
        Comment comment = commentService.findById(commentId);

        //control comment
        if (comment == null || !comment.getCreator().equals(securityController.getCurrentUser())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_COMMENT, commentId);
        }

        commentService.remove(comment);

        //result
        return Results.ok(new ResultDTO());
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result hasReadCommentForTicket(Long ticketId) {

        //control shopping item
        Ticket ticket = ticketService.findById(ticketId);
        if (!ticket.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_TICKET, ticketId);
        }

        for (CommentLastVisualization commentLastVisualization : ticket.getCommentLastVisualizations()) {
            if (commentLastVisualization.getRoommate().equals(securityController.getCurrentUser())) {
                commentLastVisualization.setDate(LocalDateTime.now());

                commentLastVisualizationService.saveOrUpdate(commentLastVisualization);
                return Results.ok(new ResultDTO());
            }
        }

        CommentLastVisualization commentLastVisualization = new CommentLastVisualization();
        commentLastVisualization.setTicket(ticket);
        commentLastVisualization.setRoommate(securityController.getCurrentUser());
        commentLastVisualization.setDate(LocalDateTime.now());

        commentLastVisualizationService.saveOrUpdate(commentLastVisualization);

        return Results.ok(new ResultDTO());
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result hasReadCommentForShoppingItem(Long shoppingItemId) {

        //control shopping item
        ShoppingItem shoppingItem = shoppingItemService.findById(shoppingItemId);
        if (!shoppingItem.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_TICKET, shoppingItemId);
        }

        for (CommentLastVisualization commentLastVisualization : shoppingItem.getCommentLastVisualizations()) {
            if (commentLastVisualization.getRoommate().equals(securityController.getCurrentUser())) {
                commentLastVisualization.setDate(LocalDateTime.now());

                commentLastVisualizationService.saveOrUpdate(commentLastVisualization);
                return Results.ok(new ResultDTO());
            }
        }

        CommentLastVisualization commentLastVisualization = new CommentLastVisualization();
        commentLastVisualization.setShoppingItem(shoppingItem);
        commentLastVisualization.setRoommate(securityController.getCurrentUser());
        commentLastVisualization.setDate(LocalDateTime.now());

        commentLastVisualizationService.saveOrUpdate(commentLastVisualization);

        return Results.ok(new ResultDTO());
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result hasReadCommentForHome() {

        for (CommentLastVisualization commentLastVisualization : securityController.getCurrentUser().getHome().getCommentLastVisualizations()) {
            if (commentLastVisualization.getRoommate().equals(securityController.getCurrentUser())) {
                commentLastVisualization.setDate(LocalDateTime.now());

                commentLastVisualizationService.saveOrUpdate(commentLastVisualization);
                return Results.ok(new ResultDTO());
            }
        }

        CommentLastVisualization commentLastVisualization = new CommentLastVisualization();
        commentLastVisualization.setHome(securityController.getCurrentUser().getHome());
        commentLastVisualization.setRoommate(securityController.getCurrentUser());
        commentLastVisualization.setDate(LocalDateTime.now());

        commentLastVisualizationService.saveOrUpdate(commentLastVisualization);

        return Results.ok(new ResultDTO());
    }

}
