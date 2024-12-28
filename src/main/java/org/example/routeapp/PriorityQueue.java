


package org.example.routeapp;

public class PriorityQueue {
    private Node[] heap;
    private int size;

    PriorityQueue(){

        heap = new Node[5000];
        this.size = 0;
    }

      static class Node {
        String name;
        double distance;

        Node(String name,double distance){
            this.name = name;
            this.distance=distance;
        }
    }

    private int parent(int index){
        return (index-1)/2;
    }

    private int leftChild(int index){
        return 2*index+1;
    }

    private int rightChild(int index){
        return 2*index+2;
    }

    private void swap(int i,int j){
        Node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

    }

    public void add(String node,double distance){
        if (size >= heap.length) {
            throw new IllegalStateException("Priority queue is full");
        }
        Node newNode = new Node(node, distance);
        heap[size++] = newNode;

      bubbleUp(size-1);
        }

    private void bubbleUp(int index) {
        while (index > 0 && heap[index].distance< heap[parent(index)].distance) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

        public Node poll () {
            if (size == 0) throw new IllegalStateException("Priority queue is empty");
            Node min = heap[0];
            heap[0] = heap[size - 1];
              size--;
           bubbleDown(0);
            return min;
        }



    private void bubbleDown(int index) {
        int smallest = index;

        while (true) {
            int left = leftChild(index);
            int right = rightChild(index);

            // Find the smallest among the current node and its children
            if (left < size && heap[left].distance<heap[smallest].distance) {
                smallest = left;
            }
            if (right < size && heap[right].distance< heap[smallest].distance) {
                smallest = right;
            }

            // If the current node is smaller than both children, heap property is restored
            if (smallest == index) {
                break;
            }

            // Swap and continue
            swap(index, smallest);
            index = smallest;
        }
    }

    public boolean isEmpty(){
    return size==0;
    }


}

