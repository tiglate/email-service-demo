package ludo.mentis.aciem.commons.web.model;

import java.util.List;

public class PaginationModel {

    private List<PaginationStep> steps;
    private String elements;

    public List<PaginationStep> getSteps() {
        return steps;
    }

    public void setSteps(List<PaginationStep> steps) {
        this.steps = steps;
    }

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements;
    }
}
