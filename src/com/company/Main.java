package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Main {

    class SortClass {

        public void bubbleSort(int[] arr) {
            int n = arr.length;
            for (int i = 0; i < n - 1; i++)
                for (int j = 0; j < n - i - 1; j++)
                    if (arr[j] > arr[j + 1]) {
                        // swap arr[j+1] and arr[j]
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                    }
        }

        public void cocktailSort(int[] arr) {
            boolean swapped = true;
            int start = 0;
            int end = arr.length;

            while (swapped) {
                swapped = false;
                for (int i = start; i < end - 1; ++i) {
                    if (arr[i] > arr[i + 1]) {
                        int temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                        swapped = true;
                    }
                }
                if (!swapped)
                    break;
                swapped = false;
                end = end - 1;
                for (int i = end - 1; i >= start; i--) {
                    if (arr[i] > arr[i + 1]) {
                        int temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                        swapped = true;
                    }
                }
                start = start + 1;
            }
        }

        public void insertionSort(int[] arr) {
            int n = arr.length;
            for (int i = 1; i < n; ++i) {
                int key = arr[i];
                int j = i - 1;
                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j = j - 1;
                }
                arr[j + 1] = key;
            }
        }
    }

    class MyThread extends Thread {
        private int[] arr;
        private String typeSort;

        public MyThread(String typeSort, int[] arr) {
            this.arr = arr;
            this.typeSort = typeSort;
        }
        @Override
        public void run() {
            SortClass sortClass = new SortClass();
            if (this.typeSort.equals("bubbleSort")) {
                sortClass.bubbleSort(this.arr);
            }
            if (this.typeSort.equals("cocktailSort")) {
                sortClass.cocktailSort(this.arr);
            }
            if (this.typeSort.equals("insertionSort")) {
                sortClass.insertionSort(this.arr);
            }
        }
    }

    public void sortAndCalculate(String typeSort, int[] arr) {
        System.out.println(typeSort + " " + arr.length);
        long start = System.currentTimeMillis();
        SortClass sortClass = new SortClass();
        if (typeSort.equals("bubbleSort")) {
            sortClass.bubbleSort(arr);
        }
        if (typeSort.equals("cocktailSort")) {
            sortClass.cocktailSort(arr);
        }
        if (typeSort.equals("insertionSort")) {
            sortClass.insertionSort(arr);
        }
        float time = (System.currentTimeMillis() - start) / 1000F;
        System.out.println("" + time);
        System.out.println(Arrays.toString(Arrays.copyOfRange(arr, arr.length - 30, arr.length)));
    }

    public void startThreads(String typeSort, int countThreads, int[] arr) {
        System.out.println(typeSort + "  " + countThreads + " " + arr.length);
        long start = System.currentTimeMillis();
        Thread[] threads = new Thread[countThreads];
        for (int i = 0; i < countThreads; i++) {
            threads[i] = new MyThread(typeSort, arr);
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        float time = (System.currentTimeMillis() - start) / 1000F;
        System.out.println("" + time);
        System.out.println(Arrays.toString(Arrays.copyOfRange(arr, arr.length - 30, arr.length)));
    }

    public int[] createArray(PrintWriter pw) {
        pw.println("Random array:");
        Random rd = new Random(); // creating Random object
        int[] arr = new int[rd.nextInt(1000, 10000)];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rd.nextInt(1, 100);
            pw.print("["+i+"]"+arr[i]);
            pw.println();
        }
        return arr;
    }


    public void startWork(String typeSort, PrintWriter pw) {
        int[] arrInit = this.createArray(pw);
        int[] arrOne = Arrays.copyOf(arrInit, arrInit.length);
        int[] arrTwo = Arrays.copyOf(arrInit, arrInit.length);
        int[] arrFour = Arrays.copyOf(arrInit, arrInit.length);
        int[] arrEight = Arrays.copyOf(arrInit, arrInit.length);
        System.out.println(Arrays.toString(Arrays.copyOfRange(arrOne, arrOne.length - 30, arrOne.length)));
        this.sortAndCalculate(typeSort, arrOne);
        System.out.println("\n" + Arrays.toString(Arrays.copyOfRange(arrTwo, arrTwo.length - 30, arrTwo.length)));
        this.startThreads(typeSort, 2, arrTwo);
        System.out.println("\n" + Arrays.toString(Arrays.copyOfRange(arrFour, arrFour.length - 30, arrFour.length)));
        this.startThreads(typeSort, 4, arrFour);
        System.out.println("\n" + Arrays.toString(Arrays.copyOfRange(arrEight, arrEight.length - 30, arrEight.length)));
        this.startThreads(typeSort, 8, arrEight);

    }


    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("ja2");
        PrintWriter pw =new PrintWriter(file);



        Main workClass = new Main();
        String[] typesSorts = {"bubbleSort", "cocktailSort", "insertionSort"};
        for (String typeSort : typesSorts) {
            for (int i = 0; i < 2; i++) {
                workClass.startWork(typeSort, pw);
                pw.close();
            }
        }
    }
}
