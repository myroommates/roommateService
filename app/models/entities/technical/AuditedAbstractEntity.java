package models.entities.technical;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by florian on 10/11/14.
 */
@MappedSuperclass
public abstract class AuditedAbstractEntity extends Model {


    public static final String COL_ID = "id";
    public static final String PARAM_ID = COL_ID;

    @Id
    @Column(name = COL_ID)
    protected Long id;
/*
    @CreatedTimestamp
    protected Date creationDate;

    @UpdatedTimestamp
    @Version
    protected Date lastUpdate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
/*
    @Override
    public String toString() {
        return "AuditedAbstractEntity{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
*/
}

