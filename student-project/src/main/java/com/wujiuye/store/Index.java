package com.wujiuye.store;

public class Index {

    private int msgPhyOffset;
    private int nextIndexOffset;

    public Index(int msgPhyOffset, int nextIndexPhyOffset) {
        this.msgPhyOffset = msgPhyOffset;
        this.nextIndexOffset = nextIndexPhyOffset;
    }

    public int getMsgPhyOffset() {
        return msgPhyOffset;
    }

    public void setMsgPhyOffset(int msgPhyOffset) {
        this.msgPhyOffset = msgPhyOffset;
    }

    public int getNextIndexOffset() {
        return nextIndexOffset;
    }

    public void setNextIndexOffset(int nextIndexOffset) {
        this.nextIndexOffset = nextIndexOffset;
    }

    @Override
    public String toString() {
        return "Index{" +
                "msgPhyOffset=" + msgPhyOffset +
                ", nextIndexOffset=" + nextIndexOffset +
                '}';
    }

    private static byte[] toByteArray(int value) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++, value >>>= i) {
            bytes[i] = (byte) (value & 0xff);
        }
        return bytes;
    }

    public static int bytes2Int(byte[] bytes, int offset, int lenght) {
        if (offset + lenght > bytes.length) {
            return 0;
        }
        int result = bytes[offset] & 0xff;
        result = result << 8 | bytes[offset + 1] & 0xff;
        result = result << 8 | bytes[offset + 2] & 0xff;
        result = result << 8 | bytes[offset + 3] & 0xff;
        return result;
    }

    public static byte[] int2Bytes(int num) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (num >>> 24);
        bytes[1] = (byte) (num >>> 16);
        bytes[2] = (byte) (num >>> 8);
        bytes[3] = (byte) num;
        return bytes;
    }

    private static byte[] merge(byte[] bytes1, byte[] bytes2) {
        byte[] newByes = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, newByes, 0, bytes1.length);
        System.arraycopy(bytes2, 0, newByes, bytes1.length, bytes2.length);
        return newByes;
    }

    public byte[] toByteArray() {
        byte[] one = int2Bytes(this.getMsgPhyOffset());
        byte[] two = int2Bytes(this.getNextIndexOffset());
        return merge(one, two);
    }

    public static Index parseByteArray(byte[] bytes) {
        return new Index(bytes2Int(bytes, 0, 4), bytes2Int(bytes, 4, 4));
    }

}
