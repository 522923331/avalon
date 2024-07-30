package iplay.cool.test;
import java.util.ArrayList;

import java.util.List;

public class CyclicFibonacci {

    public static List<Integer> generateFibonacciSequence(int limit) {
        List<Integer> sequence = new ArrayList<>();
        int num1 = 1, num2 = 1;
        while (num1 <= limit) {
            sequence.add(num1);
            int temp = num1 + num2;
            num1 = num2;
            num2 = temp;
        }
        return sequence;
    }

    public static boolean isCyclicFibonacciNumber(int num, List<Integer> sequence) {
        int n = String.valueOf(num).length();
        if (sequence.contains(num)) {
            return true;
        }
        for (int i = n + 1; i < sequence.size(); i++) {
            int sum = 0;
            for (int j = i - n; j < i; j++) {
                sum += sequence.get(j);
            }
            if (sequence.get(i) == sum) {
                return true;
            }
        }
        return false;
    }

    public static int findLargestCyclicFibonacciNumber(int limit) {
        List<Integer> fibonacciSequence = generateFibonacciSequence(limit);
        int largestCyclicFibonacci = 0;
        for (int num = limit; num > 0; num--) {
            if (isCyclicFibonacciNumber(num, fibonacciSequence)) {
                largestCyclicFibonacci = num;
                break;
            }
        }
        return largestCyclicFibonacci;
    }

    public static void main(String[] args) {
        int limit = (int) Math.pow(10, 7);
        limit = 200;
        int largestCyclicFibonacci = findLargestCyclicFibonacciNumber(limit);
        System.out.println("在 0 到 " + limit + " 中，最大的类斐波那契循环数是：" + largestCyclicFibonacci);
    }
}
