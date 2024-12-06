package com.example.PaginationDemo.service;

import com.example.PaginationDemo.entity.PeopleInfo;
import com.example.PaginationDemo.repository.PeopleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {
    @Autowired
    private PeopleRepo peopleRepo;

    public PeopleInfo addPersonInfo(PeopleInfo peopleInfo) {
        PeopleInfo info = new PeopleInfo();
        if (peopleInfo==null){
            info.setId(peopleInfo.getId());
            info.setName(peopleInfo.getName());
            info.setAddress(peopleInfo.getAddress());
            info.setEmail(peopleInfo.getEmail());
            info.setContactNumber(peopleInfo.getContactNumber());
            PeopleInfo save = peopleRepo.save(peopleInfo);
            return save;
        }else {
            info.setName(peopleInfo.getName());
            info.setAddress(peopleInfo.getAddress());
            info.setEmail(peopleInfo.getEmail());
            info.setContactNumber(peopleInfo.getContactNumber());
            PeopleInfo update = peopleRepo.save(peopleInfo);
            return update;
        }

    }

    public List<PeopleInfo> getAllPeopleList() {
        return peopleRepo.findAll();
    }

    public Page<PeopleInfo> getPaginatedPeopleList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return peopleRepo.findAll(pageable);
    }

    public Optional<PeopleInfo> fetchPeopleById(Long id) {
        Optional<PeopleInfo> person= peopleRepo.findById(id);
        if (!person.isPresent()){
            throw new RuntimeException("Person is not found");

        }
        return person;
    }

    public String deletePerson(Long id) {
        if (!peopleRepo.existsById(id)){
            throw new RuntimeException("Person Not Found");
        }
        peopleRepo.deleteById(id);
        return "Person deleted successfully";
    }
}
