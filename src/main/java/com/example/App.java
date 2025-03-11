package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class App 
{
    private static final int MEASUREMENT_ROUNDS = 10;
    private static final double TEST_SAMPLE_RATIO = 0.1;

    public static void main( String[] args )
    {
        String inputFile = "test_data.csv";
        String outputFile = "performance_results.csv";

        List<TestData> testData = readTestData(inputFile);
        List<PerformanceResult> results = new ArrayList<>();

        for (TestData data : testData) {
            // Prepare test sample
            List<Integer> testSample = getTestSample(data.data);

            // Test AVL Tree
            PerformanceResult avlResult = testTreePerformance(new AVLTree<>(), data, testSample);
            results.add(avlResult);

            // Test Red-Black Tree
            PerformanceResult rbResult = testTreePerformance(new RedBlackTree<>(), data, testSample);
            results.add(rbResult);
        }

        writeResults(results, outputFile);
        System.out.println("Performance testing completed. Results saved to " + outputFile);
    }

    private static List<Integer> getTestSample(List<Integer> data) {
        List<Integer> sample = new ArrayList<>(data);
        Collections.shuffle(sample, new Random(42)); // Fixed seed for reproducibility
        int sampleSize = (int) (data.size() * TEST_SAMPLE_RATIO);
        return sample.subList(0, sampleSize);
    }

    private static PerformanceResult testTreePerformance(BalancedBinaryTree<Integer> tree, TestData data, List<Integer> testSample) {
        PerformanceResult result = new PerformanceResult();
        result.treeType = tree.getClass().getSimpleName();
        result.dataType = data.dataType;
        result.size = data.size;

        // Warmup
        for (int i = 0; i < 10; i++) {
            testOperations(tree, data.data);
        }

        // Measure insertion time
        long insertTime = 0;
        for (int i = 0; i < MEASUREMENT_ROUNDS; i++) {
            BalancedBinaryTree<Integer> tempTree = tree.getClass().equals(AVLTree.class) ? new AVLTree<>() : new RedBlackTree<>();
            insertTime += measureOperationTime(() -> {
                for (int value : data.data) {
                    tempTree.insert(value);
                }
            });
            result.initialHeight = tempTree.height();
        }
        result.insertTime = insertTime / MEASUREMENT_ROUNDS;

        // Measure search time using larger test sample
        List<Integer> largerTestSample = getTestSample(data.data, 0.3);
        long searchTime = 0;
        for (int i = 0; i < MEASUREMENT_ROUNDS; i++) {
            searchTime += measureOperationTime(() -> {
                for (int value : largerTestSample) {
                    tree.contains(value);
                }
            });
        }
        result.searchTime = searchTime / MEASUREMENT_ROUNDS;

        // Measure deletion time using larger test sample
        long deleteTime = 0;
        for (int i = 0; i < MEASUREMENT_ROUNDS; i++) {
            BalancedBinaryTree<Integer> tempTree = tree.getClass().equals(AVLTree.class) ? new AVLTree<>() : new RedBlackTree<>();
            for (int value : data.data) {
                tempTree.insert(value);
            }
            deleteTime += measureOperationTime(() -> {
                for (int value : largerTestSample) {
                    tempTree.delete(value);
                }
            });
            result.finalHeight = tempTree.height();
        }
        result.deleteTime = deleteTime / MEASUREMENT_ROUNDS;

        return result;
    }

    private static List<Integer> getTestSample(List<Integer> data, double ratio) {
        List<Integer> sample = new ArrayList<>(data);
        Collections.shuffle(sample, new Random(42)); // Fixed seed for reproducibility
        int sampleSize = (int) (data.size() * ratio);
        return sample.subList(0, sampleSize);
    }

    private static long measureOperationTime(Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        return System.nanoTime() - startTime;
    }

    private static void testOperations(BalancedBinaryTree<Integer> tree, List<Integer> data) {
        for (int value : data) {
            tree.insert(value);
        }
        for (int value : data) {
            tree.contains(value);
        }
        for (int value : data) {
            tree.delete(value);
        }
    }

    private static List<TestData> readTestData(String filename) {
        List<TestData> testData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String dataType = parts[0];
                int size = Integer.parseInt(parts[1]);
                String[] dataValues = parts[2].split(" ");
                List<Integer> data = new ArrayList<>();
                for (String value : dataValues) {
                    data.add(Integer.parseInt(value));
                }
                testData.add(new TestData(dataType, size, data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testData;
    }

    private static void writeResults(List<PerformanceResult> results, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("TreeType,DataType,Size,InsertTime(ns),SearchTime(ns),DeleteTime(ns)\n");
            for (PerformanceResult result : results) {
                writer.write(String.format("%s,%s,%d,%d,%d,%d\n",
                    result.treeType,
                    result.dataType,
                    result.size,
                    result.insertTime,
                    result.searchTime,
                    result.deleteTime));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class TestData {
        String dataType;
        int size;
        List<Integer> data;

        TestData(String dataType, int size, List<Integer> data) {
            this.dataType = dataType;
            this.size = size;
            this.data = data;
        }
    }

    private static class PerformanceResult {
        String treeType;
        String dataType;
        int size;
        long insertTime;
        long searchTime;
        long deleteTime;
        int initialHeight;
        int finalHeight;
    }
}
