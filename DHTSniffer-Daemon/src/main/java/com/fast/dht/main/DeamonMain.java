package com.fast.dht.main;

import java.io.File;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.StreamUtils;

import com.fast.dht.model.Cmd;
import com.fast.dht.model.Commands;
import com.fast.dht.util.JsonUtil;

public class DeamonMain {

	private static final Logger LOG = Logger.getLogger(DeamonMain.class);

	public static void main(String[] args) throws Exception {

		final Commands commands = readConfig();
		final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(commands.getCmds().length);
		for (final Cmd cmd : commands.getCmds()) {
			fixedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					try {
						LOG.info("Run cmd : " + JsonUtil.toJson(cmd));
						File dir = cmd.getDir() == null ? null : new File(cmd.getDir());
						final Process process = Runtime.getRuntime().exec(cmd.getCmd(), cmd.getEnvp(), dir);
						// 增加重启守护时间
						if (cmd.getReStartTime() != null) {
							new Timer().schedule(new TimerTask() {

								@Override
								public void run() {
									LOG.info("kill "+process);
									process.destroyForcibly();
								}
							}, cmd.getReStartTime());
						}
						process.waitFor();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						LOG.info("Sleep : " + commands.getSleep());
						Thread.sleep(commands.getSleep());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					fixedThreadPool.execute(this);
				}
			});
		}

	}

	private static Commands readConfig() throws Exception {
		InputStream inputStream = DeamonMain.class.getResourceAsStream("/Commands.json");
		byte[] bin = StreamUtils.getBytes(inputStream);
		inputStream.close();
		return JsonUtil.toObject(new String(bin, "UTF-8"), Commands.class);
	}

}
