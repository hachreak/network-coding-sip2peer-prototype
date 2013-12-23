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

package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer;

import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.parser.BasicParser;
import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;
import it.unipr.ce.dsg.s2p.peer.NeighborPeerDescriptor;
import it.unipr.ce.dsg.s2p.peer.Peer;
import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;
import it.unipr.ce.dsg.s2p.peer.PeerListManager;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.msg.JoinMessage;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.msg.PeerListMessage;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.msg.RefillPeerListMessage;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.utils.JSONObject2Peer;
import it.unipr.ce.dsg.s2p.sip.Address;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class BootstrapPeer extends Peer {

	public BootstrapPeer(String pathConfig/*, String key*/) {
		// echo -n it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.BootstrapPeer|md5sum
		super(pathConfig, "bootstrap");//"b7d513f84ad6813570082c2e8971e894");
	}

	/**
	 * @Override
	 */
	protected void onDeliveryMsgFailure(String peerMsgSended, Address receiver,
            String contentType) {
        //System.out.println("onDeliveryMsgFailure: " + peerMsgSended);
	}

	/**
	 * @Override
	 */
	protected void onDeliveryMsgSuccess(String peerMsgSended, Address receiver,
            String contentType) {
		//System.out.println("onDeliveryMsgSuccess: " + peerMsgSended);
	}

	/* (non-Javadoc)
	 * @see it.unipr.ce.dsg.s2p.peer.Peer#onReceivedJSONMsg(it.unipr.ce.dsg.s2p.org.json.JSONObject, it.unipr.ce.dsg.s2p.sip.Address)
	 */
	/**
	 * @Override
	 */
	protected void onReceivedJSONMsg(JSONObject jsonMsg, Address sender) {
		// super.onReceivedJSONMsg(jsonMsg, sender);

		// join a new node
		try {
			//System.out.println("[BOOTSTRAP] received "+jsonMsg.get("type"));

			String type = (String) jsonMsg.get("type");
			
			if(type.equals(JoinMessage.MSG_PEER_JOIN) || type.equals(RefillPeerListMessage.MSG_REFILL_PEER_LIST)){
				JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject("params");
				// rebuild a peer descriptor for the peer 
				PeerDescriptor peerdesc = JSONObject2Peer.json2peerdescriptor(params);
				// add neighbor peer in the list of peer connected
				//System.out.println("[peer list prima]\n"+peerList+"[/peer list prima]");
				NeighborPeerDescriptor neighborPeer = addNeighborPeer(peerdesc);
				//System.out.println("[peer list dopo]\n"+peerList+"[/peer list dopo]");
				// send random peer list
				send(neighborPeer, new PeerListMessage(getRandomPeerList(jsonMsg)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private PeerListManager getRandomPeerList(JSONObject jsonMsg) throws Exception{
		JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject("params");
		
		// get the number of peer required by the peer
		int numPeerList = (Integer) jsonMsg.get("numPeerList");
		
		// check the number of peer in the list
		if(numPeerList < 0){
			throw new Exception("num peers not specified");
		}
			// Send peer list!
			
			PeerListManager plm = null;
			if(numPeerList == 0 || peerList.size() <= numPeerList){
				// get all peer list
				plm = (PeerListManager) peerList.clone();
			}else{
				// get "numPeerList" random peer
				plm = peerList.getRandomPeers(numPeerList + 1);
			}
			
			// remove sender peer from the list
			String senderKey = params.get("key").toString();
			if(plm.containsKey(senderKey)){
				plm.remove(senderKey);
			}else{
				plm.remove(plm.keys().nextElement());
			}
						
			// return peer list to peer
			return plm;
//			if(plm != null){
//				//System.out.println("Invia a "+neighborPeer);
//				//send(neighborPeer, new PeerListMessage(plm));
//				return p
//			}
				
//		}
	}
}
