package com.iscas.workingdiarys.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/sse")
public class SSEController {
    @RequestMapping(value = "getdata", produces = "text/event-stream;charsert=UTF-8")
    public String push(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "实时股价："+Math.random();
    }
}
