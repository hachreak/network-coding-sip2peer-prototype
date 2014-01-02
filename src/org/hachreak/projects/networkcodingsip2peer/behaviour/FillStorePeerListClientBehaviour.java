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

import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;

/**
 * Behavior of a NetworkCoding Client: publish a file and maintain the resource
 * available, controlling if there are enough store server
 * 
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class FillStorePeerListClientBehaviour extends FillPeerListClientBehaviour {

	private PeerListManager peerListToRefill;
	private PeerListManager peerListToAsk;

	public FillStorePeerListClientBehaviour(SimplePeer peer,
			PeerListManager peerList, int peerRequired) {
		super(peer, peerList, peerRequired);
		
		peerListToRefill = new PeerListManager();
	}

	@Override
	protected PeerListManager getPeerListToRefill() {
		return peerListToRefill;
	}

	@Override
	protected PeerListManager getPeerListToAsk() {
		return peerListToAsk;
	}

	@Override
	protected void setInitialPeerListToAsk(PeerListManager peerList) {
		this.peerListToAsk = peerList;
	}
	
	@Override
	protected void fireFullFillStorePeerList() {
		// remove me from behaviours
		getPeer().getBehaviours().remove(this.getClass().toString());

		// advice listeners
		super.fireFullFillStorePeerList();
	}
	
//
////	private SimplePeer peer;
//
//	private int countPendingRequest = 0;
//
//	private int peerRequiredForSegmentFile = -1;
//
//	private PeerListManager storePeerList;
//
//	private List<FullFillPeerListener> fullFillStorePeerlisteners = new ArrayList<FullFillPeerListener>();
//
//	public FillStorePeerListBehaviour(SimplePeer peer) throws IOException {
//		super(peer);
////		this.peer = peer;
//
//		// add myself into the behavoiur list
////		peer.getBehaviours().add(this);
//
//		storePeerList = peer.getPeerList();
//	}
//
//	public void askForStorePeerList() throws Exception {
//		if (peerRequiredForSegmentFile == -1)
//			throw new Exception(
//					"peerRequiredForSegmentFile not initialized - set number of output fragments after coding");
//
//		// System.out.println("output num fragments: "+peerRequiredForSegmentFile);
//		// System.out.println("peer list size: "+storePeerList.size());
//
//		if (storePeerList.size() < peerRequiredForSegmentFile) {
//			int peers_mancanti = peerRequiredForSegmentFile
//					- storePeerList.size();
//
//			int peers_req_by_each_peer = getPeerRequiredFromEachPeer(peers_mancanti);
//
//			// System.out.println("peers required by each peer: "+peers_req_by_each_peer);
//			// System.out.println("# peer da recuperare: "+peers_mancanti);
//
//			PeerListManager p = (PeerListManager) storePeerList.clone();
//
//			Iterator<Entry<String, NeighborPeerDescriptor>> i = p.entrySet()
//					.iterator();
//
//			if (peers_mancanti > 0) {
//				while (i.hasNext()) {
//					// mark a request of a peer list
//					countPendingRequest++;
//					// send request of a peer list
//					getPeer().send(
//							i.next().getValue(),
//							new FillStorePeerListMessage(getPeer()
//									.getPeerDescriptor(),
//									peers_req_by_each_peer));
//				}
//			}
//		}
//	}
//
//	/**
//	 * Ask to each peer of storePeerList some peer
//	 * 
//	 * @param totalPeers
//	 *            total peer required
//	 * @return number of peer to require at each peer of peer list
//	 */
//	protected int getPeerRequiredFromEachPeer(int totalPeers) {// , int
//																// peerListSize){
//		return Math.round(totalPeers / storePeerList.size()) + 1;
//	}
//
//	public void onReceivedJSONMsg(String type, JSONObject jsonMsg) {
//		try {
//			//
//			JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject(
//					"params");
//
//			// if required a list of Store Peer
//			if (type.equals(FillStorePeerListMessage.MSG_FILL_STORE_PEER_LIST)) {
//
//				NeighborPeerDescriptor peerdesc = new NeighborPeerDescriptor(
//						JSONObject2Peer.json2peerdescriptor(params));
//
//				int numPeerList = (Integer) jsonMsg.get("numPeerList");
//
//				// send Store Peer List
//				PeerListManager plm = null;
//				if (numPeerList > storePeerList.size())
//					plm = (PeerListManager) storePeerList.clone();// peer.getPeerList();
//				else
//					plm = storePeerList.getRandomPeers(numPeerList);
//
//				// System.out.println("[BEHAVIOUR] Send # "+plm.size());
//				getPeer().send(peerdesc, new PeerListMessage(
//						FillStorePeerListMessage.MSG_FILL_STORE_PEER_LIST, plm));
//			} else
//			// If I receive a Peer List
//			if (type.equals(PeerListMessage.MSG_PEER_LIST)) {
//				// get the type of peer list
//				String typeOfPeerList = (String) jsonMsg.get("typeOfPeerList");
//
//				// If it is a Store Peer List
//				if (typeOfPeerList
//						.equals(FillStorePeerListMessage.MSG_FILL_STORE_PEER_LIST)) {
//					// mark the reception of a peer list
//					countPendingRequest--;
//					
//					// add new peer in my peer list
//					SimplePeer.addNeighborPeerList(storePeerList,
//							JSONObject2Peer.json2peerList(params));
//
////					System.out
////							.println("[BEHAVIOUR] peer list size after receive [ "
////									+ storePeerList.size() + " ]");
//
//					// if I not reached the peer list size required
//					if (storePeerList.size() < peerRequiredForSegmentFile) {
////						System.out
////								.println("[[[ BEHAVIOUR ]]] I have NOT enough peer ");
//
//						// if each request is satisfied
//						if (countPendingRequest == 0) {
//							// a start a new request
//							this.askForStorePeerList();
//						}
//					} else {
//						// I reached the peer list size required
//						fireFullFillStorePeerList();
////						System.out.println("[[[ BEHAVIOUR ]]] I have enough peer");
//					}
//
////					System.out.println("[ count ] " + countPendingRequest);
//				}
//			}
//
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Fire a new event: I reach the peer list size required
//	 */
//	private void fireFullFillStorePeerList() {
//		Iterator<FullFillPeerListener> i = fullFillStorePeerlisteners
//				.iterator();
//		while (i.hasNext())
//			i.next().action(storePeerList);
//	}
//
//	public void setPeerRequiredForSegmentFile(int peerRequiredForSegmentFile) {
//		this.peerRequiredForSegmentFile = peerRequiredForSegmentFile;
//	}
//
//	public void run() {
//		try {
//			askForStorePeerList();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public void addFullFillStorePeerListener(FullFillPeerListener listener) {
//		fullFillStorePeerlisteners.add(listener);
//	}

}
