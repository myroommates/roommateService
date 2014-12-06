package model.entities.technical;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Embedded;
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
    private Long id;

    @Embedded
    protected TechnicalSegment technicalSegment;

    protected AuditedAbstractEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TechnicalSegment getTechnicalSegment() {
        return technicalSegment;
    }

    public void setTechnicalSegment(TechnicalSegment technicalSegment) {
        this.technicalSegment = technicalSegment;
    }

    @Override
    public String toString() {
        return "AuditedAbstractEntity{" +
                "technicalSegment=" + technicalSegment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditedAbstractEntity)) return false;
        if (!super.equals(o)) return false;

        AuditedAbstractEntity that = (AuditedAbstractEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}

