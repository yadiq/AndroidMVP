package com.hqumath.androidmvp.utils;

import java.util.Arrays;

/**
 * ****************************************************************
 * 作    者: Created by gyd
 * 创建时间: 2022/3/8 13:28
 * 文件描述:
 * 注意事项: 测试排序算法，从小到大排序
 * <p>
 * <p>
 * ****************************************************************
 */
public class SortUtil {
    public static void main(String[] args) {

        int[] data = {1, 81, 3, 16, 8, 0, 32, 82, 6, 83, 10, 22, 45, 278, 98, 432, 17, 6, 4, 33, 68, 24, 987, 67, 85, 35};
        int[] result = QuickSort(data);
        //QuickSort(data);
        /*int[] result = MergeSort(data);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            sb.append(" " + data[i]);
        }
        System.out.println(sb.toString());*/

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(" " + result[i]);
        }
        System.out.println(sb.toString());
        int index = BinarySearch(result, 98);
        System.out.println("序号：" + index);
    }

    /**
     * 冒泡排序，比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * 时间复杂度 O(n^2)
     */
    public static int[] BubbleSort(int[] sourceArray) {
        //浅克隆,不改变原数组内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        for (int i = 0; i < arr.length - 1; i++) {
            // 设定一个标记，若为true，则表示此次循环没有进行交换，也就是待排序列已经有序，排序已经完成。
            boolean flag = true;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = false;
                }
            }
            if (flag)
                break;
        }
        return arr;
    }

    /**
     * 插入排序，第一个元素看作有序序列，未排序序列一次插入有序序列。
     * 时间复杂度 O(n^2)
     */
    public static int[] InsertSort(int[] sourceArray) {
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];//要插入的数据
            int j = i - 1;
            while (j >= 0 && temp < arr[j]) {//逆序比较，不能插入为止
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = temp;
        }
        return arr;
    }

    /**
     * 选择排序，直观方法，找最小元素。
     * 时间复杂度 O(n^2)
     */
    public static int[] SelectionSort(int[] sourceArray) {
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[minIndex] > arr[j]) {
                    minIndex = j;
                }
            }
            if (i != minIndex) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
        return arr;
    }

    /**
     * 快速排序，找基准数->分割成两部分->分别递归
     * 时间复杂度 O(n^2)
     */
    public static int[] QuickSort(int[] sourceArray) {
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);
        partition(arr, 0, arr.length - 1);
        return arr;
    }

    /**
     * 快速排序，分割并递归
     */
    private static void partition(int[] arr, int left, int right) {
        if (left < right) {
            //找基准数的三个方法：固定位置 随机选取 三数取中
            int pivot = arr[left];
            int i = left;
            int j = right;
            while (i < j) {
                while (i < j && arr[j] >= pivot)
                    j--;
                if (i < j)
                    arr[i++] = arr[j];
                while (i < j && arr[i] < pivot)
                    i++;
                if (i < j)
                    arr[j--] = arr[i];
            }
            arr[i] = pivot;
            partition(arr, left, i - 1);
            partition(arr, i + 1, right);
        }
    }

    /**
     * 归并排序，分割成两部分->合并处理->递归
     * 时间复杂度O(n log n)
     */
    public static int[] MergeSort(int[] arr) {
        if (arr.length < 2)
            return arr;
        int middle = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, middle);//[from, to)
        int[] right = Arrays.copyOfRange(arr, middle, arr.length);
        return merge(MergeSort(left), MergeSort(right));
    }

    /**
     * 归并排序，合并两个子序列
     */
    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0;
        while (left.length > 0 && right.length > 0) {
            if (left[0] <= right[0]) {
                result[i++] = left[0];
                left = Arrays.copyOfRange(left, 1, left.length);
            } else {
                result[i++] = right[0];
                right = Arrays.copyOfRange(right, 1, right.length);
            }
        }
        while (left.length > 0) {
            result[i++] = left[0];
            left = Arrays.copyOfRange(left, 1, left.length);
        }
        while (right.length > 0) {
            result[i++] = right[0];
            right = Arrays.copyOfRange(right, 1, right.length);
        }
        return result;
    }

    /**
     * 二分查找(有序数组)，比较中值->查找子序列->递归
     */
    public static int BinarySearch(int[] arr, int key) {
        return binarySearch(arr, 0, arr.length - 1, key);
    }

    private static int binarySearch(int[] arr, int left, int right, int key) {
        if (left > right)
            return -1;
        int mid = left + (right - left) / 2;//直接平均可能会溢出
        if (key < arr[mid]) {
            return binarySearch(arr, left, mid - 1, key);
        } else if (key > arr[mid]) {
            return binarySearch(arr, mid + 1, right, key);
        } else {
            return mid;
        }
    }
}