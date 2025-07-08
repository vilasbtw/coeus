package com.coeus.api.integrationtests.dto.wrapper.xml;

import com.coeus.api.integrationtests.dto.StudentDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class PagedModelStudent {

    @XmlElement(name = "content")
    public List<StudentDTO> content;

    public PagedModelStudent() {
    }

    public List<StudentDTO> getContent() {
        return content;
    }

    public void setContent(List<StudentDTO> content) {
        this.content = content;
    }
}