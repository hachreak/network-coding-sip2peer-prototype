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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.hachreak.projects.networkcodingsip2peer.engine.CodingEngine;
import org.hachreak.projects.networkcodingsip2peer.engine.NetworkCodingEngine;
import org.hachreak.projects.networkcodingsip2peer.msg.EncodedFragmentPublishMessage;
import org.hachreak.projects.networkcodingsip2peer.msg.EncodedFragmentResponseMessage;
import org.hachreak.projects.networkcodingsip2peer.peer.SimplePeer;
import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragment;
import org.hachreak.projects.networkcodingsip2peer.resource.MediaResource;
import org.hachreak.projects.networkcodingsip2peer.utils.EncapsulatedEncodedFragment;

public class PublishEncodedFileClientBehaviour extends Behaviour {

	public static final String GF_N = "GF_n";
	public static final String REDUNDANCY_RATE = "redundancyRate";
	public static final String FRAGMENT_SIZE = "fragmentSize";

	private Properties configFile = new java.util.Properties();

	/**
	 * GF(p^n) -> GF(2^n)
	 */
	private static byte GF_n = 16;

	private static GaloisField galoisField = new GaloisField(GF_n);

	private int fragmentSize = 10000;

	private float redundancyRate = 5 / 2;

//	private SimplePeer peer;
	private MediaResource mediaResource = null;
	private CodingEngine codingEngine;
	private List<EncodedFragment> fragments = new ArrayList<EncodedFragment>();
	private PeerListManager storePeerListManager;

	private List<EncodedFragment> fragmentsRetrieved = new ArrayList<EncodedFragment>();

	public PublishEncodedFileClientBehaviour(SimplePeer peer,
			PeerListManager storePeerListManager, File file) throws IOException {
		super(peer);
		initPeerConfig(peer);
		this.storePeerListManager = storePeerListManager;
		this.mediaResource = new MediaResource(file, fragmentSize, galoisField);
		codingEngine = new NetworkCodingEngine(redundancyRate);
	}

	private void initPeerConfig(SimplePeer peer) throws IOException {
		initFromConfigFile();
	}

	protected void publishFile() throws Exception {
		fragments = codingEngine.encode(mediaResource);

		if (codingEngine.getOutputNumOfFragments(mediaResource) > storePeerListManager
				.size())
			throw new Exception("number of fragment > peer list size");

		Iterator<Entry<String, NeighborPeerDescriptor>> it = storePeerListManager
				.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			NeighborPeerDescriptor pd = it.next().getValue();

			EncodedFragment f = fragments.get(i);

			getPeer().send(pd, new EncodedFragmentPublishMessage(f));

			i++;
			if (i >= fragments.size())
				break;
		}
	}

	public void onReceivedJSONMsg(String type, JSONObject jsonMsg) {
		try {
			JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject(
					"params");

			if (type.equals(EncodedFragmentResponseMessage.MSG_FRAGMENT_RESPONSE)) {

				// returned a fragment required
				fragmentsRetrieved.add(EncapsulatedEncodedFragment
						.decodeJSONFragment(params));

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * On run: start publishing of file
	 */
	public void run() {
		try {
			publishFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Load configuration file
	 * 
	 * @throws IOException
	 */
	private void initFromConfigFile() throws IOException {
		InputStream i = new FileInputStream(getPeer().getPathConfig());

		// peer configuration
		configFile.load(i);

		if (configFile.containsKey(GF_N)) {
			GF_n = Byte.parseByte(configFile.getProperty(GF_N));

			galoisField = new GaloisField(GF_n);
		}

		if (configFile.containsKey(FRAGMENT_SIZE))
			fragmentSize = Integer.parseInt(configFile
					.getProperty(FRAGMENT_SIZE));

		if (configFile.containsKey(REDUNDANCY_RATE))
			redundancyRate = Float.parseFloat(configFile
					.getProperty(REDUNDANCY_RATE));

	}

	public CodingEngine getCodingEngine() {
		return codingEngine;
	}

	public MediaResource getMediaResource() {
		return mediaResource;
	}

}
