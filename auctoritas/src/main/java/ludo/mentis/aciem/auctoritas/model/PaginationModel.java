package ludo.mentis.aciem.auctoritas.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PaginationModel {

    private List<PaginationStep> steps;
    private String elements;

}
