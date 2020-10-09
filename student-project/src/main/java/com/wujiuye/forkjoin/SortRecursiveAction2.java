package com.wujiuye.forkjoin;

import java.util.concurrent.RecursiveAction;

/**
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * <p>
 * 微信公众号id：code_skill
 * QQ邮箱：419611821@qq.com
 * 微信号：www_wujiuye_com
 * <p>
 * ======================^^^^^^^==============^^^^^^^============
 *
 * @ 作者       |   吴就业 www.wujiuye.com
 * ======================^^^^^^^==============^^^^^^^============
 * @ 创建日期      |   Created in 2018年12月18日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   lock
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${version}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class SortRecursiveAction2 extends RecursiveAction {

    private int[] array;
    private int start;
    private int end;

    public SortRecursiveAction2(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        sort(this.array,start,end);
    }

    private void sort(int[] array,int start,int end){
        if (start >= end) return;
        if (start + 1 == end) {
            if (array[start] > array[end]) {
                int tmp = array[start];
                array[start] = array[end];
                array[end] = tmp;
            }
            return;
        }
        int mid = (end - start) / 2;
        SortRecursiveAction2 left = new SortRecursiveAction2(array, start, start + mid);
        // 执行左子任务
        left.fork();
        //在当前线程执行右子任务
        sort(array, start + mid + 1, end);
        //等待左子任务执行完成
        left.join();//并不是简单的阻塞线程
        //合并子任务
        hebin(array, start, start + mid, end);
        System.out.println("子任务["+start+","+end+"]由线程["+Thread.currentThread().getName()+"]执行完成！");
    }

    private static void copy(int[] array, int[] src, int start, int end) {
        for (int i = start, j = 0; i <= end; i++) {
            array[i] = src[j++];
        }
    }

    private static void hebin(int[] array, int satrt, int mid, int end) {
        int[] newArray = new int[end - satrt + 1];
        int left = satrt;
        int right = mid + 1;
        int index = 0;
        while (left <= mid && right <= end) {
            if (array[left] > array[right]) {
                newArray[index++] = array[right++];
            } else {
                newArray[index++] = array[left++];
            }
        }
        while (left <= mid) {
            newArray[index++] = array[left++];
        }
        while (right <= end) {
            newArray[index++] = array[right++];
        }
        copy(array, newArray, satrt, end);
    }

}
