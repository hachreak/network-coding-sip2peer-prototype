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

package org.hachreak.projects.networkcodingsip2peer.behavior;

import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;
import it.unipr.ce.dsg.s2p.peer.NeighborPeerDescriptor;
import it.unipr.ce.dsg.s2p.peer.PeerListManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.hachreak.projects.networkcodingsip2peer.actionListener.FullFillPeerListener;
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
public class FillPeerListClientBehavior extends Behavior {

	private int countPendingRequest = 0;

	private int peerRequired = -1;

	private PeerListManager peerList;

	private List<FullFillPeerListener> fullFillPeerlisteners = new ArrayList<FullFillPeerListener>();

	public FillPeerListClientBehavior(SimplePeer peer,
			PeerListManager peerList, int peerRequired) {
		super(peer);

		init(peer, peerList, peerRequired);
	}

	public FillPeerListClientBehavior(SimplePeer peer,
			NeighborPeerDescriptor desc, int peerRequired) {
		super(peer);

		PeerListManager peerList = new PeerListManager();
		peerList.put(desc.getKey(), desc);

		init(peer, peerList, peerRequired);
	}

	private void init(SimplePeer peer, PeerListManager peerList,
			int peerRequired) {
		this.peerRequired = peerRequired;

		setInitialPeerListToAsk(peerList);
	}

	public void askForStorePeerList() {
		PeerListManager peerListToRefill = getPeerListToRefill();

		if (peerListToRefill.size() < peerRequired) {
			int peers_mancanti = peerRequired - peerListToRefill.size();

			int peers_req_by_each_peer = getPeerRequiredFromEachPeer(peers_mancanti);

			PeerListManager p = (PeerListManager) getPeerListToAsk().clone();

			Iterator<Entry<String, NeighborPeerDescriptor>> i = p.entrySet()
					.iterator();

			if (peers_mancanti > 0) {
				while (i.hasNext()) {
					// mark a request of a peer list
					countPendingRequest++;

					NeighborPeerDescriptor pd = i.next().getValue();

					// send request of a peer list
					System.out.println("[FillPeerClient] ask "+peers_req_by_each_peer+" peers");
					getPeer().send(
							pd,
							new RefillPeerListMessage(getPeer()
									.getPeerDescriptor(),
									peers_req_by_each_peer));
				}
			}
		}
	}

	/**
	 * Ask to each peer of storePeerList some peer
	 * 
	 * @param totalPeers
	 *            total peer required
	 * @return number of peer to require at each peer of peer list
	 */
	private int getPeerRequiredFromEachPeer(int totalPeers) {// , int
																// peerListSize){
		return Math.round(totalPeers / getPeerListToAsk().size()) + 1;
	}

	public void onReceivedJSONMsg(String type, JSONObject jsonMsg) {
//		 System.out.println("[FillPeerClient] msg type "+type);

		try {
			//
			JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject(
					"params");

			// If I receive a Peer List
			if (type.equals(PeerListMessage.MSG_PEER_LIST)) {
				// get the type of peer list
				String typeOfPeerList = jsonMsg.getString("typeOfPeerList");

				// If it is a Store Peer List
				if (typeOfPeerList
						.equals(RefillPeerListMessage.MSG_REFILL_PEER_LIST)) {
					// mark the reception of a peer list
					countPendingRequest--;

					// add new peer in my peer list
					SimplePeer.addNeighborPeerList(getPeerListToRefill(),
							JSONObject2Peer.json2peerList(params));

					// if I not reached the peer list size required
					if (getPeerListToRefill().size() < peerRequired) {
						// if each request is satisfied
						if (countPendingRequest == 0) {
							try {
								// wait random time before next call
								long t = (long) (Math.random() * 5000);
								// System.out.println("sleep "+t);
								Thread.sleep(t);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
//							System.out.println("[FillPeerClient] size("+getPeerListToRefill().size()+") required: "+peerRequired);
							// a start a new request
							askForStorePeerList();
						}
					} else {
						// I reached the peer list size required
						// System.out.println("[[[ BEHAVIOUR ]]] I have enough peer ");
						fireFullFillPeerList();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fire a new event: I reach the peer list size required
	 */
	protected void fireFullFillPeerList() {
		// advice listeners
		PeerListManager peerListFilled = getPeerListToRefill();
		Iterator<FullFillPeerListener> i = fullFillPeerlisteners.iterator();
		while (i.hasNext())
			i.next().action(peerListFilled);
	}

	public void run() {
		try {
			askForStorePeerList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addFullFillPeerListener(FullFillPeerListener listener) {
		fullFillPeerlisteners.add(listener);
	}

	protected PeerListManager getPeerListToRefill() {
		return peerList;
	}

	protected PeerListManager getPeerListToAsk() {
		return peerList;
	}

	protected void setInitialPeerListToAsk(PeerListManager peerList) {
		this.peerList = peerList;
	}

}
