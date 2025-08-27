package me.hextech.remapped;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * Exception performing whole class analysis ignored.
 */
public static interface LambdaListener {
    public MethodHandles.Lookup create(Method var1, Class<?> var2) throws InvocationTargetException, IllegalAccessException;
}
