package com.gome.redline.utils.common;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Lanxiaowei at 2017/2/24 11:49
 * Java Reflection Utils
 */
public class ReflectUtils {
    public static <T>T createByConstruction(Class<T> targetClass,Class[] paramTypes,Object[] params) {
        try {
            return targetClass.getConstructor(paramTypes).newInstance(params);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
