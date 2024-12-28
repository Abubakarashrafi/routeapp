package org.example.routeapp;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.Random;

public class RouteAppGUI extends Application {

    Random random = new Random();
    private Graph graph;
    private Dijkstra dijkstra;
    private String startNode;
    private String endNode;
    private HashMap<String, double[]> nodePositions; // HashMap for node positions
    private Button addEdgeButton;
    private Button prebuilt;
    private Button findShortestPathButton;
    private Button reset;
    private Label startNodeLabel;
    private Label endNodeLabel;
    private final int edgeWeight = 1+random.nextInt(20);  // Default weight for the edge
    private GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize graph, dijkstra, and cities
        graph = new Graph();
        dijkstra = new Dijkstra();
        nodePositions = new HashMap<>();

        // Initialize Canvas
        Canvas canvas = new Canvas(1200, 900);
        gc = canvas.getGraphicsContext2D();
        

        addEdgeButton = new Button("Add Edge");
        addEdgeButton.setDisable(true);
        addEdgeButton.setOnAction(event -> addEdge());

        prebuilt = new Button("Pre Built");
        prebuilt.setDisable(false);
        prebuilt.setOnAction(event -> prebuilt());

        reset = new Button("Reset");
        reset.setOnAction(event -> reset());

        findShortestPathButton = new Button("Find Shortest Path");
        findShortestPathButton.setDisable(true);
        findShortestPathButton.setOnAction(event -> findShortestPath(gc));

        startNodeLabel = new Label("Start Node: Not Selected");
        endNodeLabel = new Label("End Node: Not Selected");

        startNodeLabel.setTextFill(Color.BLACK);  // Set text color for labels
        endNodeLabel.setTextFill(Color.BLACK);

        // Set font size for better visibility
        startNodeLabel.setFont(new Font(14));
        endNodeLabel.setFont(new Font(14));

        // Position the labels
        startNodeLabel.setTranslateY(180);  // Position above the Reset button
        startNodeLabel.setTranslateX(-130);  // Adjust position to your needs
        endNodeLabel.setTranslateY(210);
        endNodeLabel.setTranslateX(-130);
        addEdgeButton.setTranslateY(180);  // Position the add edge button below the canvas
        findShortestPathButton.setTranslateY(180); // Position the find shortest path button
        findShortestPathButton.setTranslateX(100); // Position the find shortest path button
        prebuilt.setTranslateY(225);
        reset.setTranslateY(225);
        reset.setTranslateX(70);

