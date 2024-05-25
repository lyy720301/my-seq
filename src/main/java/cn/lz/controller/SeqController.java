package cn.lz.controller;

import cn.lz.service.SeqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class SeqController {

    @Autowired
    private SeqService seqService;

//    @GetMapping("/seq/{token}")
//    public long seq(@PathVariable("token") String token) {
//        long seq = seqService.getSeq(token);
//        log.info("req token: {} res seq: {}", token, seq);
//        return seq;
//    }

    @GetMapping("/seq")
    @ResponseBody
    public long seq(@RequestParam("token") String token) {
        long seq = seqService.getSeq(token);
        log.info("req token: {} res seq: {}", token, seq);
        return seq;
    }
}
