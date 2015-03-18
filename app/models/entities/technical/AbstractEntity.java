package models.entities.technical;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by florian on 10/11/14.
 */
@MappedSuperclass
public abstract class AbstractEntity extends Model {


    public static final String COL_ID = "id";
    public static final String PARAM_ID = COL_ID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COL_ID)
    protected Long id;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass().equals(this.getClass()))) return false;
        if (!super.equals(o)) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (!id.equals(that.id)) return false;

        return true;
    }



}

