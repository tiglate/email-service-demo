package ludo.mentis.aciem.tesserarius.model;

public class AttachmentDTO {

    private byte[] attachment;

    private String fileName;

    private String fileType;

    public AttachmentDTO() {
    }

    public AttachmentDTO(String fileName, String fileType, byte[] attachment) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.attachment = attachment;
    }

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
