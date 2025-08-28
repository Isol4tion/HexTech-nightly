package me.hextech.remapped;

import me.hextech.remapped.api.events.eventbus.EventHandler;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

public class LambdaListener_AbcZKcjDwtaoZeuwqpJc
implements IListener {
    private static final boolean isJava1dot8 = System.getProperty("java.version").startsWith("1.8");
    private static Constructor<MethodHandles.Lookup> lookupConstructor;
    private static Method privateLookupInMethod;
    private final Class<?> target;
    private final boolean isStatic;
    private final int priority;
    private Consumer<Object> executor;

    public LambdaListener_AbcZKcjDwtaoZeuwqpJc(Factory factory, Class<?> klass, Object object, Method method) {
        this.target = method.getParameters()[0].getType();
        this.isStatic = Modifier.isStatic(method.getModifiers());
        this.priority = method.getAnnotation(EventHandler.class).priority();
        try {
            MethodType invokedType;
            MethodHandle methodHandle;
            MethodHandles.Lookup lookup;
            String name = method.getName();
            if (isJava1dot8) {
                boolean a = lookupConstructor.isAccessible();
                lookupConstructor.setAccessible(true);
                lookup = lookupConstructor.newInstance(klass);
                lookupConstructor.setAccessible(a);
            } else {
                lookup = factory.create(privateLookupInMethod, klass);
            }
            MethodType methodType = MethodType.methodType(Void.TYPE, method.getParameters()[0].getType());
            if (this.isStatic) {
                methodHandle = lookup.findStatic(klass, name, methodType);
                invokedType = MethodType.methodType(Consumer.class);
            } else {
                methodHandle = lookup.findVirtual(klass, name, methodType);
                invokedType = MethodType.methodType(Consumer.class, klass);
            }
            final MethodHandle lambdaFactory = LambdaMetafactory.metafactory(lookup, "accept", invokedType, MethodType.methodType(Void.TYPE, Object.class), methodHandle, methodType).getTarget();
            if (this.isStatic) {
                this.executor = (Consumer<Object>)lambdaFactory.invoke();
            }
            else {
                this.executor = (Consumer<Object>)lambdaFactory.invoke(object);
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void call(Object event) {
        this.executor.accept(event);
    }

    @Override
    public Class<?> getTarget() {
        return this.target;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public boolean isStatic() {
        return this.isStatic;
    }

    static {
        try {
            if (isJava1dot8) {
                lookupConstructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
            } else {
                privateLookupInMethod = MethodHandles.class.getDeclaredMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public interface Factory {
        MethodHandles.Lookup create(Method var1, Class<?> var2) throws InvocationTargetException, IllegalAccessException;
    }
}
