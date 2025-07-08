package com.coeus.api.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperEmployeeDTO {

    @JsonProperty("_embedded")
    private EmployeeEmbeddedDTO embedded;

    public WrapperEmployeeDTO() {
    }

    public EmployeeEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(EmployeeEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}