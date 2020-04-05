package com.vinava.pofo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pofo/client")
public class ClientController {

    @GetMapping("get")
    private String clientTest() {
        if(true){
            return "POFO APPLICATION IS UP!!!";
        }
        return "OOPS";
    }

}
