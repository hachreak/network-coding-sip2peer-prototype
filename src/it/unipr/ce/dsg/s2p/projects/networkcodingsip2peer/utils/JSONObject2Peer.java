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

package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.utils;

import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;
import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class JSONObject2Peer {
	public static PeerDescriptor json2peerdescriptor(JSONObject keyPeer)
			throws JSONException {
		//System.out.println("[keypeer]\n"+keyPeer+"\n[/keypeer]");
		PeerDescriptor neighborPeerDesc = new PeerDescriptor(keyPeer
				.get("name").toString(), keyPeer.get("address").toString(),
				keyPeer.get("key").toString());

		String contactaddress;
		if ((contactaddress = keyPeer.get("contactAddress").toString()) != null) {
			neighborPeerDesc.setContactAddress(contactaddress);
		}

		return neighborPeerDesc;
	}

}
