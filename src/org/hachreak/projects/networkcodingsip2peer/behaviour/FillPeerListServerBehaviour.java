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

import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;
import it.unipr.ce.dsg.s2p.peer.NeighborPeerDescriptor;
import it.unipr.ce.dsg.s2p.peer.PeerListManager;

import org.hachreak.projects.networkcodingsip2peer.msg.PeerListMessage;
import org.hachreak.projects.networkcodingsip2peer.msg.RefillPeerListMessage;
import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;
import org.hachreak.projects.networkcodingsip2peer.utils.JSONObject2Peer;

/**
 * Behavior of a NetworkCoding Client: publish a file and maintain the resource
 * available, controlling if there are enough store server
 * 
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class FillPeerListServerBehaviour extends Behaviour {

	private PeerListManager peerList;

	public FillPeerListServerBehaviour(SimplePeer peer) {
		super(peer);

		peerList = peer.getPeerList();
	}

	public PeerListManager getPeerList() {
		return peerList;
	}

	public void onReceivedJSONMsg(String type, JSONObject jsonMsg) {
		try {
			JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject(
					"params");

			// if required a list of Peer
			if (type.equals(RefillPeerListMessage.MSG_REFILL_PEER_LIST)) {

				NeighborPeerDescriptor peerdesc = havePeerDescriptor(new NeighborPeerDescriptor(
						JSONObject2Peer.json2peerdescriptor(params)));

				// send Store Peer List
				PeerListManager plm = getRandomPeerList(
						jsonMsg.getInt("numPeerList"), params.get("key")
								.toString());

				getPeer()
						.send(peerdesc,
								new PeerListMessage(
										RefillPeerListMessage.MSG_REFILL_PEER_LIST,
										plm));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receive a request from a new peer
	 * 
	 * @param neighborPeerDescriptor
	 * @return
	 */
	protected NeighborPeerDescriptor havePeerDescriptor(
			NeighborPeerDescriptor neighborPeerDescriptor) {
		return neighborPeerDescriptor;
	}

	public void run() {
	}

	/**
	 * Get a random peer list
	 * 
	 * @param numPeerList
	 *            number of peer required by the peer
	 * @param senderKey
	 *            sender key (to exclude him from the returned list)
	 * @return random list (without sender)
	 * @throws Exception
	 */
	private PeerListManager getRandomPeerList(int numPeerList, String senderKey)
			throws Exception {

		// check the number of peer in the list
		if (numPeerList < 0) {
			throw new Exception("num peers not specified");
		}

		// Send peer list!

		PeerListManager plm = null;
		if (numPeerList == 0 || peerList.size() <= numPeerList) {
			// get all peer list
			plm = (PeerListManager) peerList.clone();
		} else {
			// get "numPeerList" random peer
			plm = peerList.getRandomPeers(numPeerList + 1);
		}

		// remove sender peer from the list
		if (plm.containsKey(senderKey)) {
			plm.remove(senderKey);
		} else {
			plm.remove(plm.keys().nextElement());
		}

		// return peer list to peer
		return plm;
	}

}
