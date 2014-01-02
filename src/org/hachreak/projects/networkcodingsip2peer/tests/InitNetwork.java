package org.hachreak.projects.networkcodingsip2peer.tests;

import static org.junit.Assert.assertTrue;
import it.unipr.ce.dsg.s2p.peer.PeerListManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hachreak.projects.networkcodingsip2peer.actionListener.FullFillPeerListener;
import org.hachreak.projects.networkcodingsip2peer.exceptions.NoBootstrapConfiguredException;
import org.hachreak.projects.networkcodingsip2peer.peer.BootstrapPeer;
import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;
import org.junit.Test;

public class InitNetwork {

	private String configFile = "config/simplepeertest.cfg";

	private String configFileBootstrap = "config/bootstrappeertest.cfg";

	private BootstrapPeer bootstrapPeer;

	private int numberOfClients = 100;

	private int port = 5081;

	private int totalLoaded;

	private List<SimplePeer> peers;

	public InitNetwork(String confFileBootstrap, String configFilePeer) throws IOException, NoBootstrapConfiguredException {
		this.configFileBootstrap = confFileBootstrap;
		this.configFile = configFilePeer;
		initNetwork();
	}

	private void initNetwork() throws IOException,
			NoBootstrapConfiguredException {
		bootstrapPeer = new BootstrapPeer(configFileBootstrap);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// load N peers
		totalLoaded = 0;
		peers = new ArrayList<SimplePeer>();
		for (int i = 0; i < numberOfClients; i++) {
			SimplePeer peer = new SimplePeer(configFile, "client_key_" + i,
					"client_name_" + i, port + i) {
//				protected void onReceivedJSONMsg(JSONObject jsonMsg,
//						Address sender) {
//					super.onReceivedJSONMsg(jsonMsg, sender);
////					totalLoaded++;
//				}
			};
			peer.joinBootstrapPeer(new FullFillPeerListener() {
				
				public void action(PeerListManager plm) {
					totalLoaded++;
					System.out.println("[InitNetwork] Peer "+totalLoaded + ": Peer List filled!");		
				}
			});
			peers.add(peer);
//			System.out.println("InitNetwork] add peer "+i);
		}

		// wait initialization of all peers
		while (totalLoaded < numberOfClients) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public BootstrapPeer getBootstrapPeer() {
		return bootstrapPeer;
	}

	public List<SimplePeer> getPeers() {
		return peers;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void stopNetwork() {
		Iterator<SimplePeer> i = peers.iterator();
		while(i.hasNext()){
			i.next().halt();
//			System.out.println("stop network..");
		}
		peers = null;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
