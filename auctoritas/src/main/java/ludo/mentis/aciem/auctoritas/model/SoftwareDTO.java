package ludo.mentis.aciem.auctoritas.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
