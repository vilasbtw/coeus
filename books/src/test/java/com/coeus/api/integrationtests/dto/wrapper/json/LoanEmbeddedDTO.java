package com.coeus.api.integrationtests.dto.wrapper.json;

import com.coeus.api.integrationtests.dto.LoanResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LoanEmbeddedDTO {

    @JsonProperty("loans")
    private List<LoanResponseDTO> loans;

    public LoanEmbeddedDTO() {}

    public List<LoanResponseDTO> getLoans() {
        return loans;
    }

    public void setLoans(List<LoanResponseDTO> loans) {
        this.loans = loans;
    }
}
