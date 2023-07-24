package com.ericdiniz.todosimple.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericdiniz.todosimple.models.Task;
import com.ericdiniz.todosimple.models.User;
import com.ericdiniz.todosimple.models.enums.ProfileEnum;
import com.ericdiniz.todosimple.repositories.TaskRepository;
import com.ericdiniz.todosimple.security.UserSpringSecurity;
import com.ericdiniz.todosimple.services.exceptions.AuthorizationException;
import com.ericdiniz.todosimple.services.exceptions.DataBindingViolationException;
import com.ericdiniz.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    public Task findById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task)) {
            throw new AuthorizationException("Acesso negado!");
        }

        return task;
    }

    @Transactional
    public Task create(Task obj) {

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Acesso negado!");
        }

        User user = this.userService.findById(userSpringSecurity.getId());
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
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Acesso negado!");
        }
        try {
            return this.taskRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relaciondas");
        }
    }

    public List<Task> buscarTodasDeUmUsuario() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException("Acesso negado!");
        }
        try {
            return this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relaciondas");
        }
    }

    private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }
}
