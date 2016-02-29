package be.flo.roommateService.dto;

import be.flo.roommateService.dto.technical.DTO;
import be.flo.roommateService.dto.technical.verification.NotNull;
import be.flo.roommateService.dto.technical.verification.Pattern;
import be.flo.roommateService.dto.technical.verification.Size;
import be.flo.roommateService.util.ErrorMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class TicketDTO extends DTO {

    private Long id;

    @NotNull
    @Pattern(regexp = ".{1,1000}")
    private String description;

    @NotNull
    private Date date;

    @NotNull
    @Size(min=1,message = ErrorMessage.VALIDATION_DEBTOR)
    private List<TicketDebtorDTO> debtorList;

    private String category;

    private Long payerId;

    private List<CommentDTO> comments;

    private Boolean hasNewComment;

    public TicketDTO() {
    }

    public Boolean getHasNewComment() {
        return hasNewComment;
    }

    public void setHasNewComment(Boolean hasNewComment) {
        this.hasNewComment = hasNewComment;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TicketDebtorDTO> getDebtorList() {
        return debtorList;
    }

    public void setDebtorList(List<TicketDebtorDTO> debtorList) {
        this.debtorList = debtorList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", debtorList=" + debtorList +
                ", category='" + category + '\'' +
                ", payerId=" + payerId +
                '}';
    }

    public void addTicketDebtor(TicketDebtorDTO ticketDebtorDTO) {
        if(debtorList ==null){
            debtorList = new ArrayList<>();
        }
        this.debtorList.add(ticketDebtorDTO);
    }

    public void addComment(CommentDTO comment) {
        if(comments==null){
            comments = new ArrayList<>();
        }

        comments.add(comment);
    }
}
