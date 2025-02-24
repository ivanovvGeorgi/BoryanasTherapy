package com.example.boryanastherapy.repository;

import com.example.boryanastherapy.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByOrderByDateDesc();  // Custom query to order by date
}
