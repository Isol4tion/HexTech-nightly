package me.hextech.remapped;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.IListener;
import me.hextech.remapped.LambdaListener;

public class LambdaListener_AbcZKcjDwtaoZeuwqpJc
implements IListener {
    private static boolean isJava1dot8 = System.getProperty("java.version").startsWith("1.8");
    private static Constructor<MethodHandles.Lookup> lookupConstructor;
    private static Method privateLookupInMethod;
    private final Class<?> target;
    private final boolean isStatic;
    private final int priority;
    private Consumer<Object> executor;

    public LambdaListener_AbcZKcjDwtaoZeuwqpJc(LambdaListener factory, Class<?> klass, Object object, Method method) {
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
            MethodHandle lambdaFactory = LambdaMetafactory.metafactory(lookup, "accept", invokedType, MethodType.methodType(Void.TYPE, Object.class), methodHandle, methodType).getTarget();
            this.executor = this.isStatic ? lambdaFactory.invoke() : lambdaFactory.invoke(object);
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
        if (isJava1dot8) {
            me.rebirthclient.api.events.eventbus.LambdaListener.lookupConstructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
        } else {
            privateLookupInMethod = MethodHandles.class.getDeclaredMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
        }
    }
}
