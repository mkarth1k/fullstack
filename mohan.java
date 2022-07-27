package com.deloitte.spring.boot.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger; // important!
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.spring.boot.demo.exception.EmployeeNotFoundException;
import com.deloitte.spring.boot.demo.model.Employee;
import com.deloitte.spring.boot.demo.repository.DepartmentRepository;
import com.deloitte.spring.boot.demo.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository empRepository;
	
	@Autowired
	DepartmentRepository depRepository;
	
	@Autowired
	private DepartmentService depService;

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	public List<Employee> getAllEmployees() {

		return empRepository.findAll();
	}

	public Employee getEmployeeById(int employeeId) {
		LOG.info(Integer.toString(employeeId));

		Optional<Employee> empOptional = empRepository.findById(employeeId);

		if (empOptional.isPresent()) {
			return empOptional.get();
		} else {
			String errorMessage = "Employee with eid " + employeeId + " not found.";
			LOG.warn(errorMessage);
			throw new EmployeeNotFoundException(errorMessage);
		}

	}
	
	public List<Employee> getEmployeeByFirstName(String firstName) {
		LOG.info(firstName);
//		Stream<Employee> empList = empRepository.findAll().stream();
//		List<Employee> empFinal = empList.filter(p -> p.getFirstName().equals(firstName)).collect(Collectors.toCollection(ArrayList::new));
		
		List<Employee> empFinal = empRepository.findByFirstName(firstName);
		
		if (empFinal.isEmpty()) {
			String errorMessage = "Employees with first name " + firstName + " are not found.";
			LOG.warn(errorMessage);
			throw new EmployeeNotFoundException(errorMessage);
		} else {
			return empFinal;
		}

	}
	
	public List<Employee> getEmpsBySalaryGreaterThan(double salary) {
		LOG.info(Double.toString(salary));
		List<Employee> empList = empRepository.findAll();
		List<Employee> empFinal = new ArrayList<>();
		for(Employee e : empList) {
			if(e.getSalary() > salary) {
				empFinal.add(e);
			}
		}
		if (empFinal.isEmpty()) {
			String errorMessage = "Employees with salary greater " + salary + " are not found.";
			LOG.warn(errorMessage);
			throw new EmployeeNotFoundException(errorMessage);
		} else {
			return empFinal;
		}

	}

	public Employee addEmployee(Employee employee) {
		LOG.info(employee.toString());
		if (null != employee.getDepartment())
			depService.getDepartmentById(employee.getDepartment().getDepartmentId());
		return empRepository.save(employee);
	}

	public Employee updateEmployee(Employee employee) {
		LOG.info(employee.toString());
		if (null != employee.getDepartment())
			depService.getDepartmentById(employee.getDepartment().getDepartmentId());
		Employee emp = this.getEmployeeById(employee.getEmployeeId());
		emp = empRepository.save(employee);
		return emp;
	}

	public Employee deleteEmployee(int employeeId) {
		LOG.info(Integer.toString(employeeId));
		if(empRepository.existsById(employeeId))
			empRepository.deleteById(employeeId);
		else{
		 return this.getEmployeeById(employeeId);}
		return null;
		
	}

	public List<Employee> getEmployeeByFirstLetter(String letter) {
		LOG.info(letter);
		List<Employee> empFinal = empRepository.findByFirstNameStartsWith(letter);
		
		if (empFinal.isEmpty()) {
			String errorMessage = "Employees with first letter " + letter + " are not found.";
			LOG.warn(errorMessage);
			throw new EmployeeNotFoundException(errorMessage);
		} else {
			return empFinal;
		}
		
		
	}
}

