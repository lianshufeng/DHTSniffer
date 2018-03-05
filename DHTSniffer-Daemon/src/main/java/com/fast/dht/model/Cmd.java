package com.fast.dht.model;

public class Cmd {

	// 命令行
	private String cmd;
	// 环境
	private String[] envp;
	// 运行目录
	private String dir;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String[] getEnvp() {
		return envp;
	}

	public void setEnvp(String[] envp) {
		this.envp = envp;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
