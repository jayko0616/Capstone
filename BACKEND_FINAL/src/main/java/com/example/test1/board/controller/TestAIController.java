package com.example.test1.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TestAIController {

    @GetMapping("/testAI")
    public List<Integer> testAI(@RequestParam("id") int id) {
        // ID 리스트 생성
        List<Integer> idList = new ArrayList<>();
        for (int i = 1; i <= id; i++) {
            idList.add(i);
        }
        return idList;
    }
}
