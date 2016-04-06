package com.gg.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author guofeng.qin
 */
public class KryoHelper {
	private static ThreadLocal<KryoEntry> localKryo = new ThreadLocal<>();
	private static final int DefaultBufferSize = 256;

	public static byte[] writeClassAndObject(Object obj) {
		KryoEntry kryoEntry = localKryo.get();
		if (kryoEntry == null) {
			kryoEntry = new KryoEntry(new Kryo(), new Output(DefaultBufferSize));
			localKryo.set(kryoEntry);
		}
		Kryo kryo = kryoEntry.kryo;
		Output out = kryoEntry.out;
		out.clear();

		int retryCount = 0;
		while (retryCount <= 1) {
			retryCount++;
			try {
				kryo.writeClassAndObject(out, obj);
				return out.getBuffer();
			} catch (KryoException e) {
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ObjectOutputStream bos = new ObjectOutputStream(baos)) {
					bos.writeObject(obj);
					bos.flush();
					out = new Output(baos.size());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		return null;
	}

	public static <T> T readClassAndObject(byte[] bytes) {
		KryoEntry kryoEntry = localKryo.get();
		if (kryoEntry == null) {
			kryoEntry = new KryoEntry(new Kryo(), new Output(DefaultBufferSize));
			localKryo.set(kryoEntry);
		}
		Kryo kryo = kryoEntry.kryo;
		Input input = new Input(bytes);
		return (T) kryo.readClassAndObject(input);
	}

	static final class KryoEntry {
		public Kryo kryo;
		public Output out;

		public KryoEntry(Kryo kryo, Output out) {
			this.kryo = kryo;
			this.out = out;
		}
	}
}
