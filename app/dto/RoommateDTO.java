package dto;

import dto.technical.DTO;
import play.data.validation.Constraints;

import javax.validation.constraints.Size;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateDTO extends DTO {

    private Long id;

    @Constraints.Required
    @Constraints.MinLength(2)
    @Constraints.MaxLength(50)
    private String name;

    @Constraints.Required
    @Constraints.Email
    private String email;

    private Float iconColor;

    private boolean isAdmin;

    private String nameAbrv;

    public RoommateDTO() {
    }

    public String getNameAbrv() {
        return nameAbrv;
    }

    public void setNameAbrv(String nameAbrv) {
        this.nameAbrv = nameAbrv;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean getIsAdmin(){
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Float getIconColor() {
        return iconColor;
    }

    public void setIconColor(Float iconColor) {
        this.iconColor = iconColor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RoommateDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", iconColor=" + iconColor +
                ", isAdmin=" + isAdmin +
                '}';
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RoommateDTO && ((RoommateDTO) obj).getId()!=null && ((RoommateDTO) obj).getId().equals(this.id);
    }
}
