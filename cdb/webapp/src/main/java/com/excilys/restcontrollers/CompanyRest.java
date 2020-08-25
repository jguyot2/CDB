package com.excilys.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.adapters.CompanyAdapter;
import com.excilys.model.CompanyDto;

@RestController
@CrossOrigin
@RequestMapping("/api/companies")
public class CompanyRest {
	@Autowired
	CompanyAdapter companyService;

	@GetMapping
	public List<CompanyDto> getCompanyList() {
		return this.companyService.fetchList();
	}
}
