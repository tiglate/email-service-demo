package ludo.mentis.aciem.tabellarius.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_message_log")
@Data
public class MessageLog {

    @Id
    @Column(name = "id_message")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_message")
    private Message message;

    @Column(name = "success", nullable = false)
    private Boolean success = true;

    @Column(name = "sender_ip", nullable = false, length = 100)
    private String senderIp;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}