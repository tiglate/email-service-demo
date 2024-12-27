package ludo.mentis.aciem.tabellarius.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_message")
@Data
public class Message {

    public Message() {
        this.recipients = new ArrayList<>();
        this.attachments = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_message")
    private Integer id;

    @Column(name = "from_address", nullable = false)
    private String from;

    @Column(name = "message_subject", nullable = false)
    private String subject;

    @Column(name = "body", columnDefinition = "varchar(MAX)")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_type", nullable = false)
    private BodyType bodyType;

    @OneToOne(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private MessageLog log;

    @OneToOne(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private MessageError error;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recipient> recipients;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments;

    @Override
    public String toString() {
        try {
            var objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{"
                    + "\"id\":" + id + ","
                    + "\"from\":\"" + from + "\","
                    + "\"subject\":\"" + subject + "\","
                    + "\"serialization_error\":\"" + e.getMessage() + "\""
                    + "}";
        }
    }
}