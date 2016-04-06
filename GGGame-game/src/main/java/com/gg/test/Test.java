package com.gg.test;

import java.io.IOException;
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
		for (int i = 0; i < 10; i++) {
			list.add(new Entry("i-" + i, i));
		}

		String str = new Gson().toJson(list);
		System.out.println(str);

		long start1 = System.currentTimeMillis();
		Kryo k = new Kryo();
		Output ot = new Output(8);
		for (int i = 0; i < 10000; i++) {
//			ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
//			oos.writeObject(list);
			ot.clear();
			k.writeClassAndObject(ot, list);
			byte[] bf = ot.getBuffer();
//			Kryo k2 = new Kryo();
			Input it = new Input(bf);
			List<Entry> l2 = ((List<Entry>) k.readClassAndObject(it));
		}
		long end1 = System.currentTimeMillis();
		System.out.println("" + (end1 - start1));
		
		long start2 = System.currentTimeMillis();
		Gson gson = new Gson();
		for (int i = 0; i < 10000; i++) {
			String strr = gson.toJson(list);
//			Gson gson2 = new Gson();
			List<Entry> ll = gson.fromJson(strr, List.class);
		}
		long end2 = System.currentTimeMillis();
		System.out.println("" + (end2 - start2));
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
