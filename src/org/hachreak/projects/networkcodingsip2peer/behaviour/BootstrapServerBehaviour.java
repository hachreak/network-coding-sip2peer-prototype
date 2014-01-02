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

import it.unipr.ce.dsg.s2p.peer.NeighborPeerDescriptor;

import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;

public class BootstrapServerBehaviour extends FillPeerListServerBehaviour {

	public BootstrapServerBehaviour(SimplePeer peer) {
		super(peer);
//		System.out.println("[BootstrapServerBehaviour] init....");
	}

	@Override
	protected NeighborPeerDescriptor havePeerDescriptor(
			NeighborPeerDescriptor neighborPeerDescriptor) {
//		System.out.println("[BootstrapServerBehaviour] add peer.........");
		// add the new peer descriptor in the available peer list
		getPeerList().put(neighborPeerDescriptor.getKey(), neighborPeerDescriptor);
		return super.havePeerDescriptor(neighborPeerDescriptor);
	}

}
