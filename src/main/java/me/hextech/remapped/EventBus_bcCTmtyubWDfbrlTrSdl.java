package me.hextech.remapped;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class EventBus_bcCTmtyubWDfbrlTrSdl implements IEventBus {
   public final Map<Class<?>, List<IListener>> listenerMap = new ConcurrentHashMap();
   public final List<EventBus> lambdaFactoryInfos = new CopyOnWriteArrayList();
   private final Map<Object, List<IListener>> listenerCache = new ConcurrentHashMap();
   private final Map<Class<?>, List<IListener>> staticListenerCache = new ConcurrentHashMap();

   @Override
   public void registerLambdaFactory(LambdaListener factory) {
      this.lambdaFactoryInfos.add(new EventBus(factory));
   }

   @Override
   public <T> T post(T event) {
      List<IListener> listeners = (List<IListener>)this.listenerMap.get(event.getClass());
      if (listeners != null) {
         for (IListener listener : listeners) {
            listener.call(event);
         }
      }

      return event;
   }

   @Override
   public <T extends ICancellable> T post(T event) {
      List<IListener> listeners = (List<IListener>)this.listenerMap.get(event.getClass());
      if (listeners != null) {
         event.setCancelled(false);

         for (IListener listener : listeners) {
            listener.call(event);
            if (event.isCancelled()) {
               break;
            }
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
            this.insert((List<IListener>)this.listenerMap.computeIfAbsent(listener.getTarget(), aClass -> new CopyOnWriteArrayList()), listener);
         }
      } else {
         this.insert((List<IListener>)this.listenerMap.computeIfAbsent(listener.getTarget(), aClass -> new CopyOnWriteArrayList()), listener);
      }
   }

   private void insert(List<IListener> listeners, IListener listener) {
      int i = 0;

      while (i < listeners.size() && listener.getPriority() <= ((IListener)listeners.get(i)).getPriority()) {
         i++;
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
      List<IListener> l = (List<IListener>)this.listenerMap.get(listener.getTarget());
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
      Function<Object, List<IListener>> func = o -> {
         List<IListener> listeners = new CopyOnWriteArrayList();
         this.getListeners(listeners, klass, object);
         return listeners;
      };
      if (object == null) {
         return (List<IListener>)this.staticListenerCache.computeIfAbsent(klass, func);
      } else {
         for (Object key : this.listenerCache.keySet()) {
            if (key == object) {
               return (List<IListener>)this.listenerCache.get(object);
            }
         }

         List<IListener> listeners = (List<IListener>)func.apply(object);
         this.listenerCache.put(object, listeners);
         return listeners;
      }
   }

   private void getListeners(List<IListener> listeners, Class<?> klass, Object object) {
      for (Method method : klass.getDeclaredMethods()) {
         if (this.isValid(method)) {
            listeners.add(new LambdaListener_AbcZKcjDwtaoZeuwqpJc(this.getLambdaFactory(klass), klass, object, method));
         }
      }

      if (klass.getSuperclass() != null) {
         this.getListeners(listeners, klass.getSuperclass(), object);
      }
   }

   private boolean isValid(Method method) {
      if (!method.isAnnotationPresent(EventHandler.class)) {
         return false;
      } else if (method.getReturnType() != void.class) {
         return false;
      } else {
         return method.getParameterCount() != 1 ? false : !method.getParameters()[0].getType().isPrimitive();
      }
   }

   private LambdaListener getLambdaFactory(Class<?> klass) {
      Iterator var2 = this.lambdaFactoryInfos.iterator();
      if (var2.hasNext()) {
         EventBus info = (EventBus)var2.next();
         return info.factory;
      } else {
         throw new NoLambdaFactoryException(klass);
      }
   }
}
