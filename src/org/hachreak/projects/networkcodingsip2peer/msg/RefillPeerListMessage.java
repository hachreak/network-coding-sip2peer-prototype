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

package org.hachreak.projects.networkcodingsip2peer.msg;

import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragment;

import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.Payload;
import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 *
 */
public class RefillPeerListMessage extends BasicMessage {

	public static final String MSG_REFILL_PEER_LIST = "refill_peer_list";
	
	/**
	 * Number of neighbor in the peer's list
	 * 
	 * If numPeerList=0 the BootstrapPeer sends full peer list
     * If numPeerList=-1 the BootstrapPeer not sends the peer list
     * Per default all peer are requested
	 */
	private int numPeerList = 0;
	
	/**
	 * 
	 * @param fragment
	 */
	public RefillPeerListMessage(PeerDescriptor peerDesc, int num_peers) {
		super(MSG_REFILL_PEER_LIST, new Payload(peerDesc));
		setNumPeerList(num_peers);
	}
	
	/**
	 * 
	 * @param num_peers number of peers
	 * @param type type of message
	 */
	public RefillPeerListMessage(PeerDescriptor peerDesc, int num_peers, String type) {
		super(type, new Payload(peerDesc));
		setNumPeerList(num_peers);
	}
	
	/**
	 * @return the numNeighborPeerList
	 */
	public int getNumPeerList() {
		return numPeerList;
	}

	/**
	 * @param numNeighborPeerList the numNeighborPeerList to set
	 */
	public void setNumPeerList(int numPeerList) {
		this.numPeerList = numPeerList;
	}
}
