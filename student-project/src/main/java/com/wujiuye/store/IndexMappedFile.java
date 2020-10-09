package com.wujiuye.store;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

public class IndexMappedFile extends MappedFile {

    private volatile boolean isWarm = false;
    private MappedByteBuffer mappedByteBuffer;

    public IndexMappedFile(String fileName, int fileSize) throws IOException {
        super(fileName, fileSize);
        this.mappedByteBuffer = getMappedByteBuffer();
    }

    public void initIndex() {
        warmMappedFile(FlushDiskType.SYNC_FLUSH, fileSize % MappedFile.OS_PAGE_SIZE == 0
                ? fileSize / MappedFile.OS_PAGE_SIZE : (fileSize / MappedFile.OS_PAGE_SIZE) + 1);
        isWarm = true;
        writeBuffer = ByteBuffer.allocateDirect(fileSize);
        this.wrotePosition.set((1024 - 1) * 8);
    }

    public boolean appendIndex(int hashCode, Index index) {
        if (hashCode >= 1024) {
            int currentPos = this.wrotePosition.get();
            if (currentPos < this.fileSize) {
                ByteBuffer byteBuffer = writeBuffer != null ? writeBuffer.slice() : getMappedByteBuffer().slice();
                byteBuffer.position(currentPos);
                byteBuffer.put(index.toByteArray());
                this.wrotePosition.addAndGet(8);
                return true;
            }
        } else {
            ByteBuffer buffer = mappedByteBuffer.slice();
            buffer.position((hashCode - 1) * 8);
            buffer.put(index.toByteArray());
            mappedByteBuffer.force();
            return true;
        }
        return false;
    }

    @Override
    public boolean destroy() {
        if (isWarm) {
            munlock();
            isWarm = false;
        }
        return super.destroy();
    }

}
