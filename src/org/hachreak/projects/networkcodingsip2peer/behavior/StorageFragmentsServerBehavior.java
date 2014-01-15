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

import it.unipr.ce.dsg.s2p.org.json.JSONArray;
import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;
import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;

import java.io.IOException;

import org.hachreak.projects.networkcodingsip2peer.msg.EncodedFragmentPublishMessage;
import org.hachreak.projects.networkcodingsip2peer.msg.EncodedFragmentRequestMessage;
import org.hachreak.projects.networkcodingsip2peer.msg.EncodedFragmentResponseMessage;
import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;
import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragment;
import org.hachreak.projects.networkcodingsip2peer.resource.StorageFragments;
import org.hachreak.projects.networkcodingsip2peer.utils.EncapsulatedEncodedFragment;
import org.hachreak.projects.networkcodingsip2peer.utils.JSONObject2Peer;

public class StorageFragmentsServerBehavior extends Behavior {
//	private SimplePeer peer;
//	private List<EncodedFragment> fragments = new ArrayList<EncodedFragment>();
	
//	private List<EncodedFragmentReceivedListener> listeners = new ArrayList<EncodedFragmentReceivedListener>();

	private StorageFragments storage;
	
	public StorageFragmentsServerBehavior(SimplePeer peer, StorageFragments storage) throws IOException {
		super(peer);
		initPeerConfig(peer);
		this.storage = storage;
	}

	private void initPeerConfig(SimplePeer peer) {
//		this.peer = peer;
//		peer.getBehaviours().add(this);
	}

	public void run() {
	}

	public void onReceivedJSONMsg(String type, JSONObject jsonMsg) {
		String name = getPeer().getPeerDescriptor().getName();

		System.out.println("[ "+name+" ] receive msg type "+type);

		try {
			JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject(
					"params");

			if (type.equals(EncodedFragmentPublishMessage.MSG_FRAGMENT_PUBLISH)) {
				// get fragment from message received
				EncodedFragment fragment = EncapsulatedEncodedFragment
								.decodeJSONFragment(params);
//				System.out.println("[SERVER] resource published... ");

				// store the fragment
				storage.put(fragment);
			} else if (type
					.equals(EncodedFragmentRequestMessage.MSG_FRAGMENT_REQUEST)) {
				// get sender identity
				PeerDescriptor peerdesc = JSONObject2Peer
						.json2peerdescriptor(jsonMsg.getJSONObject("sender"));
				
				// get resource key of resource required
				JSONArray resourceKeyJSONArray = (JSONArray) jsonMsg
						.get("resourceKey");
				byte[] resourceKey = EncapsulatedEncodedFragment.decodeJSONArray2byte(resourceKeyJSONArray);
				
				// send the required fragment
				getPeer().send(
						getPeer().addNeighborPeer(peerdesc),
						new EncodedFragmentResponseMessage(
						storage.get(resourceKey)));

//				System.out.println("[SERVER] Send resource.. to "+peerdesc.getName());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public void addListener(EncodedFragmentReceivedListener listener) {
//		listeners.add(listener);
//	}
//
//	protected void fireEventEncodedFragmentReceived(EncodedFragment fragment) {
//		Iterator<EncodedFragmentReceivedListener> i = listeners.iterator();
//		while (i.hasNext())
//			i.next().action(fragment);
//	}

}
