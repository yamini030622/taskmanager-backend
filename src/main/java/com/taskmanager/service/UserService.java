package com.taskmanager.service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.dto.LoginRequest;
import com.taskmanager.dto.RegisterRequest;
import com.taskmanager.dto.TaskRequest;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public User registerUser(RegisterRequest request) {

        User existingUser = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (existingUser != null) {
            throw new RuntimeException("Account already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User loginUser(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!user.getPassword()
                .equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
    public User googleLogin(String name, String email) {

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword("");
        newUser.setRole("USER");
        newUser.setCreatedAt(LocalDateTime.now());

        return userRepository.save(newUser);
    }

    public Task createTask(TaskRequest request) {

        System.out.println("User ID Received = " + request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElse(null);

        System.out.println("User Found = " + user);

        if (user == null) {
            return null;
        }

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        task.setUser(user);

        System.out.println("Task User = " + task.getUser());
        System.out.println("User ID in Task = " + task.getUser().getId());

        return taskRepository.save(task);
    }
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }
    
    public Task updateTask(Long taskId, TaskRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElse(null);

        if (task == null) {
            return null;
        }
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        return taskRepository.save(task);
    }
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}