package com.gg.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;

/**
 * @author guofeng.qin
 */
public class Test {
    public static void main(String[] args) throws IOException {
        List<Entry> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new Entry("i-" + i, i));
        }

        String str = new Gson().toJson(list);
        System.out.println(str);

        long start1 = System.currentTimeMillis();
        Kryo k = new Kryo();
        // Output ot = new Output(256000);
        int maxsize = 0;
        for (int i = 0; i < 50000; i++) {
            ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
            oos.writeObject(list);
            // ot.clear();
            Output ot = new Output(oos);
            k.writeClassAndObject(ot, list);
            byte[] bf = ot.toBytes();
            if (maxsize < bf.length) {
                maxsize = bf.length;
            }
            // Kryo k2 = new Kryo();
            Input it = new Input(bf);
            List<Entry> l2 = ((List<Entry>) k.readClassAndObject(it));
        }
        long end1 = System.currentTimeMillis();
        System.out.println("" + (end1 - start1) + ":" + maxsize);

        maxsize = 0;
        long start2 = System.currentTimeMillis();
        Gson gson = new Gson();
        for (int i = 0; i < 50000; i++) {
            String strr = gson.toJson(list);
            byte[] sss = strr.getBytes();
            if (maxsize < sss.length) {
                maxsize = sss.length;
            }
            // Gson gson2 = new Gson();
            List<Entry> ll = gson.fromJson(strr, List.class);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("" + (end2 - start2) + ":" + maxsize);
    }

    static class Entry implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        public String str;
        public int i;

        public Entry() {

        }

        public Entry(String str, int i) {
            this.str = str;
            this.i = i;
        }

        @Override
        public String toString() {
            return "[" + str + ":" + i + "]";
        }
    }
}
