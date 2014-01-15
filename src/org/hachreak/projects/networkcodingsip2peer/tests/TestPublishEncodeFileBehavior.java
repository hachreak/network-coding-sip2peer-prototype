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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.hachreak.projects.gfjama.matrix.GFMatrixException;
import org.hachreak.projects.networkcodingsip2peer.behavior.Behavior;
import org.hachreak.projects.networkcodingsip2peer.behavior.PublishEncodedFileClientBehavior;
import org.hachreak.projects.networkcodingsip2peer.behavior.StorageFragmentsServerBehavior;
import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;
import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragment;
import org.hachreak.projects.networkcodingsip2peer.resource.MediaResource;
import org.hachreak.projects.networkcodingsip2peer.resource.StorageFragments;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestPublishEncodeFileBehavior {

//	private int numberOfClients = 100;
//	private List<SimplePeer> peers = new ArrayList<SimplePeer>();
//	private BootstrapPeer bootstrapPeer;
	private String configFileBootstrap = "config/bootstrappeertest.cfg";
	private String configFile = "config/TestPublishEncodeFileBehaviour.cfg";
	private int port = 5081;
	private int totalLoaded = 0;
	private File file = new File("tests/TestPublishEncodeFileBehaviour.txt");
//	private File file = new File("tests/TestNetworkCodingEngineNotMultiple.txt.zip");
	private File fileOutput = new File("tests/TestPublishEncodeFileBehaviour.decoded.txt");
	private Thread thread;
	private List<EncodedFragment> fragments;
	private int received;
	private InitNetwork initializer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		initializer = new InitNetwork(configFileBootstrap, configFile);

//		initNetwork();
	}

	@After
	public void stopNetwork(){
		initializer.stopNetwork();
	}

	@Test
	public void testPublishFragments() throws IOException {
		fragments = new ArrayList<EncodedFragment>();
		List<SimplePeer> peers = initializer.getPeers();
		int numberOfClients = peers.size();
		
		List<Behavior> l = new ArrayList<Behavior>(numberOfClients);
		PublishEncodedFileClientBehavior pefcb = new PublishEncodedFileClientBehavior(peers.get(0), peers.get(0)
				.getPeerList(), file);
		l.add(pefcb);
		received = 0;
		for (int i = 1; i < peers.size(); i++) {
			StorageFragmentsServerBehavior pfsb = new StorageFragmentsServerBehavior(peers.get(i), new StorageFragments(){
				
				public void put(EncodedFragment fragment) {
					fragments.add(fragment);
					received++;
					System.out.println("[TEST] Fragment received");
				}
				
				public List<EncodedFragment> getAll(byte[] resourceKey) {
					// TODO Auto-generated method stub
					return null;
				}
				
				public EncodedFragment get(byte[] resourceKey) {
					// TODO Auto-generated method stub
					return null;
				}
			});

			l.add(pfsb);
		}
		
		PublishEncodedFileClientBehavior b = (PublishEncodedFileClientBehavior) l
				.get(0);
		
		thread = new Thread(b);
		thread.start();

		System.out.println("[TEST] output num of fragments:" +pefcb.getCodingEngine().getOutputNumOfFragments(pefcb.getMediaResource()));
		System.out.println("[TEST] input num of fragments: "+pefcb.getMediaResource().getNumberOfFragments());
		while (received < pefcb.getCodingEngine().getOutputNumOfFragments(pefcb.getMediaResource())) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		assertTrue(true);

		try {
			List<EncodedFragment> f = new ArrayList<EncodedFragment>();
			for(int i=0; i<pefcb.getMediaResource().getNumberOfFragments(); i++){
				f.add(fragments.get(i));
			}

			char[][] buffer = pefcb.getCodingEngine().decode(f);

			areEquals(buffer, file);

			MediaResource.save(fileOutput, buffer, file.length());
		} catch (GFMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void areEquals(char[][] data, File originalFile) throws IOException{
		RandomAccessFile reader = new RandomAccessFile(originalFile, "r");
		
		long j,k, n = data[0].length;
		for(int i=0; i<originalFile.length(); i++){
			char orig = (char) reader.read();

			j = i % n;
			k = i / n;
			
			assertTrue(orig == data[(int) k][(int) j]);
		}
		
		reader.close();
	}
//	@Test
//	public void testPublishEncodeFileBehaviourSimplePeer() {
//		fail("Not yet implemented");
//	}

}
