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

package org.hachreak.projects.networkcodingsip2peer.behaviour;

import it.unipr.ce.dsg.s2p.peer.PeerListManager;

import org.hachreak.projects.networkcodingsip2peer.exceptions.NoBootstrapConfiguredException;
import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;

public class BootstrapClientBehaviour extends FillPeerListClientBehaviour {
	
	private PeerListManager peerListToRefill;
	private PeerListManager peerListToAsk;

	public BootstrapClientBehaviour(SimplePeer peer, int peerRequired) throws NoBootstrapConfiguredException {
		super(peer, peer.getBootstrapPeer(), peerRequired);
		
		peerListToRefill = peer.getPeerList();
	}

	@Override
	protected PeerListManager getPeerListToRefill() {
//		System.out.println("[BootstrapClientBehaviour] get peer list to refill! "+peerListToRefill.size());
		return peerListToRefill;
	}

	@Override
	protected PeerListManager getPeerListToAsk() {
//		System.out.println("[BootstrapClientBehaviour] get peer list to ask! "+peerListToAsk.size());
		return peerListToAsk;
	}

	@Override
	protected void setInitialPeerListToAsk(PeerListManager peerList) {
//		this.peerListToAsk = new PeerListManager();
//		this.peerListToAsk.put(bootstrap.getKey(), bootstrap);
		this.peerListToAsk = peerList;
	}

	@Override
	protected void fireFullFillStorePeerList() {
		// remove me from behaviours
		getPeer().getBehaviours().remove(this.getClass().toString());

//		System.out.println("[BootstrapClientBehaviour] size behaviour ");//+getPeer().getBehaviours().size()+ " da cancellare: "+k);
		// advice listeners
		super.fireFullFillStorePeerList();
	}

	
}
