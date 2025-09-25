package ludo.mentis.aciem.tabellarius.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.IOException;

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

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
