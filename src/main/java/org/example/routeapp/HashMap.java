
package org.example.routeapp;

public class HashMap<K,V> {
    private int size = 0;
    private int capacity = 16;
    private final float loadfactor = 0.75f;


    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }


    HashMap() {
        table = new Node[capacity];
    }

    private Node<K, V>[] table;

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> newEntry = new Node<>(key, value);

        if (table[index] == null)
            table[index] = newEntry;
        else {
            Node<K, V> current = table[index];
            while (current.next != null && !(current.key.equals(key))) {
                current = current.next;
            }
            if (current.key.equals(key)) {
                current.value = value; // Update the value
                return;
            } else {
                current.next = newEntry; // Add new entity
            }
        }
        size++;

        if ((float) size / capacity > loadfactor) {
            resize();
        }


    }

    private void resize() {
        capacity *= 2; // Double the capacity
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;

        for (Node<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value); // Rehash all entries
                entry = entry.next;
            }
        }
    }

    public V get(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        int index = hash(key);
        Node current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public String[] getAllKeys() {
        String[] keys = new String[size]; // Array to store keys
        int index = 0;
        for (Node<K, V> entry : table) {
            while (entry != null) {
                keys[index++] = entry.key.toString(); // Add the key to the array
                entry = entry.next;
            }
        }
        return keys; // Return the array of keys
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    size=0;
    }

}



