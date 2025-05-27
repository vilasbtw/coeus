package com.coeus.books.models.mapper;

import com.coeus.books.models.Employee;
import com.coeus.books.models.dtos.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {
    Employee toEntity(EmployeeDTO employeeDTO);
    EmployeeDTO toDTO(Employee employee);
    List<Employee> toEntities(List<EmployeeDTO> employeeDTOS);
    List<EmployeeDTO> toDTOS(List<Employee> employees);
}