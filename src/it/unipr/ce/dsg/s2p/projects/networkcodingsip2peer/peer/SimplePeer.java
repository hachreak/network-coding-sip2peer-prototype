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

import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;
import it.unipr.ce.dsg.s2p.peer.NeighborPeerDescriptor;
import it.unipr.ce.dsg.s2p.peer.Peer;
import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;
import it.unipr.ce.dsg.s2p.peer.PeerListManager;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.actionListener.FillPeerListListener;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.exceptions.NoBootstrapConfiguredException;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.exceptions.NotEnoughPeerException;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.msg.JoinMessage;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.msg.PeerListMessage;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.msg.RefillPeerListMessage;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.utils.JSONObject2Peer;
import it.unipr.ce.dsg.s2p.sip.Address;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class SimplePeer extends Peer {

	/**
	 * Number of peer to request at least
	 */
	private static final String REQ_NPEER_DEFAULT = "10";
	public static final String REQ_NPEER = "req_npeer";
	public static final String BOOTSTRAP_PEER = "bootstrap_peer";

	private Address bootstrapPeer = null;

//	private List<FillPeerListListener> fillPeerListListener = new ArrayList<FillPeerListListener>();
	
	/**
	 * configuration's file
	 */
	protected Properties configFile = new java.util.Properties();

	/**
	 * Create a new peer and join the network
	 * 
	 * @param pathConfig
	 *            configuration file name for peer
	 * @param key
	 *            identifies the peer
	 * @throws IOException
	 * @throws NoBootstrapConfiguredException
	 */
	public SimplePeer(String pathConfig, String key) throws IOException,
			NoBootstrapConfiguredException {
		super(pathConfig, key);

		// init peer
		init(pathConfig);
	}

	private void init(String pathConfig) throws IOException,
			NoBootstrapConfiguredException {
		InputStream i = new FileInputStream(pathConfig);// this.getClass().getClassLoader().

		// load peer configuration
		configFile.load(i);

		// join bootstrap peer
		joinBootstrapPeer();
	}

	/**
	 * Return the bootstrap address
	 * 
	 * @return
	 * @throws NoBootstrapConfiguredException
	 */
	public Address getBootstrapPeer() throws NoBootstrapConfiguredException {
		if (bootstrapPeer == null) {
			// get bootstrap address from configuration
			String bootstrapPeerName = configFile.getProperty(BOOTSTRAP_PEER);

			// if isn't set, raise an exception
			if (bootstrapPeerName == null) {
				throw new NoBootstrapConfiguredException();
			}

			bootstrapPeer = new Address(bootstrapPeerName);
		}

		return bootstrapPeer;
	}

	/**
	 * Join the network
	 * 
	 * @throws NoBootstrapConfiguredException
	 */
	private void joinBootstrapPeer() throws NoBootstrapConfiguredException {
		// join msg
		JoinMessage newJoinMsg = new JoinMessage(peerDescriptor,
				Integer.parseInt(configFile.getProperty(REQ_NPEER,
						REQ_NPEER_DEFAULT)));
		// send join message
		send(getBootstrapPeer(), newJoinMsg);
	}

	@Override
	protected void onDeliveryMsgFailure(String arg0, Address arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDeliveryMsgSuccess(String arg0, Address arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.unipr.ce.dsg.s2p.peer.Peer#onReceivedJSONMsg(it.unipr.ce.dsg.s2p.org
	 * .json.JSONObject, it.unipr.ce.dsg.s2p.sip.Address)
	 */
	@Override
	protected void onReceivedJSONMsg(JSONObject jsonMsg, Address sender) {
		// TODO Auto-generated method stub
		try {
			// System.out.println("msg arrivato: "+jsonMsg.get("type"));

			// get params from the payload
			JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject(
					"params");
			// get type of message
			String type = (String) jsonMsg.get("type");

			// if I request the peer list
			if (type.equals(PeerListMessage.MSG_PEER_LIST)) {
				loadPeerList(params);
				// System.out.println("[PEER LIST] "+this.peerList.size());
			}else if(type.equals(RefillPeerListMessage.MSG_REFILL_PEER_LIST)){
				loadPeerList(params);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// super.onReceivedJSONMsg(jsonMsg, sender);
	}

	protected void loadPeerList(JSONObject params) throws JSONException {
		// save neighbor peer list
		Iterator<String> i = params.keys();
		while (i.hasNext()) {
			// get key of peer
			String key = i.next();
			// rebuild peer descriptor
			PeerDescriptor pd = JSONObject2Peer.json2peerdescriptor(params
					.getJSONObject(key));
			// add the neighbor peer
			addNeighborPeer(pd);
			// System.out.println(this.getAddress() + "\nadd neighbot: "+pd);
		}
	//	System.out.println(peerList);
	}

	/**
	 * Return "num" peers from the neighbor peer list
	 * 
	 * @param num
	 *            how many peers
	 * @return
	 * @throws NotEnoughPeerException
	 */
	protected Collection<NeighborPeerDescriptor> getRandomPeers(int num)
			/*throws NotEnoughPeerException*/ {
		if (peerList.size() < num) {
			//throw new NotEnoughPeerException();
			// if isn't enough, return all
			return peerList.values();
		}

		return peerList.getRandomPeers(num).values();
	}

	/**
	 * Refill the peer list to obtain at least REQ_NPEER peers
	 * 
	 * @throws NoBootstrapConfiguredException
	 */
	public void refillPeerList() throws NoBootstrapConfiguredException {
		// compute the number of peers to request
		int num_peer = Integer.parseInt(configFile.getProperty(REQ_NPEER,
				REQ_NPEER_DEFAULT)) - peerList.size();
		
		// if the peer list isn't full
		if (num_peer > 0) {
			// refill msg
			RefillPeerListMessage rplm = new RefillPeerListMessage(peerDescriptor, num_peer);
			// send join message
			send(getBootstrapPeer(), rplm);
		}
		// TODO if I have more peers than required? (num_peer < 0)
	}

	public PeerListManager getPeerList() {
		return peerList;
	}
	
	
}
