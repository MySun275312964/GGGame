package com.gg.common;

/**
 * @author guofeng.qin
 */
public class Constants {
	public static final String Localhost = "127.0.0.1";
	public static final int MasterPort = 19999; // master服务的端口号
	public static final int GatePort = 19998; // gate 服务的端口号

	public static final String Gate = "gate";
	public static final String Master = "master";

	public static final class ZK {
		public static final int SessionTimeout = 30;
		public static final String ConnectionUrl = "127.0.0.1:2181";
		public static final String Root = "/GGGame";
		public static final String Serivce = Root + "/services";
		
	}
}
