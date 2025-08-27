package me.hextech.remapped;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import me.hextech.remapped.EventBus;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.ICancellable;
import me.hextech.remapped.IEventBus;
import me.hextech.remapped.IListener;
import me.hextech.remapped.LambdaListener;
import me.hextech.remapped.LambdaListener_AbcZKcjDwtaoZeuwqpJc;
import me.hextech.remapped.NoLambdaFactoryException;

public class EventBus_bcCTmtyubWDfbrlTrSdl
implements IEventBus {
    public final Map<Class<?>, List<IListener>> listenerMap = new ConcurrentHashMap();
    public final List<EventBus> lambdaFactoryInfos = new CopyOnWriteArrayList<EventBus>();
    private final Map<Object, List<IListener>> listenerCache = new ConcurrentHashMap<Object, List<IListener>>();
    private final Map<Class<?>, List<IListener>> staticListenerCache = new ConcurrentHashMap();

    @Override
    public void registerLambdaFactory(LambdaListener factory) {
        this.lambdaFactoryInfos.add(new EventBus(factory));
    }

    @Override
    public <T> T post(T event) {
        List<IListener> listeners = this.listenerMap.get(event.getClass());
        if (listeners != null) {
            for (IListener listener : listeners) {
                listener.call(event);
            }
        }
        return event;
    }

    @Override
    public <T extends ICancellable> T post(T event) {
        List<IListener> listeners = this.listenerMap.get(event.getClass());
        if (listeners != null) {
            event.setCancelled(false);
            for (IListener listener : listeners) {
                listener.call(event);
                if (!event.isCancelled()) continue;
                break;
            }
        }
        return event;
    }

    @Override
    public void subscribe(Object object) {
        this.subscribe(this.getListeners(object.getClass(), object), false);
    }

    @Override
    public void subscribe(Class<?> klass) {
        this.subscribe(this.getListeners(klass, null), true);
    }

    @Override
    public void subscribe(IListener listener) {
        this.subscribe(listener, false);
    }

    private void subscribe(List<IListener> listeners, boolean onlyStatic) {
        for (IListener listener : listeners) {
            this.subscribe(listener, onlyStatic);
        }
    }

    private void subscribe(IListener listener, boolean onlyStatic) {
        if (onlyStatic) {
            if (listener.isStatic()) {
                this.insert(this.listenerMap.computeIfAbsent(listener.getTarget(), aClass -> new CopyOnWriteArrayList()), listener);
            }
        } else {
            this.insert(this.listenerMap.computeIfAbsent(listener.getTarget(), aClass -> new CopyOnWriteArrayList()), listener);
        }
    }

    private void insert(List<IListener> listeners, IListener listener) {
        int i;
        for (i = 0; i < listeners.size() && listener.getPriority() <= listeners.get(i).getPriority(); ++i) {
        }
        listeners.add(i, listener);
    }

    @Override
    public void unsubscribe(Object object) {
        this.unsubscribe(this.getListeners(object.getClass(), object), false);
    }

    @Override
    public void unsubscribe(Class<?> klass) {
        this.unsubscribe(this.getListeners(klass, null), true);
    }

    @Override
    public void unsubscribe(IListener listener) {
        this.unsubscribe(listener, false);
    }

    private void unsubscribe(List<IListener> listeners, boolean staticOnly) {
        for (IListener listener : listeners) {
            this.unsubscribe(listener, staticOnly);
        }
    }

    private void unsubscribe(IListener listener, boolean staticOnly) {
        List<IListener> l = this.listenerMap.get(listener.getTarget());
        if (l != null) {
            if (staticOnly) {
                if (listener.isStatic()) {
                    l.remove(listener);
                }
            } else {
                l.remove(listener);
            }
        }
    }

    private List<IListener> getListeners(Class<?> klass, Object object) {
        Function<Object, List> func = o -> {
            CopyOnWriteArrayList<IListener> listeners = new CopyOnWriteArrayList<IListener>();
            this.getListeners(listeners, klass, object);
            return listeners;
        };
        if (object == null) {
            return this.staticListenerCache.computeIfAbsent(klass, func);
        }
        for (Object key : this.listenerCache.keySet()) {
            if (key != object) continue;
            return this.listenerCache.get(object);
        }
        List listeners = func.apply(object);
        this.listenerCache.put(object, listeners);
        return listeners;
    }

    private void getListeners(List<IListener> listeners, Class<?> klass, Object object) {
        for (Method method : klass.getDeclaredMethods()) {
            if (!this.isValid(method)) continue;
            listeners.add(new LambdaListener_AbcZKcjDwtaoZeuwqpJc(this.getLambdaFactory(klass), klass, object, method));
        }
        if (klass.getSuperclass() != null) {
            this.getListeners(listeners, klass.getSuperclass(), object);
        }
    }

    private boolean isValid(Method method) {
        if (!method.isAnnotationPresent(EventHandler.class)) {
            return false;
        }
        if (method.getReturnType() != Void.TYPE) {
            return false;
        }
        if (method.getParameterCount() != 1) {
            return false;
        }
        return !method.getParameters()[0].getType().isPrimitive();
    }

    private LambdaListener getLambdaFactory(Class<?> klass) {
        Iterator<EventBus> iterator = this.lambdaFactoryInfos.iterator();
        if (iterator.hasNext()) {
            EventBus info = iterator.next();
            return info.factory;
        }
        throw new NoLambdaFactoryException(klass);
    }
}
