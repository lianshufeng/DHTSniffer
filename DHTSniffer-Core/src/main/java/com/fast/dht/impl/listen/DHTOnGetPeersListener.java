package com.fast.dht.impl.listen;

import java.net.InetSocketAddress;

import com.fast.dht.impl.DHTSnifferServerImpl;
import com.fast.dht.net.listener.OnGetPeersListener;
import com.fast.dht.util.AddressUtil;
import com.fast.dht.util.BytesUtil;

/**
 * GetPeer
 * 
 * @作者 练书锋
 * @时间 2018年2月28日
 *
 *
 */
public class DHTOnGetPeersListener extends SuperListener implements OnGetPeersListener {

	public DHTOnGetPeersListener(DHTSnifferServerImpl dhtSnifferServerImpl) {
		super(dhtSnifferServerImpl);
	}

	@Override
	public void onGetPeers(InetSocketAddress address, byte[] info_hash) {
		String hash = BytesUtil.binToHex(info_hash).toLowerCase();
		// 保存到数据库里
		super.infoHashDao.updateHash(hash, AddressUtil.format(address));
	}

}
