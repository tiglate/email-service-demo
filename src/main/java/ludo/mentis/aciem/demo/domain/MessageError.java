package ludo.mentis.aciem.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_message_error")
@Data
public class MessageError {

    @Id
    @Column(name = "id_message")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_message")
    private Message message;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "stack_trace", columnDefinition = "varchar(4000)")
    private String stackTrace;

    public void fromException(Exception e) {
        this.content = e.getMessage();
        this.stackTrace = e.getStackTrace().toString();
    }
}