package com.coeus.api.integrationtests.dto.wrapper.xml;

import com.coeus.api.integrationtests.dto.WarningDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class PagedModelWarning {

    @XmlElement(name = "content")
    public List<WarningDTO> content;

    public PagedModelWarning() {
    }

    public List<WarningDTO> getContent() {
        return content;
    }

    public void setContent(List<WarningDTO> content) {
        this.content = content;
    }
}