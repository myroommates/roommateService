package dto.post;

import dto.technical.DTO;
import dto.technical.verification.NotNull;
import dto.technical.verification.Size;

/**
 * Created by florian on 18/02/15.
 */
public class ContactUsDTO extends DTO{
    @NotNull
    private long roommateId;

    @Size(min=1,max=255)
    private String subject;

    @Size(min=1,max=3000)
    private String content;

    public ContactUsDTO() {
    }

    public long getRoommateId() {
        return roommateId;
    }

    public void setRoommateId(long roommateId) {
        this.roommateId = roommateId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ContactUsDTO{" +
                "roommateId=" + roommateId +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
