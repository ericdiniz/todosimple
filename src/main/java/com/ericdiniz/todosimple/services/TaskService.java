package com.ericdiniz.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericdiniz.todosimple.models.Task;
import com.ericdiniz.todosimple.models.User;
import com.ericdiniz.todosimple.repositories.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }

    @Transactional
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);

        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Task obj) {
        findById(obj.getId());
        try {
            this.taskRepository.deleteById(obj.getId());
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relaciondas");
        }
    }
}
