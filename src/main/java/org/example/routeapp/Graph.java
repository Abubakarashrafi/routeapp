package org.example.routeapp;

import java.util.Random;
public class Graph {
    // Adjacency list
    private HashMap<String, HashMap<String, Edge>> graph;

    public Graph() {
        graph = new HashMap<>();
    }

    // Method to add a node
    public void addNode(String nodeName) {
        if (!graph.containsKey(nodeName)) {
            graph.put(nodeName, new HashMap<>());
        }
    }

    // Method to add an edge (from source -> to destination with weight)
    public void addEdge(String source, String destination, double weight) {
        // Add the nodes if they don't exist
        if (!graph.containsKey(source)) {
            addNode(source);
        }
        if (!graph.containsKey(destination)) {
            addNode(destination);
        }

        // Create an edge with adjusted weight
        Edge edge = new Edge(weight);

        // Add the edge from source to destination only if it doesn't already exist
        if (!graph.get(source).containsKey(destination)) {
            graph.get(source).put(destination, edge);
        }

        // Add the reverse edge from destination to source only if it doesn't already exist
        if (!graph.get(destination).containsKey(source)) {
            graph.get(destination).put(source, edge);
        }
    }

    // Method to get neighbors of a node
    public HashMap<String, Edge> getNeighbors(String nodeName) {
        return graph.get(nodeName);
    }

    // Method to get all node (keys) in the graph
    public String[] getAllNodes() {
        return graph.getAllKeys();
    }

    public static class Edge {
       private int weight;
        private final int trafficFactor;
       private final boolean isConstruction;

        // Constructor for the Edge
        Edge(double weight) {
            this.trafficFactor = setTrafficFactor();
            this.isConstruction = setConstruction();
            this.weight = calculateWeight((int) weight);  // Cast weight to int
        }

        // Random traffic factor (1 to 10)
        public int setTrafficFactor() {
            Random random = new Random();
            return 1 + random.nextInt(9); // Generates a value between 1 and 9
        }

        // Random construction (20% chance)
        public boolean setConstruction() {
            return Math.random() < 0.1;  // 10% chance
        }

        // Calculate the adjusted weight based on traffic factor and construction
        public int calculateWeight(int originalWeight) {
            double adjustedWeight = originalWeight;
            if (isConstruction) {
                adjustedWeight += originalWeight * 2;  // Add 200% if there's construction
            }
            return (int) (adjustedWeight + adjustedWeight * trafficFactor );  // Apply the traffic factor and return int
        }

        // Getter for weight (returns the weight as an integer)
        public int getWeight() {
            return this.weight;
        }


        public int getTrafficFactor() {
            return trafficFactor;
        }

        public boolean isConstruction() {
            return isConstruction;
        }
        }
}
