package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) { // Model : 데이터를 view로 넘길수 있는 객체
        model.addAttribute("data","hello!!");
        return "hello"; // view이름은 기본적으로 /resources/templates/ 하위 경로에서 찾는다.
    }
}
