package com.coeus.api.integrationtests.dto.wrapper.xml;

import com.coeus.api.integrationtests.dto.LoanResponseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class PagedModelLoan {

    @XmlElement(name = "content")
    public List<LoanResponseDTO> content;

    public PagedModelLoan() {}

    public List<LoanResponseDTO> getContent() {
        return content;
    }

    public void setContent(List<LoanResponseDTO> content) {
        this.content = content;
    }
}