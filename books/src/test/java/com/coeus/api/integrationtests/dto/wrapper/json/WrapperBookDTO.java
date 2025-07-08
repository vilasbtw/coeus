package com.coeus.api.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperBookDTO {

    @JsonProperty("_embedded")
    private BookEmbeddedDTO embedded;

    public WrapperBookDTO() {
    }

    public BookEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(BookEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}