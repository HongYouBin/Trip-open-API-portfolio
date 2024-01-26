package com.ssafy.i5i.hotelAPI.domain.user.controller;

import com.ssafy.i5i.hotelAPI.common.response.CommonResponse;
import com.ssafy.i5i.hotelAPI.domain.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/docs/email")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/{email}")
    public CommonResponse sendMail(@PathVariable("email") String email) {
        emailService.sendEmail(email);
        return new CommonResponse(200, "success");
    }

    @GetMapping("auth/{email}/{code}")
    public CommonResponse authMail(@PathVariable("email") String email, @PathVariable("code") Long code) {
        emailService.checkMail(email, code);
        return new CommonResponse(200, "success");
    }
}
