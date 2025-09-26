package ludo.mentis.aciem.auctoritas.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Set;


@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @Column(columnDefinition = "datetime2")
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

    public Integer getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public OffsetDateTime getAccountExpirationDate() {
        return this.accountExpirationDate;
    }

    public int getFailedLoginAttempts() {
        return this.failedLoginAttempts;
    }

    public OffsetDateTime getLastFailedLoginAttempt() {
        return this.lastFailedLoginAttempt;
    }

    public boolean isAccountLocked() {
        return this.accountLocked;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public Software getSoftware() {
        return this.software;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountExpirationDate(OffsetDateTime accountExpirationDate) {
        this.accountExpirationDate = accountExpirationDate;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public void setLastFailedLoginAttempt(OffsetDateTime lastFailedLoginAttempt) {
        this.lastFailedLoginAttempt = lastFailedLoginAttempt;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
