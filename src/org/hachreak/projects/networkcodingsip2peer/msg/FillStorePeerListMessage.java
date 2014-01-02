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

import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;

/**
 * It's a request to fill his personal Peer List with a indicated number of peers
 */
public class FillStorePeerListMessage extends RefillPeerListMessage {

	public static final String MSG_FILL_STORE_PEER_LIST = "fill_store_peer_list";
	
	/**
	 * 
	 * @param peerDesc Peer descriptor of peer that require to join
	 * @param num_peers request this number of peers
	 */
	public FillStorePeerListMessage(PeerDescriptor peerDesc, int num_peers) {
		super(peerDesc, num_peers, FillStorePeerListMessage.MSG_FILL_STORE_PEER_LIST);
	}
	
}
