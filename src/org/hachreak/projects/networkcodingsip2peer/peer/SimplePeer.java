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

package org.hachreak.projects.networkcodingsip2peer.peer;

import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;
import it.unipr.ce.dsg.s2p.peer.NeighborPeerDescriptor;
import it.unipr.ce.dsg.s2p.peer.Peer;
import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;
import it.unipr.ce.dsg.s2p.peer.PeerListManager;
import it.unipr.ce.dsg.s2p.sip.Address;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.hachreak.projects.networkcodingsip2peer.actionListener.FullFillPeerListener;
import org.hachreak.projects.networkcodingsip2peer.behavior.Behavior;
import org.hachreak.projects.networkcodingsip2peer.behavior.BootstrapClientBehavior;
import org.hachreak.projects.networkcodingsip2peer.exceptions.NoBootstrapConfiguredException;
import org.hachreak.projects.networkcodingsip2peer.exceptions.NotEnoughPeerException;
import org.hachreak.projects.networkcodingsip2peer.msg.RefillPeerListMessage;

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

	private NeighborPeerDescriptor bootstrapPeer = null;

	private Map<String, Behavior> behaviours = new HashMap<String, Behavior>();

	/**
	 * configuration's file
	 */
	protected Properties configFile = new java.util.Properties();
	private String pathConfig;

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
	public SimplePeer(String pathConfig, String key) throws IOException {
		super(pathConfig, key);

		// init peer
		init(pathConfig);
	}

	public SimplePeer(String pathConfig, String key, String peerName,
			int peerPort) throws IOException {
		super(pathConfig, key, peerName, peerPort);

		init(pathConfig);
	}

	private void init(String pathConfig) throws IOException {
		InputStream i = new FileInputStream(pathConfig);

		// load peer configuration
		configFile.load(i);

		// save pathConfig
		this.pathConfig = pathConfig;
	}

	public String getPathConfig() {
		return pathConfig;
	}

	/**
	 * Return the bootstrap address
	 * 
	 * @return
	 * @throws NoBootstrapConfiguredException
	 */
	public NeighborPeerDescriptor getBootstrapPeer()
			throws NoBootstrapConfiguredException {
		if (bootstrapPeer == null) {
			// get bootstrap address from configuration
			String bootstrapPeerName = configFile.getProperty(BOOTSTRAP_PEER);

			// if isn't set, raise an exception
			if (bootstrapPeerName == null) {
				throw new NoBootstrapConfiguredException();
			}

			String random = "bootstrap"+String.valueOf((int )(Math.random() * 10000 + 1));
			
			bootstrapPeer = new NeighborPeerDescriptor(new PeerDescriptor(
					random, bootstrapPeerName, random));
		}

		return bootstrapPeer;
	}

	/**
	 * Join the network
	 * 
	 * @throws NoBootstrapConfiguredException
	 */
	public void joinBootstrapPeer(FullFillPeerListener listener) throws NoBootstrapConfiguredException {
		int peerRequired = Integer.parseInt(configFile.getProperty(REQ_NPEER,
				REQ_NPEER_DEFAULT));
		System.out.println("peer richiesti "+peerRequired);
//		final String name = this.getPeerDescriptor().getName();
		BootstrapClientBehavior bcb = new BootstrapClientBehavior(this, peerRequired);
		bcb.addFullFillPeerListener(listener);
		
		Thread thread = new Thread(bcb);
		thread.run();
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
			// System.out.println("[SimplePeer] msg arrivato: "+jsonMsg.get("type"));

			// get params from the payload
			JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject(
					"params");
			// get type of message
			String type = (String) jsonMsg.get("type");

			// execute any associated behaviour
			execBehaviours(type, jsonMsg);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execute each beaviours
	 * 
	 * @param type
	 * @param jsonMsg
	 */
	private void execBehaviours(String type, JSONObject jsonMsg) {
		Iterator<Entry<String, Behavior>> i = behaviours.entrySet().iterator();
		while (i.hasNext()) {
			i.next().getValue().onReceivedJSONMsg(type, jsonMsg);
		}
	}

	public PeerListManager getPeerList() {
		return peerList;
	}

	public PeerDescriptor getPeerDescriptor() {
		return peerDescriptor;
	}

	public Map<String, Behavior> getBehaviours() {
		return behaviours;
	}

	/**
	 * Add a Neighbor Peer List
	 * 
	 * @param peerDescriptorList
	 */
	public void addNeighborPeerList(List<PeerDescriptor> peerDescriptorList) {
		Iterator<PeerDescriptor> i = peerDescriptorList.iterator();
		while (i.hasNext()) {
			addNeighborPeer(i.next());
		}
	}

	/**
	 * Add to a existing Peer List Manager a list of Peer
	 * 
	 * @param plm
	 *            Peer List where put each peer
	 * @param peerDescriptorList
	 *            source peer list
	 */
	public static void addNeighborPeerList(PeerListManager plm,
			List<PeerDescriptor> peerDescriptorList) {
		Iterator<PeerDescriptor> i = peerDescriptorList.iterator();
		while (i.hasNext()) {
			NeighborPeerDescriptor neighborPD = new NeighborPeerDescriptor(
					i.next());
			plm.put(neighborPD.getKey(), neighborPD);
		}
	}

	@Override
	protected void onDeliveryMsgFailure(String arg0, Address arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDeliveryMsgSuccess(String arg0, Address arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	/**
	 * Get a property
	 * 
	 * @param name
	 *            name of property
	 * @return property value
	 */
	public String getProperty(String name) {
		return configFile.getProperty(name);
	}

}
