package ludo.mentis.aciem.tabellarius.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tb_user")
@Data
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
}