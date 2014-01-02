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

import java.util.List;

import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class TestSimplePeer {

	private String configFile = "config/simplepeertest.cfg";

	private String configFileBootstrap = "config/bootstrappeertest.cfg";

	private InitNetwork initializer;


	/**
	 * @throws java.lang.Exception
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		initializer = new InitNetwork(configFileBootstrap, configFile);
	}

	@After
	public void stop(){
		initializer.stopNetwork();
	}
	
	/**
	 * Test method for
	 * {@link org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer#SimplePeer(java.lang.String)}
	 * .
	 * 
	 */
	@Test
	public void testSimplePeerPeerList() {
		List<SimplePeer> peers = initializer.getPeers();
		PeerListManager p = peers.get(0)
				.getPeerList();
		assertTrue(p.size() > 1);
//		int peerListReq = initializer.getPeers().size() - 1;
		int peerListReq = Integer.parseInt(initializer.getPeers().get(0).getProperty(SimplePeer.REQ_NPEER));
//		System.out.println("[TEST] peer list req: "+peerListReq+" peer size: "+p.size());
		for(int i=0; i<peers.size(); i++){
//			System.out.println("peer ("+i+") have "+peers.get(i).getPeerList().size()+" peers");
			assertTrue (
				peers.get(i).getPeerList().size() >= peerListReq);
		}

	}

}
