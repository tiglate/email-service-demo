package ludo.mentis.aciem.demo.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentDTO {

    @Deprecated
    public AttachmentDTO() {
    }

    public AttachmentDTO(String fileName, String fileType, byte[] attachment) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.attachment = attachment;
    }

    @NotNull
    @NotEmpty
    private byte[] attachment;

    @NotNull
    @Size(max = 255)
    private String fileName;

    @NotNull
    @Size(max = 255)
    private String fileType;
}
