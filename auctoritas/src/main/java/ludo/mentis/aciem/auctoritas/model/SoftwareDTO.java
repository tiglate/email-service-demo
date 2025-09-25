package ludo.mentis.aciem.auctoritas.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;


public class SoftwareDTO {

    private Integer id;

    @Size(max = 3)
    private String code;

    @NotNull
    @Size(max = 255)
    private String name;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

    public SoftwareDTO(Integer id, @Size(max = 3) String code, @NotNull @Size(max = 255) String name, OffsetDateTime dateCreated, OffsetDateTime lastUpdated) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
    }

    public SoftwareDTO() {
    }

    public Integer getId() {
        return this.id;
    }

    public @Size(max = 3) String getCode() {
        return this.code;
    }

    public @NotNull @Size(max = 255) String getName() {
        return this.name;
    }

    public OffsetDateTime getDateCreated() {
        return this.dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCode(@Size(max = 3) String code) {
        this.code = code;
    }

    public void setName(@NotNull @Size(max = 255) String name) {
        this.name = name;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
