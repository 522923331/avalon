package iplay.cool.codeGeeX;/**
 * @author dove
 * @date 2023/2/21
 */
public class TestCodeGeeX {
    //write bubble sort method to sort array
    public static void bubbleSort(int[] array){
        boolean needNextPass = true;
        int end = array.length - 1;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < end; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    needNextPass = true;
                } else {
                    needNextPass = false;
                }
            }
            if (needNextPass) {
                end--;
            } else {
                needNextPass = true;
                i--;
            }
        }
    }

    public static void main(String[] args) {
        int[] array = {1, 3, 2, 4, 5};
        bubbleSort(array);
        for (int i : array) {
            System.out.println(i);
        }
    }
}


