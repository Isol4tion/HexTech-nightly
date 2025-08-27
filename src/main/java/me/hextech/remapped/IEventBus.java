package me.hextech.remapped;

public interface IEventBus {
    public void registerLambdaFactory(LambdaListener_AbcZKcjDwtaoZeuwqpJc.Factory var1);

    public <T> T post(T var1);

    public <T extends ICancellable> T post(T var1);

    public void subscribe(Object var1);

    public void subscribe(Class<?> var1);

    public void subscribe(IListener var1);

    public void unsubscribe(Object var1);

    public void unsubscribe(Class<?> var1);

    public void unsubscribe(IListener var1);
}
