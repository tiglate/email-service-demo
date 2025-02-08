package ludo.mentis.aciem.tabellarius.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;

@Getter
@Setter
public class AttachmentDTO {

    public AttachmentDTO() {
    }

    public AttachmentDTO(MultipartFile file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        var originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }

        var contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("File type cannot be null");
        }

        this.fileName = originalFilename;
        this.fileType = contentType;

        try {
            this.attachment = file.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read file", e);
        }
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
