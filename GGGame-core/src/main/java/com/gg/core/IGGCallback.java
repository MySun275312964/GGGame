package com.gg.core;

public interface IGGCallback<T> extends ICallback {
	void call(T t);
}
