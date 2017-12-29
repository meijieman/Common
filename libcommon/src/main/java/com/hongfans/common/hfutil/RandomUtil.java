package com.hongfans.common.hfutil;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 判断当前可播放资源
 * 随机播放获取下一个可播放资源
 * Created by MEI on 2017/10/24.
 */

public class RandomUtil {

    private List<Integer> mList = new CopyOnWriteArrayList<>();

    private Random mRandom = new Random();

    public void start(int total) {
        if (total < 0) {
            throw new IllegalArgumentException("total cannot less than 0");
        }
        mList.clear();
        for (int i = 0; i < total; i++) {
            mList.add(i);
        }
    }

    public void remove(int index) {
        int i = -1;
        for (int i1 = 0; i1 < mList.size(); i1++) {
            Integer integer = mList.get(i1);
            if (index == integer) {
                i = i1;
                break;
            }
        }
        if (i != -1) {
            print("移除成功       源索引 " + index);
            mList.remove(i);
        } else {
            print("移除失败 (源索引) " + index);
        }
    }

    public boolean hasNext() {
        return mList.size() != 0;
    }

    /**
     * 调用此方法前需要先调用 hasNext 判断是否有下一个
     * @return
     */
    public int next() {
        int index = getIndex();
        Integer integer = mList.get(index);
        Integer remove = mList.remove(index);
        print("下一个 索引 " + index + ", 源索引 " + remove);
        return integer;
    }

    /**
     * 调用此方法前需要先调用 contains 判断是否存在
     * @param index
     */
    public void next(int index) {
        boolean remove = mList.remove(new Integer(index));
        if (remove) {
            print("下一个成功 源索引 " + index);
        } else {
            print("下一个失败 (源索引) " + index);

        }
    }

    public boolean contains(int index) {
        for (Integer integer : mList) {
            if (integer == index) {
                return true;
            }
        }
        return false;
    }

    // 添加多个到最后面
    public void add(int startIndex, int count) {
        if (count < 0) {
            return;
        }
        print("添加 起始值 " + startIndex + ", " + count + "个");
        for (int i = 0; i < count; i++) {
            mList.add(startIndex);
        }
    }

    public int getIndex() {
        return mRandom.nextInt(mList.size());
    }

    public static void main(String[] args) {
        RandomUtil rpu = new RandomUtil();
        rpu.start(2);
        int i = 0;
        while (rpu.hasNext()) {
            if (rpu.contains(1)) {
                rpu.next(1);
            } else {
                print("待移除的出错了 ");
                break;
            }
            i++;
        }
        print("没有数据了 i " + i);
    }

    public static void print(String msg) {
        System.out.println("ele_a ---------- " + msg);
    }

}
