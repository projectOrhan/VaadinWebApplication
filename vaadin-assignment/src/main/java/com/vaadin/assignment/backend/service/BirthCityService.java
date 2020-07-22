package com.vaadin.assignment.backend.service;

import com.vaadin.assignment.backend.entity.BirthCity;
import com.vaadin.assignment.backend.repository.BirthCityRepository;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BirthCityService {

    private BirthCityRepository birthCityRepository;

    public BirthCityService(BirthCityRepository birthCityRepository) {
        this.birthCityRepository = birthCityRepository;
    }

    public List<BirthCity> findAll() {
        return birthCityRepository.findAll();
    }

    public Map<String,Integer> getStats() {
        HashMap<String, Integer> stats = new HashMap<>();
        findAll().forEach(company ->
                stats.put(company.getName(),company.getCustomers().size()));
        return stats;
    }
}
