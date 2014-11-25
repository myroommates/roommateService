package entities;

import entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Roommate.FIND_BY_ID, query = "where " + AuditedAbstractEntity.COL_ID + " = :" + AuditedAbstractEntity.COL_ID),
        @NamedQuery(name = Roommate.FIND_BY_AUTHENTICATION_KEY, query = "where " + Roommate.COL_AUTHENTICATION_KEY + " = :" + Roommate.COL_AUTHENTICATION_KEY),
        @NamedQuery(name = Roommate.FIND_BY_HOME, query = "where " + Roommate.COL_HOME + " = :" + Roommate.COL_HOME),
        @NamedQuery(name = Roommate.FIND_BY_EMAIL, query = "where " + Roommate.COL_EMAIL + " = :" + Roommate.COL_EMAIL),
        @NamedQuery(name = Roommate.FIND_BY_EMAIL_AND_PASSWORD, query = "where " + Roommate.COL_EMAIL + " = :" + Roommate.COL_EMAIL + " and " + Roommate.COL_PASSWORD + " = :" + Roommate.COL_PASSWORD),
})
public class Roommate extends AuditedAbstractEntity {

    //request
    public static final String FIND_BY_EMAIL = "Roommate_FIND_BY_EMAIL";
    public static final String FIND_BY_EMAIL_AND_PASSWORD = "Roommate_FIND_BY_EMAIL_AND_PASSWORD";
    public static final String FIND_BY_HOME = "Roommate_FIND_BY_HOME";
    public static final String FIND_BY_ID = "Roommate_FIND_BY_ID";
    public static final String FIND_BY_AUTHENTICATION_KEY = "Roommate_FIND_BY_AUTHENTICATION_KEY";

    //columns
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_HOME = "home";
    public static final String COL_AUTHENTICATION_KEY = "authenticationKey";


    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, name = COL_EMAIL)
    private String email;

    @Column(nullable = false, name = COL_PASSWORD)
    private String password;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Home home;

    @Column(name = COL_AUTHENTICATION_KEY)
    private String authenticationKey;

    @Column()
    private Date authenticationTime;

    @Column(nullable = false)
    private Float iconColor;

    @OneToMany(mappedBy = "creator")
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

    public Date getAuthenticationTime() {
        return authenticationTime;
    }

    public void setAuthenticationTime(Date authenticationTime) {
        this.authenticationTime = authenticationTime;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    @Override
    public String toString() {
        return "Roommate{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", home=" + home +
                ", authenticationKey='" + authenticationKey + '\'' +
                ", authenticationTime=" + authenticationTime +
                ", iconColor=" + iconColor +
                ", ticketList=" + ticketList +
                '}';
    }
}
