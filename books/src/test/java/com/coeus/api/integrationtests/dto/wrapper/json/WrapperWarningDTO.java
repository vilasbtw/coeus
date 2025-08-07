package com.coeus.api.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperWarningDTO {

    @JsonProperty("_embedded")
    private WarningEmbeddedDTO embedded;

    public WrapperWarningDTO() {
    }

    public WarningEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(WarningEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}
