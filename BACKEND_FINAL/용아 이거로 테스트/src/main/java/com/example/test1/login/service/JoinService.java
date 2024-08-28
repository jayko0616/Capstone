package com.example.test1.login.service;

import com.example.test1.login.DTO.JoinDTO;
import com.example.test1.login.entity.UserEntity;
import com.example.test1.login.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public int joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String phoneNumber = joinDTO.getPhoneNumber();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {

            return 0;
        }

        UserEntity data = new UserEntity();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setPhoneNumber(phoneNumber);
        data.setRole("ROLE_ADMIN");

        userRepository.save(data);

        return 1;
    }

    public boolean isUsernameUnique(String username) {
        // 사용자 이름을 이용하여 데이터베이스에서 사용자를 찾습니다.
        UserEntity user = userRepository.findByUsername(username);

        // 사용자가 존재하지 않으면 중복되지 않은 것으로 판단합니다.
        return user == null;
    }
}