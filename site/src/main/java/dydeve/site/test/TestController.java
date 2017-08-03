package dydeve.site.test;

import dydeve.monitor.util.IPUtils;
import dydeve.site.web.handler.annotation.JsonBy;
import dydeve.site.web.handler.annotation.RequestJson;
import dydeve.site.web.handler.annotation.ResponseJson;
import dydeve.site.web.handler.annotation.WebObject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * Created by dy on 2017/7/21.
 */
@Controller
//@JsonBy(JsonBy.GSON)
public class TestController {

    @RequestMapping("/ok")
    @ResponseBody
    public Object ok(@WebObject(required = false) /*@Validated*/ A/*Map<String, String[]>*/ a, /*BindingResult result,*/ HttpServletRequest httpServletRequest) {
        /*if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }*/

        System.out.println(IPUtils.getHeaders(httpServletRequest));

        return a;
    }

    @RequestMapping(value = "/okk", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseJson
    public Object okk(@RequestJson("") A a, BindingResult result, HttpServletRequest httpServletRequest) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }
        Object b = a;
        return a;
    }

    @RequestMapping("/")
    public String index() {
        return "redirect:index.html";
    }

    @Nullable
    public static class A {

        @Min(value = 1, message = "a can't < 1")
        private int a;
        private float b;

        @Size(min = 2, max = 2, message = "size must be 2")
        private String c;
        private /*List<*/Integer[]/*>*/ d;//d=[]
        private /*List<*/Integer[]/*>*/ e;//e=..&e=..

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public float getB() {
            return b;
        }

        public void setB(float b) {
            this.b = b;
        }


        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public Integer[] getD() {
            return d;
        }

        public void setD(Integer[] d) {
            this.d = d;
        }

        public Integer[] getE() {
            return e;
        }

        public void setE(Integer[] e) {
            this.e = e;
        }
    }

    public static class B extends A {}


}
