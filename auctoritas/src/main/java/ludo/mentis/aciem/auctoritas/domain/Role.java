package ludo.mentis.aciem.auctoritas.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_role")
public class Role {

    @Id
    @Column(name = "id_role", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role", nullable = false, unique = true)
    private String code;

    @Column(columnDefinition = "varchar(max)")
    private String description;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "datetime2")
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "datetime2")
    private OffsetDateTime lastUpdated;

    public Integer getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public OffsetDateTime getDateCreated() {
        return this.dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
