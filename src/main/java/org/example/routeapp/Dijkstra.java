package org.example.routeapp;

public class Dijkstra {

    private  HashMap<String, Double> distances; // Stores shortest distance from source to each node
    private  HashMap<String, String> previousNodes; // Stores the previous node for path reconstruction
    private PriorityQueue pq; // priority queue to store nodes with their distances

    public Dijkstra() {
        distances = new HashMap<>();
        previousNodes = new HashMap<>();
        pq = new PriorityQueue();
    }

    // Method to calculate the shortest path using Dijkstra's algorithm
    public void calculateShortestPath(String startNode, Graph graph) {
        // Initialize distances for all nodes
        for (String node : graph.getAllNodes()) {
            distances.put(node, Double.MAX_VALUE); // Start with infinite distance
            previousNodes.put(node, null); // No previous node initially
            pq.add(node, Double.MAX_VALUE); // Add node with infinite distance to priority queue
        }

        distances.put(startNode, 0.0); // Set the distance to the start node as 0
        pq.add(startNode, 0.0); // Add the start node with distance 0

        while (!pq.isEmpty()) {
            // Get the node with the smallest distance
            PriorityQueue.Node currentNode = pq.poll();

            // If the current node's distance is still infinite, stop processing
            if (distances.get(currentNode.name) == Double.MAX_VALUE) {
                break;
            }

            // Get neighbors of the current node from the graph
            HashMap<String, Graph.Edge> neighbors = graph.getNeighbors(currentNode.name);
            for (String neighbor : neighbors.getAllKeys()) {
                // Calculate the tentative distance to this neighbor
                double newDist = distances.get(currentNode.name) + neighbors.get(neighbor).getWeight();

                // If the new distance is smaller, update it
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, currentNode.name);
                    pq.add(neighbor, newDist); // Update the priority queue with the new distance
                }
            }
        }
    }

    // Method to get the shortest distance to a node
    public double getDistance(String node) {
        return distances.get(node);
    }
    // Method to get the shortest path from start to end
    public String getShortestPath(String endNode) {
        StringBuilder path = new StringBuilder();
        String currentNode = endNode;

        // Reconstruct the path from the end node to the start node
        while (currentNode != null) {
            path.insert(0, currentNode + " ");
            currentNode = previousNodes.get(currentNode);
        }

        return path.toString().trim();
    }


}

