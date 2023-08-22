package com.dndoz.PosePicker.Controller;

import com.dndoz.PosePicker.Dto.LoginResponse;
import com.dndoz.PosePicker.Service.UserService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@Api(tags = {"유저 API"})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/login/oauth/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code){
        try{
            return ResponseEntity.ok(userService.kakaoLogin(code));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item Not Found");
        }
    }

}

