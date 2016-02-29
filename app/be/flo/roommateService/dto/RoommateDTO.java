package be.flo.roommateService.dto;

import be.flo.roommateService.dto.technical.DTO;
import be.flo.roommateService.dto.technical.verification.NotNull;
import be.flo.roommateService.dto.technical.verification.Pattern;
import be.flo.roommateService.dto.technical.verification.Size;
import be.flo.roommateService.util.ErrorMessage;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateDTO extends DTO {

    private Long id;

    @NotNull
    @Size(min = 2,max =50)
    private String name;

    private String nameAbrv;

    @NotNull
    @Pattern(regexp = Pattern.EMAIL,message = ErrorMessage.VALIDATION_EMAIL)
    private String email;

    private Float iconColor;

    private boolean isAdmin;

    private String languageCode;

    private boolean keepSessionOpen;

    public RoommateDTO() {
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
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

    public boolean getKeepSessionOpen() {
        return keepSessionOpen;
    }

    public void setKeepSessionOpen(boolean keepSessionOpen) {
        this.keepSessionOpen = keepSessionOpen;
    }
}
