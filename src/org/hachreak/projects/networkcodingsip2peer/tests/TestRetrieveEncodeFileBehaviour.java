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
import org.hachreak.projects.networkcodingsip2peer.behaviour.Behaviour;
import org.hachreak.projects.networkcodingsip2peer.behaviour.PublishEncodedFileClientBehaviour;
import org.hachreak.projects.networkcodingsip2peer.behaviour.RetrieveEncodedFileClientBehaviour;
import org.hachreak.projects.networkcodingsip2peer.behaviour.StorageFragmentsServerBehaviour;
import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;
import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragment;
import org.hachreak.projects.networkcodingsip2peer.resource.MapUniqueFragmentStorageFragments;
import org.hachreak.projects.networkcodingsip2peer.resource.MediaResource;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRetrieveEncodeFileBehaviour {

	private int numberOfClients = 100;
	private String configFileBootstrap = "config/bootstrappeertest.cfg";
	private String configFile = "config/TestRetrieveEncodeFileBehaviour.cfg";
	private File file = new File("tests/TestRetrieveEncodeFileBehaviour.txt");
	// private File file = new
	// File("tests/TestNetworkCodingEngineNotMultiple.txt.zip");
	private File fileOutput = new File(
			"tests/TestRetrieveEncodeFileBehaviour.decoded.txt");
	private Thread thread;
	private List<EncodedFragment> fragments;
	private int received;
	private List<EncodedFragment> fragmentsRetrieved;
	private InitNetwork initializer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		initializer = new InitNetwork(configFileBootstrap, configFile);
	}

	@After
	public void stopNetwork(){
		initializer.stopNetwork();
	}
	
	@Test
	public void testRetrieveFragments() throws IOException {
		fragments = new ArrayList<EncodedFragment>();
		fragmentsRetrieved = new ArrayList<EncodedFragment>();
		
		// get peers of network
		List<SimplePeer> peers = initializer.getPeers();

		List<Behaviour> l = new ArrayList<Behaviour>(numberOfClients);
		
		// behaviour: publish a file
		PublishEncodedFileClientBehaviour pefcb = new PublishEncodedFileClientBehaviour(
				peers.get(0), peers.get(0).getPeerList(), file);
		// behaviour: retrieve a file
		RetrieveEncodedFileClientBehaviour refcb = new RetrieveEncodedFileClientBehaviour(
				peers.get(0), peers.get(0).getPeerList(), file, new MapUniqueFragmentStorageFragments(){

					@Override
					public void put(EncodedFragment fragment) {
						// save retrieved fragment
						super.put(fragment);
						
						received++;
						System.out.println("[TEST] Fragment retrieved");
					}					
				});
		
		l.add(pefcb);
		l.add(refcb);
		
		received = 0;
		// for each other peer: add "Store Fragments Peer" behaviour 
		for (int i = 1; i < peers.size(); i++) {
			StorageFragmentsServerBehaviour pfsb = new StorageFragmentsServerBehaviour(
					peers.get(i), new MapUniqueFragmentStorageFragments() {

						@Override
						public void put(EncodedFragment fragment) {
							// fragment to store
							super.put(fragment);
							received++;
							System.out.println("[TEST] Fragment received");
							fragmentsRetrieved.add(fragment);
						}
					});
			l.add(pfsb);
		}

		// get number of output encoded fregments
		int outNumOfFragment = pefcb.getCodingEngine().getOutputNumOfFragments(
				pefcb.getMediaResource());
		// get number of original file fragment
		int inNumOfFragment = pefcb.getMediaResource().getNumberOfFragments();
		
		System.out.println("[TEST] output num of fragments:"
				+ outNumOfFragment);
		System.out.println("[TEST] input num of fragments: "
				+ inNumOfFragment);
		
		// [ publish file ]
		System.out.println("Publish fragments...");
		
		received = 0;
		
		thread = new Thread(pefcb);
		thread.start();

		while (received < outNumOfFragment) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		thread.interrupt();
		
		// [ retrieve file ]

		System.out.println("Retrieve fragments...");
		
		received = 0;

		thread = new Thread(refcb);
		thread.start();

		while (received < inNumOfFragment) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		thread.interrupt();

		// try to decode resource
		System.out.println("Decode retrieved fragments...");
		
		char[][] buffer;
		try {
			buffer = pefcb.getCodingEngine().decode(fragmentsRetrieved);

			areEquals(buffer, file);

			MediaResource.save(fileOutput, buffer, file.length());
		} catch (GFMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private void areEquals(char[][] data, File originalFile) throws IOException {
		RandomAccessFile reader = new RandomAccessFile(originalFile, "r");

		long j, k, n = data[0].length;
		for (int i = 0; i < originalFile.length(); i++) {
			char orig = (char) reader.read();

			j = i % n;
			k = i / n;

			assertTrue(orig == data[(int) k][(int) j]);
		}

		reader.close();
	}

}
