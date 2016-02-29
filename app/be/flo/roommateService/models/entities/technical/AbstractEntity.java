package be.flo.roommateService.models.entities.technical;

import be.flo.roommateService.controllers.technical.CommonSecurityController;
import be.flo.roommateService.models.entities.converter.LocalDateTimePersistenceConverter;
import org.apache.commons.lang3.StringUtils;
import play.mvc.Http;

import javax.persistence.*;
import java.io.Serializable;
import java.text.Normalizer;
import java.time.LocalDateTime;

/**
 * Created by florian on 10/11/14.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Version
    protected Long version;

    @Column(columnDefinition = "timestamp")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    protected LocalDateTime creationDate;

    @Basic
    protected String creationUser;

    @Column(columnDefinition = "timestamp")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    protected LocalDateTime lastUpdate;

    @Basic
    protected String lastUpdateUser;

    @PrePersist
    public void prePersist() {
        creationDate = LocalDateTime.now();
        creationUser = getCurrentUser();
        lastUpdate = LocalDateTime.now();
        lastUpdateUser = getCurrentUser();
    }

    @PreUpdate
    public void preUpdate() {
        if (id == null) {
            prePersist();
        } else {
            lastUpdate = LocalDateTime.now();
            lastUpdateUser = getCurrentUser();
        }
    }

    private static String getCurrentUser() {

        if (Http.Context.current.get() == null) {
            return "TECHNICAL";
        }

        Http.Session session     = Http.Context.current().session();
        String       currentUser = session.get(CommonSecurityController.ACCOUNT_IDENTIFIER);
        if (currentUser == null) {
            currentUser = session.get(CommonSecurityController.SESSION_IDENTIFIER_STORE);
        }
        if (currentUser == null) {
            currentUser = session.get(CommonSecurityController.REQUEST_HEADER_AUTHENTICATION_KEY);
        }

        if (currentUser == null || StringUtils.isBlank(currentUser)) {
            currentUser = "TECHNICAL";
        }
        return currentUser;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AuditedAbstractEntity{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }


    protected String normalize(String s) {
        s = s.toLowerCase();
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[^\\p{ASCII}]", "");
        s = s.replaceAll("\\p{M}", "");
        return s;
    }



}