        // Set up the layout
        StackPane root = new StackPane();
        root.getChildren().addAll(canvas, addEdgeButton, findShortestPathButton, prebuilt,reset,startNodeLabel,endNodeLabel);
        // Set up the scene and stage
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Route App - Dijkstra Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Mouse click event to select cities for adding edge
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleMouseClick(event, gc));
        // Double-click event to add a new node at the clicked position
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {  // Double-click detected
                double x = event.getX();
                double y = event.getY();
                char letter = (char) ('A' + graph.getAllNodes().length);
                String newNode = String.valueOf(letter);
                nodePositions.put(newNode, new double[]{x, y}); // Add the city at the clicked position
                graph.addNode(newNode); // Add the node to the graph

                drawGraph(gc);  // Redraw the graph with the new node
            }
        });

      
    }
    
    private void reset() {
            graph = new Graph(); // Reset the graph
            nodePositions.clear(); // Clear the city positions
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            startNodeLabel.setText("Start Node: Not Selected");
            endNodeLabel.setText("End Node: Not Selected");
            System.out.println("Screen cleared and graph reset");
        }


    private void prebuilt() {
       reset();

        double[][] positions = {
                {392.6666666666667, 183.33333333333331}, // A
                {355.3333333333333, 312.6666666666667},  // B
                {379.3333333333333, 470.66666666666663}, // C
                {678.0, 168.66666666666666},             // D
                {888.6666666666666, 196.66666666666666}, // E
                {533.3333333333334, 259.3333333333333},  // F
                {764.6666666666666, 265.3333333333333},  // G
                {875.3333333333334, 329.3333333333333},  // H
                {869.3333333333334, 460.0},              // I
                {640.6666666666666, 464.66666666666663}, // J
                {522.6666666666666, 386.0},              // K
                {764.6666666666666, 386.0}               // L
        };

        for (int i = 0; i < positions.length; i++) {
            char letter = (char) ('A' + i);
             String newNode = String.valueOf(letter);
             nodePositions.put(newNode, positions[i]);
             graph.addNode(newNode);
    }
        String[][] edges = { {"A", "B"}, {"A", "D"}, {"A", "F"}, {"B", "C"}, {"B", "K"}, {"B", "F"}, {"C", "K"}, {"C", "J"}, {"D", "F"}, {"D", "G"}, {"D", "E"}, {"E", "G"}, {"E", "H"}, {"F", "G"}, {"F", "K"},{"G", "L"}, {"H", "L"}, {"H", "I"}, {"I", "L"}, {"I", "J"}, {"J", "L"}, {"J", "K"}, {"K", "L"} };

        for (String[] edge : edges) {
            int randomWeight =1+random.nextInt(20);  // Random weight between 0 and 20 graph.
             graph.addEdge(edge[0], edge[1], randomWeight);
        }
        drawGraph(gc);
    }

    // Method to add an edge between the selected cities
    private void addEdge() {
        if (startNode != null && endNode != null && !endNode.equals(startNode)) {
            // Add the edge with the specified weight
            graph.addEdge(startNode, endNode, edgeWeight);
            System.out.println("Edge added between: " + startNode + " and " + endNode);

            // Reset cities and disable the button again after adding the edge
            startNode = null;
            endNode = null;
            startNodeLabel.setText("Start Node: Not Selected");
            endNodeLabel.setText("End Node: Not Selected");
            addEdgeButton.setDisable(true);  // Disable the Add Edge button again
            findShortestPathButton.setDisable(true);  // Disable the Find Shortest Path button

            // Redraw the graph (after adding the edge)
            drawGraph(gc);
        }
    }

    // Method to draw the graph: cities (nodes) and their edges (connections)

    private void drawGraph(GraphicsContext gc) {
        gc.clearRect(0, 0, 1800, 1800);  // Clear the canvas

        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);
        for (int i = 0; i < 1800; i += 50) {
            gc.strokeLine(i, 0, i, 1800);  // Vertical lines
            gc.strokeLine(0, i, 1800, i);  // Horizontal lines
        }

        // Draw edges
        for (String city : graph.getAllNodes()) {
            double[] position = nodePositions.get(city);
            HashMap<String, Graph.Edge> neighbors = graph.getNeighbors(city);
            for (String neighbor : neighbors.getAllKeys()) {
                Graph.Edge edge = neighbors.get(neighbor);
                double[] neighborPosition = nodePositions.get(neighbor);

                // Set edge color based on conditions
                if (edge.isConstruction()) {
                    gc.setStroke(Color.DARKGOLDENROD);
                } else if (edge.getTrafficFactor() >= 8) {
                    gc.setStroke(Color.DEEPPINK);
                } else {
                    gc.setStroke(Color.LIGHTGRAY);
                }
                gc.setLineWidth(2);

                // Draw edge (line between cities)
                gc.strokeLine(position[0], position[1], neighborPosition[0], neighborPosition[1]);

                // Display edge weight at the midpoint with offset
                double midX = (position[0] + neighborPosition[0]) / 2;
                double midY = (position[1] + neighborPosition[1]) / 2;

                // Adjust weight position slightly to avoid overlap
                double offset = 15;  // Adjust this value for better spacing
                gc.setFill(Color.BLACK);
                gc.setFont(new Font(13));
                gc.fillText(edge.getWeight()+"", midX + offset, midY + offset);
            }
        }

        // Draw nodes (cities) with map-like representation
        for (String city : graph.getAllNodes()) {
            double[] position = nodePositions.get(city);

            // Set gradient color for cities
            gc.setFill(new Color(0.2, 0.3, 0.5, 1));  // Dark blue for city nodes
            gc.fillOval(position[0] - 15, position[1] - 15, 28, 28); // Outer circle

            gc.setFill(Color.WHITE);  // Inner circle
            gc.fillOval(position[0] - 10, position[1] - 10, 20, 20);  // City name background

            gc.setFill(Color.BLACK);
            gc.setFont(new Font(14));
            gc.fillText(city, position[0] - 5, position[1] + 5); // City name in the center of the node
        }
    }

    // Method to find and display the shortest path

    private void findShortestPath(GraphicsContext gc) {
        if (startNode != null && endNode != null) {
            findShortestPathButton.setDisable(true); // Disable the button after calculation

            // Calculate the shortest path using Dijkstra's algorithm
            dijkstra.calculateShortestPath(startNode, graph);


            // Get the shortest path and distance
            String path = dijkstra.getShortestPath(endNode);
            System.out.println("Path: "+path);
            double distance = dijkstra.getDistance(endNode);
            System.out.println("Total distance: "+distance);
            // Display the path and distance on the canvas
            gc.setFill(Color.GREEN);

            // Redraw the graph
            drawGraph(gc);

            // Highlight the shortest path on the graph
            highlightShortestPath(gc, path);

            // Reset start and end city after the calculation
            startNode = null;
            endNode = null;
            startNodeLabel.setText("Start Node: Not Selected");
            endNodeLabel.setText("End Node: Not Selected");

            addEdgeButton.setDisable(true);  // Disable the Add Edge button
            findShortestPathButton.setDisable(true);  // Disable the Find Shortest Path button
        } else {
            System.out.println("Please select a start and end city.");
        }
    }

    // Method to highlight the shortest path on the canvas
    private void highlightShortestPath(GraphicsContext gc, String path) {
        // Split the path into individual cities
        String[] citiesInPath = path.split(" ");

        // Prepare a timeline to animate the path drawing
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Current segment index and position trackers
        final int[] currentIndex = {0};  // Track the current segment being drawn
        double[] currentPosition = new double[2];  // Track the current position of the line being drawn

        // Initialize the current position with the first city
        String startNode = citiesInPath[0];
        double[] startPosition = nodePositions.get(startNode);
        if (startPosition != null) {
            currentPosition[0] = startPosition[0];
            currentPosition[1] = startPosition[1];
        }

        // List of path segments to draw
        KeyFrame keyFrame = new KeyFrame(Duration.millis(90), e -> {
            if (currentIndex[0] < citiesInPath.length - 1) {
                String city1 = citiesInPath[currentIndex[0]];
                String city2 = citiesInPath[currentIndex[0] + 1];

                double[] position1 = nodePositions.get(city1);
                double[] position2 = nodePositions.get(city2);

                if (position1 != null && position2 != null) {
                    // Calculate the delta for smooth line drawing
                    double deltaX = position2[0] - position1[0];
                    double deltaY = position2[1] - position1[1];

                    // Incremental drawing
                    double increment = 0.1;  // Amount to increment the line drawing
                    double endX = currentPosition[0] + deltaX * increment;
                    double endY = currentPosition[1] + deltaY * increment;

                    // Draw the segment incrementally
                    gc.setStroke(Color.GREEN);
                    gc.setLineWidth(3);  // Thicker line for better visibility
                    gc.strokeLine(currentPosition[0], currentPosition[1], endX, endY);

                    // Update the current position to the new end position
                    currentPosition[0] = endX;
                    currentPosition[1] = endY;

                    // If the segment is drawn fully, move to the next one
                    if (Math.abs(endX - position2[0]) < 1 && Math.abs(endY - position2[1]) < 1) {
                        currentIndex[0]++;
                    }
                } else {
                    System.out.println("Positions not found for cities: " + city1 + " or " + city2);
                }
            } else {
                timeline.stop();  // Stop the animation when all segments are drawn
            }

        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    // Mouse click event handler for selecting cities
    private void handleMouseClick(MouseEvent event, GraphicsContext gc) {
        double x = event.getX();
        double y = event.getY();
        String selectedCity = findCityAtPosition(x, y);

        if (selectedCity != null) {
            if (startNode == null) {
                startNode = selectedCity;
                startNodeLabel.setText("Start node: "+startNode +" Node Selected:");
                System.out.println("Start city selected: " + startNode);
            } else if (endNode == null && !selectedCity.equals(startNode)) {
                endNode = selectedCity;

                endNodeLabel.setText("End node: "+endNode+" Node Selected");
                System.out.println("End city selected: " + endNode);
                addEdgeButton.setDisable(false);  // Enable Add Edge button after both cities are selected
                findShortestPathButton.setDisable(false);  // Enable Find Shortest Path button
            } else if(endNode!=null){
                endNode=selectedCity;
                endNodeLabel.setText("End node: "+endNode+" Node Selected");
                System.out.println("End city selected");
            }
        }
    }

    // Helper method to find the city at a given position
    private String findCityAtPosition(double x, double y) {
        for (String city : nodePositions.getAllKeys()) {
            double[] position = nodePositions.get(city);
            double dx = x - position[0];
            double dy = y - position[1];
            if (Math.sqrt(dx * dx + dy * dy) <= 15) {  // Check if the click is within the city circle
                return city;
            }
        }
        return null;
    }
}
