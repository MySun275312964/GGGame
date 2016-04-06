package com.gg.core.harbor.center;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.AsyncCallback.Children2Callback;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.gg.common.Constants;
import com.gg.common.StringUtil;

/**
 * @author guofeng.qin
 */
public class ZKMonitor {
	private static ZooKeeper zk = null;

	private ZKMonitor() {
		try {
			zk = new ZooKeeper(Constants.ZK.ConnectionUrl, Constants.ZK.SessionTimeout, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			zk = null;
		}
	}

	private static class Holder {
		private static ZKMonitor _ZKMonitor = new ZKMonitor();
	}

	public static ZKMonitor getInstance() {
		return Holder._ZKMonitor;
	}

	private void createRoot() {
		int retry = 0;
		while (retry <= 50) {
			retry++;
			try {
				zk.create(Constants.ZK.Root, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} catch (KeeperException e) {
				if (e.code() != Code.NODEEXISTS) {
					continue;
				}
			} catch (InterruptedException e) {
				continue;
			}
			try {
				zk.create(Constants.ZK.Serivce, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} catch (KeeperException e) {
				if (e.code() != Code.NODEEXISTS) {
					continue;
				}
			} catch (InterruptedException e) {
				continue;
			}
			return;
		}
		throw new RuntimeException("can not create root node.");
	}

	private Children2Callback monitorServiceCallback = new Children2Callback() {
		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {

		}
	};

	private void monitorService() {
		zk.getChildren(Constants.ZK.Serivce, true, monitorServiceCallback, null);
	}

	private void createServiceNode(String path) throws KeeperException, InterruptedException {
		if (zk.exists(path, false) == null) {
			int retry = 0;
			while (retry <= 50) {
				retry++;
				try {
					zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				} catch (KeeperException e) {
					if (e.code() != Code.NODEEXISTS) {
						continue;
					}
				} catch (InterruptedException e) {
					continue;
				}
				return;
			}
			throw new RuntimeException("can not create root node.");
		}
	}

	private String createSelfNode(String path, String host, int port) {
		String nodePath = StringUtil.join("/", path, "node");
		try {
			return zk.create(nodePath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		} catch (KeeperException | InterruptedException e) {
			throw new RuntimeException("self register error.");
		}
	}

	private void getServices(String servicePath) {
		zk.getChildren(servicePath, false, new ChildrenCallback() {
			@Override
			public void processResult(int rc, String path, Object ctx, List<String> children) {
				if (children != null) {
					for (String child : children) {
						String childPath = StringUtil.join("/", path, child);
						// TODO ... 处理具体的service注册
					}
				}
			}
		}, null);
	}

	private void registSelf(String service, String host, int port) throws KeeperException, InterruptedException {
		String servicePath = StringUtil.join("/", Constants.ZK.Serivce, service);
		createServiceNode(servicePath);
		createSelfNode(servicePath, host, port);
		zk.getChildren(Constants.ZK.Serivce, false, new ChildrenCallback() {
			@Override
			public void processResult(int rc, String path, Object ctx, List<String> children) {
				if (children != null) {
					for (String child : children) {
						String childPath = StringUtil.join("/", path, child);
						getServices(childPath);
					}
				}
			}
		}, null);
	}

	public void startMonitor(String service, String host, int port)
			throws IOException, KeeperException, InterruptedException {
		if (zk == null) {
			throw new RuntimeException("zk init error.");
		}
		if (zk.exists(Constants.ZK.Serivce, false) == null) {
			createRoot();
		}
		monitorService();
	}
}
