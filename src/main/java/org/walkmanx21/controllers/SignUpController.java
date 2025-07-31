package org.walkmanx21.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.models.User;

@Controller("/sign-up")
@RequestMapping("sign-up")
public class SignUpController {

    private UserDao userDao;

    @Autowired
    public SignUpController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping
    public String newUserRequestDto(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto) {
        return "/sign-up";
    }

    @PostMapping
    public String signUp(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto) {
        userDao.insertUser(userRequestDto);
        return "redirect:/index";
    }
}
