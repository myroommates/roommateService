package be.flo.roommateService.dto;

import be.flo.roommateService.dto.technical.DTO;
import be.flo.roommateService.dto.technical.verification.NotNull;
import be.flo.roommateService.dto.technical.verification.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class HomeDTO extends DTO {

    private Long id;

    @NotNull
    @Size(min = 1,max = 3)
    private String moneySymbol;

    private List<CommentDTO> comments;

    private Boolean hasNewComment;


    public HomeDTO() {
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getMoneySymbol() {
        return moneySymbol;
    }

    public void setMoneySymbol(String moneySymbol) {
        this.moneySymbol = moneySymbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addComment(CommentDTO comment) {
        if(comments==null){
            comments=new ArrayList<>();
        }

        comments.add(comment);
    }

    public void setHasNewComment(Boolean hasNewComment) {
        this.hasNewComment = hasNewComment;
    }

    public Boolean isHasNewComment() {
        return hasNewComment;
    }
}
