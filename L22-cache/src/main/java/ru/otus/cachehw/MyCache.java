package ru.otus.cachehw;


import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final WeakHashMap<K, V> cache = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = new HashSet<>();
    private final PhantomQueueListener<K, V> phantomQueueListener = new PhantomQueueListener<>(listeners);

    {
        phantomQueueListener.start();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        phantomQueueListener.add(key);
        listeners.forEach(listener -> listener.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        V remove = cache.remove(key);
        listeners.forEach(listener -> listener.notify(key, remove, "remove"));
    }

    @Override
    public V get(K key) {
        V result = cache.get(key);
        listeners.forEach(listener -> listener.notify(key, result, "get"));
        return result;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private static class PhantomWatcher<K> extends PhantomReference<K> {
        // Сохраним тут строковое представление ключа, чтоб хоть как-то понимать что же было удалено сборщиком мусора
        private final String keyDescription;

        private PhantomWatcher(K key, ReferenceQueue<? super K> q) {
            super(key, q);
            // Сделаем копию, если K и так строка
            this.keyDescription = new String(key.toString());
        }

        public String getKeyDescription() {
            return keyDescription;
        }
    }

    private static class PhantomQueueListener<K, V> extends Thread {
        private final Collection<PhantomWatcher<K>> watchers = Collections.synchronizedCollection(new HashSet<>());
        private final ReferenceQueue<K> referenceQueue = new ReferenceQueue<>();
        private final Set<HwListener<K, V>> listeners;

        private PhantomQueueListener(Set<HwListener<K, V>> listeners) {
            this.listeners = listeners;
            this.setDaemon(true);
        }

        @Override
        public void run() {

            while (true) {
                try {
                    PhantomWatcher<K> remove = (PhantomWatcher<K>) referenceQueue.remove();
                    watchers.remove(remove);
                    // Объекты уже удалены, я не очень понимаю, что можно передать здесь кроме null.
                    listeners.forEach(listener ->
                            listener.notify(null, null, "Killed by gc: " + remove.getKeyDescription()));

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void add(K key) {
            PhantomWatcher<K> watcher = new PhantomWatcher<>(key, referenceQueue);
            watchers.add(watcher);
        }
    }
}
