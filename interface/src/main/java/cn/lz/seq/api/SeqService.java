package cn.lz.seq.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public interface SeqService {

    @RequestMapping("/seq")
    long getSeq(@RequestParam("token") String token);
}
