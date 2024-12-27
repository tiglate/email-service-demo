package ludo.mentis.aciem.auctoritas.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SoftwareDTO {

    private Integer id;

    @Size(max = 3)
    private String code;

    @NotNull
    @Size(max = 255)
    private String name;

}
