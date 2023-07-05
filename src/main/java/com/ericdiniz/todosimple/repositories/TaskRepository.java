package com.ericdiniz.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ericdiniz.todosimple.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // DIRETAMENTE DO SPRING
    // Optional<Task> findById(Long id);

    List<Task> findByUser_Id(Long id);

    // QUERY FALSA DO SQL
    // @Query(value = "Select t FROM Task t WHERE t.user.id = :id")
    // List<Task> findByUser_Id(@Param("id") Long id);

    // QUERY 100% SQL
    // @Query(value = "Select * FROM task t WHERE t.user_id = :id", nativeQuery =
    // true)
    // List<Task> findByUser_Id(@Param("id") Long id);
}
