package ludo.mentis.aciem.tabellarius.domain;

import java.util.Arrays;
import javax.persistence.*;

@Entity
@Table(name = "tb_message_error")
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
        this.stackTrace = Arrays.toString(e.getStackTrace());
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}