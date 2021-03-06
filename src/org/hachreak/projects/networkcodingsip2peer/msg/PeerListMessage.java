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

import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.Payload;
import it.unipr.ce.dsg.s2p.peer.PeerListManager;

/**
 * This message include a peer list
 * 
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 *
 */
public class PeerListMessage extends BasicMessage {

	public static final String MSG_PEER_LIST = "peer_list";
	
	private String typeOfPeerList = PeerListMessage.MSG_PEER_LIST;

	public PeerListMessage(PeerListManager manager) {
		super(PeerListMessage.MSG_PEER_LIST, new Payload(manager));
	}

	public PeerListMessage(String type, PeerListManager manager) {
		super(PeerListMessage.MSG_PEER_LIST, new Payload(manager));
		typeOfPeerList = type;
	}
	
	public void setTypeOfPeerList(String type){
		this.typeOfPeerList = type;
	}
	
	public String getTypeOfPeerList(){
		return this.typeOfPeerList;
	}
}
