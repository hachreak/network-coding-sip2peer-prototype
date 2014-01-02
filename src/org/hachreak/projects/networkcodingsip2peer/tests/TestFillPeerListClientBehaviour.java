package org.hachreak.projects.networkcodingsip2peer.tests;

import it.unipr.ce.dsg.s2p.peer.PeerListManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hachreak.projects.networkcodingsip2peer.actionListener.FullFillPeerListener;
import org.hachreak.projects.networkcodingsip2peer.behaviour.FillPeerListClientBehaviour;
import org.hachreak.projects.networkcodingsip2peer.behaviour.FillPeerListServerBehaviour;
import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestFillPeerListClientBehaviour {

	private String configFileBootstrap = "config/bootstrappeertest.cfg";
	private String configFile = "config/TestFillStorePeerListBehaviour.cfg";

	private InitNetwork initializer;
	private boolean finish;

	@Before
	public void setUp() throws Exception {
		initializer = new InitNetwork(configFileBootstrap, configFile);
	}

	@After
	public void tearDown() throws Exception {
		initializer.stopNetwork();
	}

	@Test
	public void testFillPeerListBehaviour() {
		List<SimplePeer> peers = initializer.getPeers();
		Iterator<SimplePeer> i = peers.iterator();
		List<FillPeerListServerBehaviour> bs = new ArrayList<FillPeerListServerBehaviour>();
		while(i.hasNext()){
			SimplePeer p = i.next();
			bs.add(new FillPeerListServerBehaviour(p));
			System.out.println(p.getPeerList().size());
		}
		
		finish = false;
		SimplePeer peer = peers.get(20);
		FillPeerListClientBehaviour bc = new FillPeerListClientBehaviour(peer, peer.getPeerList(), 30);
		bc.addFullFillPeerListener(new FullFillPeerListener() {
			
			public void action(PeerListManager plm) {
				finish = true;
			}
		});
				
		Thread thread = new Thread(bc);
		thread.run();
		
		while (!finish) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("peer list size: "+peer.getPeerList().size());

	}

}
