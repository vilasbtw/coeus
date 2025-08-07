package com.coeus.api.integrationtests.dto.wrapper.json;

import com.coeus.api.integrationtests.dto.WarningDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WarningEmbeddedDTO {

    @JsonProperty("warnings")
    private List<WarningDTO> warnings;

    public WarningEmbeddedDTO() {
    }

    public List<WarningDTO> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<WarningDTO> warnings) {
        this.warnings = warnings;
    }
}
