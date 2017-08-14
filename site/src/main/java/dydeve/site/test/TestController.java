package dydeve.site.test;

import com.alibaba.fastjson.TypeReference;
import dydeve.monitor.aop.Trace;
import dydeve.monitor.util.IPUtils;
import dydeve.site.web.exception.CustomServerException;
import dydeve.site.web.handler.annotation.RequestJson;
import dydeve.site.web.handler.annotation.ResponseJson;
import dydeve.site.web.handler.annotation.WebObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by dy on 2017/7/21.
 */
@Controller
//@JsonBy(JsonBy.GSON)
public class TestController {

    @Trace(value = "haha")
    @RequestMapping("/ok")
    @ResponseJson
    public Object ok(@WebObject(required = false) /*@Validated*/ A/*Map<String, String[]>*/ a, /*BindingResult result,*/ HttpServletRequest httpServletRequest) throws CustomServerException {
        /*if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }*/

        if (true) {
            if (a.a == 1) {
                throw new CustomServerException("", 1);
            } else {
                throw new RuntimeException("asdf");
            }
        }

        System.out.println(IPUtils.getHeaders(httpServletRequest));

        return a;
    }

    @RequestMapping(value = "/okk", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseJson
    public Object okk(@RequestJson("") A a, BindingResult result, HttpServletRequest httpServletRequest) throws CustomServerException {

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }
        Object b = a;
        return a;
    }

    @Autowired
    private CloseableHttpAsyncClient httpAsyncClient;

    @RequestMapping("/haha")
    public String aaaa() {
        HttpGet httpGet = new HttpGet("http://httpbin.org/");
        Future<HttpResponse> future = httpAsyncClient.execute(httpGet, null);
        try {
            HttpResponse httpResponse = future.get();
            String a = EntityUtils.toString(httpResponse.getEntity());
            return a;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public static void main(String[] args) {
        Map<String, List<String>> map1;
        Map<String, Collection<String>> map2;

        TestController c = new TestController();

        c.a(new TypeReference<Map<String, String[]>>() {}, Map.class);
        c.a(new TypeReference<Map<String, Collection>>() {}, Map.class);
        c.a(new TypeReference<Map>() {}, Map.class);

    }


    public <T> void t(T obj, Class<T> clazz) {



        new TypeReference<T>(){};
    }

    public <T> void  a(TypeReference<T> object, Class<? super T> clazz) {
        Type type = object.getType();
        Class<?> z = type.getClass();
        /*Class<?> x = (Class<?>)type;*/
        if (Map.class.isAssignableFrom(clazz)) {
            if (type instanceof ParameterizedType) {//in case of map without generic
                ParameterizedType mapGenericType = (ParameterizedType) type;

                Type a = mapGenericType.getActualTypeArguments()[0];
                Type b = mapGenericType.getActualTypeArguments()[1];

                if (String.class.equals(mapGenericType.getActualTypeArguments()[0])
                        && String[].class.equals(mapGenericType.getActualTypeArguments()[1])) {
                    return;
                } else {
                    //throw new UnsupportedOperationException("just support map in form of map<String, String[]>");
                }
            }

        } else {
            Class<?> a = (Class<?>)type;
        }
    }

}
