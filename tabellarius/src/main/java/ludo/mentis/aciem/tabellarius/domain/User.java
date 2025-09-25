package ludo.mentis.aciem.tabellarius.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 4000)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "reset_uid", length = 1000)
    private String resetUid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetUid() {
        return resetUid;
    }

    public void setResetUid(String resetUid) {
        this.resetUid = resetUid;
    }
}