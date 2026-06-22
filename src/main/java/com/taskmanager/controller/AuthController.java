package com.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.taskmanager.dto.LoginRequest;
import com.taskmanager.dto.RegisterRequest;
import com.taskmanager.dto.TaskRequest;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        try {
            User user = userService.loginUser(request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/task")
    public Task createTask(@RequestBody TaskRequest request) {
        return userService.createTask(request);
    }
    

    @GetMapping("/tasks/{userId}")
    public List<Task> getTasks(@PathVariable Long userId) {
        return userService.getTasksByUser(userId);
    }

    @PutMapping("/task/{id}")
    public Task updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest request) {
        return userService.updateTask(id, request);
    }

    @DeleteMapping("/task/{id}")
    public String deleteTask(@PathVariable Long id) {
        userService.deleteTask(id);
        return "Task deleted successfully";
    }
    @PostMapping("/google-login")
    public User googleLogin(
            @RequestBody Map<String, String> data) {

        String email = data.get("email");
        String name = data.get("name");

        return userService.googleLogin(
                name,
                email
        );
    }
}