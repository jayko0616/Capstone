package com.example.test1.login.controller;

import com.example.test1.login.DTO.UserDTO;
import com.example.test1.login.jwt.JWTUtil;
import com.example.test1.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/user")
    public ResponseEntity<UserDTO> findUser(@RequestHeader("Authorization") String jwtToken){
        return new ResponseEntity<>(userService.findByUserId(jwtToken), HttpStatus.OK);
    }
}
