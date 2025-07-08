package com.coeus.api.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperStudentDTO {

    @JsonProperty("_embedded")
    private StudentEmbeddedDTO embedded;

    public WrapperStudentDTO() {
    }

    public StudentEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(StudentEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}