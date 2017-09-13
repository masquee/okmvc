package okmvc.controller;

import lombok.Getter;
import lombok.Setter;
import okmvc.annotation.Autowired;
import okmvc.annotation.Controller;
import okmvc.annotation.GetMapping;
import okmvc.mvc.JsonData;
import okmvc.service.HelloService;

import java.util.Map;

@Getter
@Setter
@Controller
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello-abc")
    public Map<String, Object> test(Map<String, Object> param) {

        helloService.doService();
        JsonData data = new JsonData();
//        data.addAttribute("param", param);
        return param;
    }
}
