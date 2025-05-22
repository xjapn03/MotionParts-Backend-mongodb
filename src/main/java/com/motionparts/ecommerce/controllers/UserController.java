package com.motionparts.ecommerce.controllers;

import com.motionparts.ecommerce.dto.RegisterRequest;
import com.motionparts.ecommerce.models.User;
import com.motionparts.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;



    // Endpoint para obtener información de un usuario por su username
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        return userService.findByUsername(username)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(
            @PathVariable("username") String username,
            @RequestBody @Valid RegisterRequest registerRequest) {
        try {
            // Extrae los DTOs del RegisterRequest
            var userDTO = registerRequest.getUser();
            var userInfoDTO = registerRequest.getUserInfo();

            // Llama al método updateUser del servicio, que usa el mapper internamente
            User updatedUser = userService.updateUser(username, userDTO, userInfoDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}

