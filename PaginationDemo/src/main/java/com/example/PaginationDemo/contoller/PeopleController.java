package com.example.PaginationDemo.contoller;

import com.example.PaginationDemo.entity.PeopleInfo;
import com.example.PaginationDemo.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PeopleController {
    @Autowired
    private PeopleService peopleService;

    @PostMapping("/createPerson")
    public PeopleInfo addPersonInfo(@RequestBody PeopleInfo peopleInfo){
        return  peopleService.addPersonInfo(peopleInfo);
    }

    @GetMapping("/peopleList")
    public List<PeopleInfo> getAllPeopleList(){
        return peopleService.getAllPeopleList();

    }

    @GetMapping("/paginatedPeopleList")
    public Page<PeopleInfo> getPaginatedPeopleList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return peopleService.getPaginatedPeopleList(page, size);
    }

    @GetMapping("/peopleList/{id}")
    public Optional<PeopleInfo> fetchPeopleById(@PathVariable("id") Long id){
        return peopleService.fetchPeopleById(id);
    }
    @DeleteMapping("/delete-person/{id}")
    public String deletePerson(@PathVariable Long id){
        return peopleService.deletePerson(id);
    }

}
