package ludo.mentis.aciem.tabellarius.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_message_log")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getSenderIp() {
        return senderIp;
    }

    public void setSenderIp(String senderIp) {
        this.senderIp = senderIp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}