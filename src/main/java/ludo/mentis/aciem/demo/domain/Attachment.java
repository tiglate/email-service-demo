package ludo.mentis.aciem.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_attachment")
@Data
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_attachment")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_message", nullable = false)
    private Message message;

    @Column(name = "attachment", nullable = false, columnDefinition = "varbinary(MAX)")
    private byte[] attachment;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;
}