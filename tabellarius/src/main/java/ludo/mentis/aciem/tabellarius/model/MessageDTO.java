package ludo.mentis.aciem.tabellarius.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ludo.mentis.aciem.tabellarius.domain.BodyType;
import ludo.mentis.aciem.tabellarius.validation.UniqueFileName;
import ludo.mentis.aciem.tabellarius.validation.ValidEmailList;

import java.util.List;

@Getter
@Setter
public class MessageDTO {

    @NotNull
    @Size(max = 255)
    private String subject;

    @NotNull
    @Size(max = 75000)
    private String body;

    @NotNull
    private BodyType bodyType;

    @ValidEmailList
    private List<String> recipientsTo;

    @ValidEmailList(allowNull = true, allowEmpty = true)
    private List<String> recipientsCc;

    @ValidEmailList(allowNull = true, allowEmpty = true)
    private List<String> recipientsBcc;

    @UniqueFileName
    private List<AttachmentDTO> attachments;
}