package model.entities;

import model.entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Roommate.FIND_BY_ID, query = "where " + AuditedAbstractEntity.COL_ID + " = :" + AuditedAbstractEntity.PARAM_ID),
        @NamedQuery(name = Roommate.FIND_BY_AUTHENTICATION_KEY, query = "where " + Roommate.COL_AUTHENTICATION_KEY + " = :" + Roommate.PARAM_AUTHENTICATION_KEY),
        @NamedQuery(name = Roommate.FIND_BY_REACTIVATION_KEY, query = "where " + Roommate.COL_REACTIVATION_KEY + " = :" + Roommate.PARAM_REACTIVATION_KEY),
        @NamedQuery(name = Roommate.FIND_BY_HOME, query = "where " + Roommate.COL_HOME + " = :" + Roommate.PARAM_HOME),
        @NamedQuery(name = Roommate.FIND_BY_EMAIL, query = "where " + Roommate.COL_EMAIL + " = :" + Roommate.PARAM_EMAIL),
})
public class Roommate extends AuditedAbstractEntity {

    //request
    public static final String FIND_BY_EMAIL = "Roommate_FIND_BY_EMAIL";
    public static final String FIND_BY_REACTIVATION_KEY = "Roommate_FIND_BY_REACTIVATION_KEY";
    public static final String FIND_BY_HOME = "Roommate_FIND_BY_HOME";
    public static final String FIND_BY_ID = "Roommate_FIND_BY_ID";
    public static final String FIND_BY_AUTHENTICATION_KEY = "Roommate_FIND_BY_AUTHENTICATION_KEY";

    //columns
    public static final String COL_EMAIL = "email";
    public static final String PARAM_EMAIL = COL_EMAIL;
    public static final String COL_REACTIVATION_KEY = "reactivation_key";
    public static final String PARAM_REACTIVATION_KEY = COL_REACTIVATION_KEY;
    public static final String COL_HOME = "home";
    public static final String PARAM_HOME = COL_HOME;
    public static final String COL_AUTHENTICATION_KEY = "authenticationKey";
    public static final String PARAM_AUTHENTICATION_KEY = COL_AUTHENTICATION_KEY;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, name = COL_EMAIL)
    private String email;

    @Column(nullable = false, name = COL_REACTIVATION_KEY)
    private String reactivationKey;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Home home;

    @Column(name = COL_AUTHENTICATION_KEY)
    private String authenticationKey;

    @Column(nullable = false)
    private Float iconColor;

    @OneToMany(mappedBy = "payer")
    private List<Ticket> ticketList;

    public Roommate() {
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

    public String getReactivationKey() {
        return reactivationKey;
    }

    public void setReactivationKey(String reactivationKey) {
        this.reactivationKey = reactivationKey;
    }

    @Override
    public String toString() {
        return "Roommate{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", reactivationKey='" + reactivationKey + '\'' +
                ", home=" + home +
                ", authenticationKey='" + authenticationKey + '\'' +
                ", iconColor=" + iconColor +
                ", ticketList=" + ticketList +
                '}';
    }
}
