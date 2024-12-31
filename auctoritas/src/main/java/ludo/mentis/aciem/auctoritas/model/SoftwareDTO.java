package ludo.mentis.aciem.auctoritas.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


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
