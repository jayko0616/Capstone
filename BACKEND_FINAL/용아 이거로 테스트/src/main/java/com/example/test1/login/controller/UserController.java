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

    @PostMapping("/user")
    public ResponseEntity<UserDTO> findUser(@RequestHeader("Authorization") String jwtToken){
        try {
            // JWT 토큰 검증 및 사용자 정보 조회
            UserDTO user = userService.findByUserId(jwtToken);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            // 유효하지 않은 JWT 토큰 예외 처리
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
