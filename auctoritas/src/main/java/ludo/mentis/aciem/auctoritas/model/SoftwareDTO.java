package ludo.mentis.aciem.auctoritas.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareDTO {

    private Integer id;

    @Size(max = 3)
    private String code;

    @NotNull
    @Size(max = 255)
    private String name;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

}
