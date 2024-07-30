package iplay.cool.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wu.dang
 * @since 2024/4/13
 */
public class Test {

    public static void main(String[] args) {
        for (int i = 10000000; i > 1; i--) {
            itr(i);
        }
    }

    private static void itr(int number){
        List<Integer> mataArr = splitInteger(number);
        int length = mataArr.size();
        addFibo(mataArr,length,number);
        if (mataArr.contains(number)){
            System.out.println("当前数字为："+number);
        }
//        else {
//            System.out.println("不符合");
//        }
    }

    public static void addFibo(List<Integer> mataArr,int length,int number){
        int sumValue = 0;
        while (sumValue < number){
            sumValue = sumFromIndex(mataArr, mataArr.size() - length);
            mataArr.add(sumValue);
        }
    }

    public static List<Integer> splitInteger(int num) {
        List<Integer> digits = new ArrayList<>();

        // 将整数按位数拆分并添加到列表中
        while (num > 0) {
            digits.add(num % 10);
            num /= 10;
        }

        // 将列表反转以使得顺序正确
        Collections.reverse(digits);

        return digits;
    }

    public static int sumFromIndex(List<Integer> array, int startIndex) {
        // 确保起始索引在数组范围内
        if (startIndex < 0 || startIndex >= array.size()) {
            System.out.println("起始索引超出数组范围！");
            return 0;
        }

        int sum = 0;
        // 从起始索引开始累加数组中的元素值
        for (int i = startIndex; i < array.size(); i++) {
            sum += array.get(i);
        }
        return sum;
    }
}
