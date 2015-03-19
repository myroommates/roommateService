package controllers.rest;

import controllers.AboutController;
import controllers.technical.SecurityRestController;
import converter.CommentToCommentDTOConverter;
import dto.CommentDTO;
import dto.technical.ResultDTO;
import models.entities.Comment;
import models.entities.ShoppingItem;
import models.entities.Ticket;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.CommentService;
import services.ShoppingItemService;
import services.TicketService;
import services.impl.CommentServiceImpl;
import services.impl.ShoppingItemServiceImpl;
import services.impl.TicketServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

import java.util.Date;

/**
 * Created by florian on 18/03/15.
 */
public class CommentRestController extends AboutController{


    //service
    private ShoppingItemService shoppingItemService = new ShoppingItemServiceImpl();
    private TicketService ticketService = new TicketServiceImpl();
    private CommentService commentService = new CommentServiceImpl();

    //converter
    private CommentToCommentDTOConverter commentToCommentDTOConverter = new CommentToCommentDTOConverter();

    @Security.Authenticated(SecurityRestController.class)
    @com.avaje.ebean.annotation.Transactional
    public Result addCommentForShoppingItem(Long shoppingItemId){

        //control shopping item
        ShoppingItem shoppingItem = shoppingItemService.findById(shoppingItemId);
        if(!shoppingItem.getHome().equals(securityController.getCurrentUser().getHome())){
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_SHOPPING_ITEM, shoppingItemId);
        }

        //add comment
        Comment comment = createComment(extractDTOFromRequest(CommentDTO.class));
        comment.setShoppingItem(shoppingItem);

        //save
        commentService.saveOrUpdate(comment);


        //result
        return ok(commentToCommentDTOConverter.convert(comment));
    }

    @Security.Authenticated(SecurityRestController.class)
    @com.avaje.ebean.annotation.Transactional
    public Result addCommentForHome(){

        //add comment
        Comment comment = createComment(extractDTOFromRequest(CommentDTO.class));
        comment.setHome(securityController.getCurrentUser().getHome());

        //save
        commentService.saveOrUpdate(comment);


        //result
        return ok(commentToCommentDTOConverter.convert(comment));
    }

    @Security.Authenticated(SecurityRestController.class)
    @com.avaje.ebean.annotation.Transactional
    public Result addCommentForTicket(Long ticketId){

        //control shopping item
        Ticket ticket= ticketService.findById(ticketId);
        if(!ticket.getHome().equals(securityController.getCurrentUser().getHome())){
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_TICKET, ticketId);
        }

        //add comment
        Comment comment = createComment(extractDTOFromRequest(CommentDTO.class));
        comment.setTicket(ticket);

        //save
        commentService.saveOrUpdate(comment);


        //result
        return ok(commentToCommentDTOConverter.convert(comment));
    }

    private Comment createComment(CommentDTO commentDTO){

        Comment comment = new Comment();

        comment.setComment(commentDTO.getComment());
        comment.setCreator(securityController.getCurrentUser());
        comment.setDateCreation(new Date());

        //parent
        Comment commentParent = commentService.findById(commentDTO.getParentId());
        if(commentParent!=null && commentParent.getCreator().getHome().equals(securityController.getCurrentUser().getHome())) {
            comment.setParent(commentParent);
        }

        return comment;
    }





    @Security.Authenticated(SecurityRestController.class)
    @com.avaje.ebean.annotation.Transactional
    public Result editComment(Long commentId){

        Comment comment = commentService.findById(commentId);

        //control comment
        if(comment==null || !comment.getCreator().equals(securityController.getCurrentUser())){
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_COMMENT, commentId);
        }

        //edit
        CommentDTO commentDTO = extractDTOFromRequest(CommentDTO.class);

        comment.setComment(commentDTO.getComment());
        comment.setDateEdit(new Date());

        //save
        commentService.saveOrUpdate(comment);

        //result
        return ok(new ResultDTO());
    }

    @Security.Authenticated(SecurityRestController.class)
    @com.avaje.ebean.annotation.Transactional
    public Result remove(Long commentId) {
        Comment comment = commentService.findById(commentId);

        //control comment
        if (comment == null || !comment.getCreator().equals(securityController.getCurrentUser())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_COMMENT, commentId);
        }

        commentService.remove(comment);

        //result
        return ok(new ResultDTO());
    }
}
