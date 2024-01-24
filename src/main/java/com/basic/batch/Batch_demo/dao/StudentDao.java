package com.basic.batch.Batch_demo.dao;

import com.basic.batch.Batch_demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDao extends JpaRepository<Student, Integer> {
}
