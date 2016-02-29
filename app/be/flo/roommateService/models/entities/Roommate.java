package be.flo.roommateService.models.entities;

import be.flo.roommateService.models.entities.technical.AbstractEntity;
import play.i18n.Lang;

import javax.persistence.*;

import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
@Entity
public class Roommate extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nameAbrv;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String googleKey;

    @ManyToOne(optional = false,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Home home;

    /**
     * used to authentication
     * For the application and for the cookie into for the web site
     */
    @Column
    private String authenticationKey;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private Float iconColor;

    @OneToMany(mappedBy = "payer")
    private List<Ticket> ticketList;

    @Column
    private boolean keepSessionOpen;

    /**
     * used for connection and for reactivation for the application
     */
    @Column(nullable = false)
    private String password;

    @Column(nullable = false,columnDefinition = "boolean default false")
    private Boolean isAdmin = false;

    @Column(nullable = false,columnDefinition = "boolean default false")
    private Boolean isSuperAdmin = false;

    @OneToMany(mappedBy = "roommate")
    private List<SurveyValue> surveyValues;


    public Roommate() {
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public List<SurveyValue> getSurveyValues() {
        return surveyValues;
    }

    public void setSurveyValues(List<SurveyValue> surveyValues) {
        this.surveyValues = surveyValues;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getNameAbrv() {
        return nameAbrv;
    }

    public void setNameAbrv(String nameAbrv) {
        this.nameAbrv = nameAbrv;
    }

    public Lang getLanguage() {
        return Lang.forCode(language);
    }

    public void setLanguage(Lang language) {
        this.language = language.code();
    }

    public boolean isKeepSessionOpen() {
        return keepSessionOpen;
    }

    public void setKeepSessionOpen(boolean keepSessionOpen) {
        this.keepSessionOpen = keepSessionOpen;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public Float getIconColor() {
        return iconColor;
    }

    public void setIconColor(Float iconColor) {
        this.iconColor = iconColor;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
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

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsSuperAdmin() {
        return isSuperAdmin;
    }

    @Override
    public String toString() {
        return "Roommate{" +
                "name='" + name + '\'' +
                ", nameAbrv='" + nameAbrv + '\'' +
                ", email='" + email + '\'' +
                ", home=" + home +
                ", authenticationKey='" + authenticationKey + '\'' +
                ", language='" + language + '\'' +
                ", iconColor=" + iconColor +
                ", ticketList=" + ticketList +
                ", keepSessionOpen=" + keepSessionOpen +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
