package com.coeus.api.integrationtests.dto.wrapper.xml;

import com.coeus.api.integrationtests.dto.EmployeeDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class PagedModelEmployee {

    @XmlElement(name = "content")
    public List<EmployeeDTO> content;

    public PagedModelEmployee() {
    }

    public List<EmployeeDTO> getContent() {
        return content;
    }

    public void setContent(List<EmployeeDTO> content) {
        this.content = content;
    }
}