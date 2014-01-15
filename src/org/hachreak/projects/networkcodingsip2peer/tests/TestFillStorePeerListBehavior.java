/**
 * Copyright (C) 2013 Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 *
 * This source code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.hachreak.projects.networkcodingsip2peer.tests;

import static org.junit.Assert.assertTrue;
import it.unipr.ce.dsg.s2p.peer.PeerListManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.hachreak.projects.networkcodingsip2peer.actionListener.FullFillPeerListener;
import org.hachreak.projects.networkcodingsip2peer.behavior.Behavior;
import org.hachreak.projects.networkcodingsip2peer.behavior.FillPeerListServerBehavior;
import org.hachreak.projects.networkcodingsip2peer.behavior.FillStorePeerListClientBehavior;
import org.hachreak.projects.networkcodingsip2peer.engine.CodingEngine;
import org.hachreak.projects.networkcodingsip2peer.engine.NetworkCodingEngine;
import org.hachreak.projects.networkcodingsip2peer.peer.BootstrapPeer;
import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;
import org.hachreak.projects.networkcodingsip2peer.resource.MediaResource;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestFillStorePeerListBehavior {

//	private int numberOfClients = 100;
//	private List<SimplePeer> peers = new ArrayList<SimplePeer>();
	private BootstrapPeer bootstrapPeer;
	private String configFileBootstrap = "config/bootstrappeertest.cfg";
	private String configFile = "config/TestFillStorePeerListBehaviour.cfg";
//	private int port = 5081;
//	private int totalLoaded = 0;
	private File file = new File("tests/TestFillStorePeerListBehaviour.txt");
	private float redundancyRate = 5 / 2;
	private GaloisField galoisField = new GaloisField((byte) 16);
	private int fragmentSize = 500;
	private Thread thread;
	private boolean stoptest;
	
	private InitNetwork initializer;
	private PeerListManager toFill;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		initializer = new InitNetwork(configFileBootstrap, configFile);
//		initNetwork();
		initCodingEngine();
	}
	
	@After
	public void stopNetwork(){
		initializer.stopNetwork();
	}	

	private void initCodingEngine() throws IOException {
		CodingEngine ce = new NetworkCodingEngine(redundancyRate);
		List<SimplePeer> peers = initializer.getPeers();
		int numberOfClients = peers.size();

		int outputNumOfFragments = ce
				.getOutputNumOfFragments(new MediaResource(file, fragmentSize,
						galoisField));

		System.out.println("[TestFillStorePeerList] peer list size required = "+outputNumOfFragments);
		
		List<Behavior> l = new ArrayList<Behavior>(
				numberOfClients);
		
		toFill = null;
		FillStorePeerListClientBehavior fsplcb = new FillStorePeerListClientBehavior(peers.get(0), peers.get(0).getPeerList(), outputNumOfFragments);
		fsplcb.addFullFillPeerListener(new FullFillPeerListener() {
			
			public void action(PeerListManager plm) {
				toFill = plm;
				stoptest = true;
				thread.interrupt();
			}
		});
		l.add(fsplcb);
		
		for (int i = 1; i < numberOfClients; i++) {
			// peers.get(i);
			l.add(new FillPeerListServerBehavior(peers.get(i)));// , file);
			// System.out.println("Init Coding Engine Behaviour "+i);
		}

		thread = new Thread(l.get(0));
		thread.start();

		while (!stoptest) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertTrue(outputNumOfFragments <= toFill.size());
	}

	@Test
	public void testNCClientBehaviour() {
		// NCClientBehaviour b = new NCClientBehaviour();
	}

}
