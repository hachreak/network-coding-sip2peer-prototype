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

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import org.hachreak.projects.networkcodingsip2peer.msg.EncodedFragmentRequestMessage;
import org.hachreak.projects.networkcodingsip2peer.msg.EncodedFragmentResponseMessage;
import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;
import org.hachreak.projects.networkcodingsip2peer.resource.StorageFragments;
import org.hachreak.projects.networkcodingsip2peer.utils.EncapsulatedEncodedFragment;

public class RetrieveEncodedFileClientBehavior extends Behavior {

	private PeerListManager storePeerListManager;
	private File file;
//	private SimplePeer peer;

	private Properties configFile = new java.util.Properties();

	public static final String FRAGMENT_SIZE = "fragmentSize";
//	private int fragmentSize = 10000;
	private StorageFragments storage;
	private byte[] resourceKey;
	private int numOfFragments;

	public RetrieveEncodedFileClientBehavior(SimplePeer peer,
			PeerListManager storePeerListManager, byte[] resourceKey, int numOfFragments, StorageFragments storage) throws IOException {
		super(peer);
		this.storePeerListManager = storePeerListManager;
		this.resourceKey = resourceKey;
		this.numOfFragments = numOfFragments;
//		this.peer = peer;
//		this.peer.getBehaviours().add(this);
		this.storage = storage;
		initFromConfigFile();
	}

	private void initFromConfigFile() throws IOException {
//		InputStream i = new FileInputStream(getPeer().getPathConfig());
//
//		// peer configuration
//		configFile.load(i);
//
////		if (configFile.containsKey(FRAGMENT_SIZE))
////			fragmentSize = Integer.parseInt(configFile
////					.getProperty(FRAGMENT_SIZE));

	}

	public void run() {
		try {
			retrieveEncodedFragments();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private void retrieveEncodedFragments() throws NoSuchAlgorithmException, IOException {
//		byte[] resourceKey = MediaResource.computeResourceKey(file);
		
		Iterator<Entry<String, NeighborPeerDescriptor>> it = storePeerListManager
				.entrySet().iterator();
		int i = 0;
//		int numOfFragments = MediaResource.computeNumberOfFragments(file, fragmentSize);
		while (it.hasNext()) {
			NeighborPeerDescriptor pd = it.next().getValue();
			
			// send request of fragment stored in the peer
			getPeer().send(pd,
					new EncodedFragmentRequestMessage(getPeer().getPeerDescriptor(),	resourceKey));
			i++;
			if (i >= numOfFragments)
				break;
		}
	}

	public void onReceivedJSONMsg(String type, JSONObject jsonMsg) {
//		String name = peer.getPeerDescriptor().getName();
//
//		 System.out.println("[ "+name+" ] RETRIEVER SERVER receive msg type "+type);

		try {
			JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject(
					"params");

			if (type.equals(EncodedFragmentResponseMessage.MSG_FRAGMENT_RESPONSE)) {
				// received a fragment
				storage.put(EncapsulatedEncodedFragment
								.decodeJSONFragment(params));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
