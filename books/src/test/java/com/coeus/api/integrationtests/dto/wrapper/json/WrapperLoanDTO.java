package com.coeus.api.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperLoanDTO {

    @JsonProperty("_embedded")
    private LoanEmbeddedDTO embedded;

    public WrapperLoanDTO() {}

    public LoanEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(LoanEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}
