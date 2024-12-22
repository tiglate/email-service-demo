package ludo.mentis.aciem.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_recipient")
@Data
public class Recipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recipient")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_message", nullable = false)
    private Message message;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 3)
    private RecipientType type;
}