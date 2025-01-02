package ludo.mentis.aciem.auctoritas.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Table(name = "tb_user")
public class User {

    @Id
    @Column(name = "id_user", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column
    private OffsetDateTime accountExpirationDate;

    @Column(nullable = false)
    private int failedLoginAttempts = 0;

    @Column(columnDefinition = "datetime2")
    private OffsetDateTime lastFailedLoginAttempt;

    @Column(nullable = false)
    private boolean accountLocked = false;

    @ManyToMany
    @JoinTable(
            name = "tb_user_role",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private Set<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_software")
    private Software software;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "datetime2")
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "datetime2")
    private OffsetDateTime lastUpdated;

}
