package dydeve.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @see com.alibaba.fastjson.TypeReference
 * Created by dy on 2017/8/8.
 * @param <T>
 */
public abstract class TypeReference<T> {

    protected final Type type;

    protected TypeReference() {
        type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

}
