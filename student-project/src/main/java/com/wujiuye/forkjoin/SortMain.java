package com.wujiuye.forkjoin;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

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
public class SortMain {

//    private void copy(int[] array,int[] src,int start,int end){
//        for(int i=start,j=0;i<=end;i++){
//            array[i] = src[j++];
//        }
//    }
//
//    private void hebin(int[] array,int satrt,int mid,int end){
//        int[] newArray = new int[end-satrt+1];
//        int left = satrt;
//        int right = mid+1;
//        int index = 0;
//        while (left<=mid&&right<=end){
//            if(array[left]>array[right]){
//                newArray[index++]=array[right++];
//            }else{
//                newArray[index++]=array[left++];
//            }
//        }
//        while (left<=mid){
//            newArray[index++]=array[left++];
//        }
//        while (right<=end){
//            newArray[index++]=array[right++];
//        }
//        copy(array,newArray,satrt,end);
//    }
//
//    private void sort(int[] array,int satrt,int end){
//        if(satrt>=end)return;
//        if(satrt+1==end){
//            if(array[satrt]>array[end]){
//                int tmp = array[satrt];
//                array[satrt] = array[end];
//                array[end] = tmp;
//            }
//            return;
//        }
//        int mid = (end-satrt)/2;
//        sort(array,satrt,satrt+mid);
//        sort(array,satrt+mid+1,end);
//        hebin(array,satrt,satrt+mid,end);
//    }
//
//    public void startSort(int[] array){
//        sort(array,0,array.length-1);
//    }

    public static void main(String[] args) {
        int[] array = new int[]{2,5,3,8,1,0,4,7,3,6,3,8,10};
//        new SortMain()
//                .startSort(array);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
//        SortRecursiveAction action = new SortRecursiveAction(array,0,array.length-1);
        SortRecursiveAction2 action = new SortRecursiveAction2(array,0,array.length-1);
        ForkJoinTask<Void> task = forkJoinPool.submit(action);
        try {
            task.get();
            System.out.println("排序后的结果：");
            for (int i:array){
                System.out.print(i+"  ");
            }
            System.out.println();
        } catch (InterruptedException e) {
            System.err.println("被中断异常");
        } catch (ExecutionException e) {
            System.out.println("执行异常：");
            e.printStackTrace();
        }
    }


}
