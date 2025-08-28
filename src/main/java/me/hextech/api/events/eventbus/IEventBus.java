package me.hextech.api.events.eventbus;

public interface IEventBus {
    void registerLambdaFactory(LambdaListener_AbcZKcjDwtaoZeuwqpJc.Factory var1);

    <T> T post(T var1);

    <T extends ICancellable> T post(T var1);

    void subscribe(Object var1);

    void subscribe(Class<?> var1);

    void subscribe(IListener var1);

    void unsubscribe(Object var1);

    void unsubscribe(Class<?> var1);

    void unsubscribe(IListener var1);
}
