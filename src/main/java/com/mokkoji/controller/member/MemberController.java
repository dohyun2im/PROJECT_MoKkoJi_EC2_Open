package com.mokkoji.controller.member;

import com.mokkoji.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/getLoginInfo")
    public String getLoginInfo(){
        return memberService.getloginMemail();
    }
}
