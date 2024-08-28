package com.example.test1.login.controller;

import com.example.test1.login.DTO.JoinDTO;
import com.example.test1.login.service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinDTO joinDTO) {
        System.out.println(joinDTO.getUsername());
        int result = joinService.joinProcess(joinDTO);

        if (result == 1) {
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입에 실패했습니다.");
        }
    }

    @PostMapping("/checkUsername")
    public ResponseEntity<String> checkUsername(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        boolean isUsernameUnique = joinService.isUsernameUnique(username);

        if (isUsernameUnique) {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다.");
        }
    }

}
