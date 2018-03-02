package com.fast.dht.impl.listen;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fast.dht.impl.DHTSnifferServerImpl;
import com.fast.dht.model.FileModel;
import com.fast.dht.net.handler.AnnouncePeerInfoHashWireHandler;
import com.fast.dht.net.listener.OnAnnouncePeerListener;
import com.fast.dht.net.listener.OnMetadataListener;
import com.fast.dht.net.model.Info;
import com.fast.dht.net.model.SubFile;
import com.fast.dht.net.model.Torrent;
import com.fast.dht.util.AddressUtil;
import com.fast.dht.util.BytesUtil;

/**
 * AnnouncePeer
 * 
 * @作者 练书锋
 * @时间 2018年2月28日
 *
 *
 */
public class DHTOnAnnouncePeerListener extends SuperListener implements OnAnnouncePeerListener {

	private static final Logger LOG = Logger.getLogger(DHTOnAnnouncePeerListener.class);

	public DHTOnAnnouncePeerListener(DHTSnifferServerImpl dhtSnifferServerImpl) {
		super(dhtSnifferServerImpl);
	}

	@Override
	public void onAnnouncePeer(InetSocketAddress address, byte[] info_hash, int port) {
		String ip = address.getAddress().getHostAddress();
		String hash = BytesUtil.binToHex(info_hash).toLowerCase();
		LOG.info(String.format("[AnnouncePeer] - %s:%s - %s", ip, port, hash));
		super.infoHashDao.updateHash(hash, AddressUtil.format(address));
		if (!torrentDao.exists(hash, com.fast.dht.db.domain.Torrent.class)) {
			AnnouncePeerInfoHashWireHandler handler = new AnnouncePeerInfoHashWireHandler();
			initHandler(handler);
			try {
				handler.handler(new InetSocketAddress(ip, port), info_hash);
			} catch (Exception e) {
				LOG.error(e);
			}
		}
	}

	private void initHandler(AnnouncePeerInfoHashWireHandler handler) {
		// private void initHandler() {
		handler.setOnMetadataListener(new OnMetadataListener() {
			@Override
			public void onMetadata(Torrent torrent) {
				if (torrent == null || torrent.getInfo() == null)
					return;
				// 入库操作
				String hash = torrent.getInfo_hash().trim().toLowerCase();

				com.fast.dht.db.domain.Torrent torrentEnity = new com.fast.dht.db.domain.Torrent();
				torrentEnity.setHash(hash);
				if (torrent.getCreationDate() != null) {
					torrentEnity.setCreationTime(torrent.getCreationDate().getTime());
				}
				Info info = torrent.getInfo();
				if (info != null) {
					torrentEnity.setName(info.getName());
					long size = 0;
					List<FileModel> files = new ArrayList<>();
					if (info.getFiles() != null) {
						for (SubFile torrentFile : info.getFiles()) {
							long fileSize = Long.parseLong(torrentFile.getLength());;
							files.add(new FileModel(torrentFile.getPath(), fileSize));
							size+=fileSize;
						}
					}
					torrentEnity.setSize(size);
					torrentEnity.setFiles(files.toArray(new FileModel[files.size()]));
				}
				torrentDao.update(torrentEnity);
			}
		});
	}

}
