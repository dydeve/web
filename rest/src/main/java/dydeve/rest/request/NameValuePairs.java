package dydeve.rest.request;

import com.google.common.collect.Lists;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by yuduy on 2017/8/11.
 */
public class NameValuePairs {

    public static List<NameValuePair> fromMap(@NotEmpty Map<String, Object> kvs) {
        List<NameValuePair> nameValuePairs = Lists.newArrayListWithCapacity(kvs.size());
        for (Map.Entry<String, Object> entry : kvs.entrySet()) {
            addNameValuePair(nameValuePairs, entry.getKey(), entry.getValue());
        }
        return nameValuePairs;
    }

    public static List<NameValuePair> fromObject(@NotEmpty Object object) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
        List<NameValuePair> nameValuePairs = Lists.newArrayListWithCapacity(proDescrtptors.length);
        for (PropertyDescriptor descriptor : proDescrtptors) {
            addNameValuePair(nameValuePairs, descriptor.getName(), descriptor.getReadMethod().invoke(object));
        }
        return nameValuePairs;
    }

    private static void addNameValuePair(List<NameValuePair> nameValuePairs, String name, Object value) {
        if (BeanUtils.isSimpleValueType(value.getClass())) {
            nameValuePairs.add(new BasicNameValuePair(name, value.toString()));
            return;
        }

        if (value instanceof Collection) {
            for (Object v : ((Collection) value)) {
                nameValuePairs.add(new BasicNameValuePair(name, v.toString()));
            }
            return;
        }

        if (value.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(value); i++) {
                nameValuePairs.add(new BasicNameValuePair(name, Array.get(value, i).toString()));
            }
            return;
        }
        throw new UnsupportedOperationException();
    }

}
