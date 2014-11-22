package entities.technical;

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
}

