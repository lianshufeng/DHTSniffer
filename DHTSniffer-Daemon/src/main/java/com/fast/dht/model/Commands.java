package com.fast.dht.model;

/**
 * 
 * @作者 练书锋
 * @时间 2018年3月5日
 *
 *
 */
public class Commands {

	// 命令行列表
	private Cmd[] cmds = null;

	// 休息时间
	private long sleep = 10000;

	public Cmd[] getCmds() {
		return cmds;
	}

	public void setCmds(Cmd[] cmds) {
		this.cmds = cmds;
	}

	public long getSleep() {
		return sleep;
	}

	public void setSleep(long sleep) {
		this.sleep = sleep;
	}

}
