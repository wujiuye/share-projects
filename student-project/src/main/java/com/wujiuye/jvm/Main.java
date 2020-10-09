package com.wujiuye.jvm;

public class Main {

    private String toBinay(int value) {
        StringBuffer binay = new StringBuffer("0xb");
        // 直到商为0
        while (value != 0) {
            // 取余数，0｜1
            binay.append(value % 2);
            // 除2继续
            value /= 2;
        }
        return binay.toString();
    }

    public void hello() {
        System.out.println(toBinay(5));
        System.out.println((byte) 1 >>> 2);
        // 0xb1000_0001 -> 0xb0010_0000
        System.out.println(toBinay((byte) -1 >>> 2));
        System.out.println((byte) 1 << 2);
        System.out.println((byte) -1 << 2);
        System.out.println((byte) -1 >> 4);
    }

    public static void main(String[] args) {
        new Main().hello();
    }

}
