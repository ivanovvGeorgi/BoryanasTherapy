package com.example.boryanastherapy.repository;

import com.example.boryanastherapy.model.entity.UnavailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnavailableDateRepository extends JpaRepository<UnavailableDate, String> {
}