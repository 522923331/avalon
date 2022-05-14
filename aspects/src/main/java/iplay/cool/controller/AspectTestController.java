package iplay.cool.controller;

import iplay.cool.annotation.AfterAnno;
import iplay.cool.annotation.BeforeAnno;
import org.springframework.web.bind.annotation.*;

/**
 * @author dove
 * @date 2022/5/14 1:14
 */
@RequestMapping("/aspectTest")
@RestController
public class AspectTestController {

    @BeforeAnno("before切入点-1")
    @GetMapping("/before")
    public String beforeTest() {
        System.out.println("before测试-controller");
        return "before测试-controller";
    }

    @AfterAnno("interest")
    @PostMapping("after/{name}")
    public String afterTest(@PathVariable("name") String name) {
        System.out.println("after测试-controller");
        return name;
    }

    @PostMapping("pointCut")
    public String pointCutTest() {
        System.out.println("pointCutTest测试-controller");
        return "pointCutTest测试-controller";
    }

    @PostMapping("afterReturning/{i1}/{i2}")
    public String afterReturningTest(@PathVariable("i1") int i1, @PathVariable("i2") int i2) {
        System.out.println("afterReturningTest测试-controller");
        return "afterReturningTest测试-controller-->int总和:" + (i1 + i2);
    }

    @PostMapping("afterThrowing/{string}")
    public String afterThrowingTest(@PathVariable("string") String string) {
        System.out.println("afterThrowingTest测试-controller");
        int i = 1 / 0;
        return "afterThrowingTest测试end--->String值:" + string;
    }
}
