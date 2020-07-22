package com.excilys.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.adapters.ComputerAdapter;

@RestController
@RequestMapping("/api")
public class DeleteComputerRest {

    @Autowired
    ComputerAdapter adapter;

    @PostMapping(path = "/deleteComputer", produces = "application/json")
    public ResponseEntity<String> deleteComputer(final Long[] identifiers) {
        System.out.println("deletion");
        int nbComputersDeleted = this.adapter.delete(identifiers);
        return ResponseEntity.ok("{}");
    }
}
