package com.ericdiniz.todosimple.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericdiniz.todosimple.models.Task;
import com.ericdiniz.todosimple.models.User;
import com.ericdiniz.todosimple.repositories.TaskRepository;
import com.ericdiniz.todosimple.services.exceptions.DataBindingViolationException;
import com.ericdiniz.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new ObjectNotFoundException(
                "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));
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

    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public List<Task> buscarTodas() {
        try {
            return this.taskRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relaciondas");
        }
    }

    public List<Task> buscarTodasDeUmUsuario(Long userId) {
        try {
            return this.taskRepository.findByUser_Id(userId);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relaciondas");
        }
    }
}
