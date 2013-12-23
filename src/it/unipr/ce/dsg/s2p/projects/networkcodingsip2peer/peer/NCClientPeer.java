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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;
import it.unipr.ce.dsg.s2p.peer.NeighborPeerDescriptor;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.exceptions.InvalidParamsException;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.exceptions.NoBootstrapConfiguredException;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.exceptions.NotEnoughPeerException;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.msg.FragmentPublishMessage;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.EncodedFragment;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.EncodedFragmentHeader;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.GenerationSize;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.MediaResourceOLD;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tmp.GFMatrix;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tmp.GaloisField;
import it.unipr.ce.dsg.s2p.sip.Address;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class NCClientPeer extends SimplePeer {

	private Properties configFile = new java.util.Properties();

	public static boolean networkCoding = false;
//	public static byte h = GenerationSize.LITTLE.getValue();
//	public static float k_as_n_over_m = 5.17f;

//	public static byte redundancyFactor = 20;
//	public static byte redundancyFactorAfterLookup = 0;

	/**
	 *  GF(p^n) -> GF(2^n)
	 */
	public static byte GF_n = 8;
	
	protected static GaloisField field = null;

	public static final String GF_N = "GF_n";
	public static final String CLIENT_NODE = "NCClientPeer";
//	public static final String THRESHOLD_FRAGMENTS = "thresholdFragments";
//	private static final String MAINTENANCE_IF_SUCCESS = "maintenanceIfSuccess";
//	private static final String NUMBER_OF_RESOURCES = "numberOfResources";
//	private static final String MAX_RESOURCE_SIZE = "maxResourceSize";
	private static final String NETWORK_CODING = "networkCoding";
//	private static final String GENERATION_SIZE = "generationSize";
//	private static final String P_POWER = "pPower";
//	private static final String K = "k";
//	private static final String REDUNDANCY_FACTOR = "redundancyFactor";
//	private static final String REDUNDANCY_FACTOR_AFTER_LOOKUP = "redundancyFactorAfterLookup";

	/**
	 * @param pathConfig
	 * @param key
	 * @throws IOException
	 * @throws InvalidParamsException
	 * @throws NoBootstrapConfiguredException 
	 */
	public NCClientPeer(String pathConfig, String key) throws IOException,
			InvalidParamsException, NoBootstrapConfiguredException {
		super(pathConfig, key);

		init(pathConfig);
	}

	private void init(String pathConfig) throws IOException,
			InvalidParamsException {
		// System.out.println(System.getProperty("user.dir"));
		InputStream i = new FileInputStream(pathConfig);// this.getClass().getClassLoader().

		// peer configuration
		configFile.load(i);

//		if (configFile.containsKey(GENERATION_SIZE)) {
//			try {
//				NCClientPeer.h = (byte) Float.parseFloat(configFile
//						.getProperty(GENERATION_SIZE));
//			} catch (NumberFormatException ex) {
//				throw new InvalidParamsException(GENERATION_SIZE
//						+ " must be a valid int value.");
//			}
//			if ((NCClientPeer.h != GenerationSize.LITTLE.getValue())
//					&& (NCClientPeer.h != GenerationSize.BIG.getValue())
//					&& (NCClientPeer.h != GenerationSize.TEST.getValue()))
//				throw new InvalidParamsException(
//						"Il parametro generationSize puÃ² assumere "
//								+ "uno tra i valori {50,100}");
//		}

		if (configFile.containsKey(NETWORK_CODING))
			NCClientPeer.networkCoding = Boolean.parseBoolean(configFile
					.getProperty(NETWORK_CODING));

		if (configFile.containsKey(GF_N))
			NCClientPeer.GF_n = Byte.parseByte(configFile
					.getProperty(GF_N));
		
//		if (configFile.containsKey(P_POWER)) {
//			try {
//				NCClientPeer.n_as_primePower = (byte) Float
//						.parseFloat(configFile.getProperty(P_POWER));
//			} catch (NumberFormatException ex) {
//				throw new InvalidParamsException(P_POWER
//						+ " must be a valid int value.");
//			}
//		}

//		if (configFile.containsKey(K)) {
//			try {
//				NCClientPeer.k_as_n_over_m = Float.parseFloat(configFile
//						.getProperty(K));
//				// System.out.println("k = " + NCClientPeer.k_as_n_over_m);
//			} catch (NumberFormatException ex) {
//				throw new InvalidParamsException(K
//						+ " must be a valid int value.");
//			}
//		}

//		if (configFile.containsKey(REDUNDANCY_FACTOR)) {
//			try {
//				NCClientPeer.redundancyFactor = (byte) Float
//						.parseFloat(configFile.getProperty(REDUNDANCY_FACTOR));
//			} catch (NumberFormatException ex) {
//				throw new InvalidParamsException(REDUNDANCY_FACTOR
//						+ " must be a valid int value.");
//			}
//		}

//		if (configFile.containsKey(REDUNDANCY_FACTOR_AFTER_LOOKUP)) {
//			try {
//				NCClientPeer.redundancyFactorAfterLookup = (byte) Float
//						.parseFloat(configFile
//								.getProperty(REDUNDANCY_FACTOR_AFTER_LOOKUP));
//			} catch (NumberFormatException ex) {
//				throw new InvalidParamsException(REDUNDANCY_FACTOR_AFTER_LOOKUP
//						+ " must be a valid int value.");
//			}
//		}

		NCClientPeer.field = new GaloisField(GF_n);
	}

//	/**
//	 * <p>
//	 * This method is used to generate a set of fragments for a Resource.
//	 * </p>
//	 * 
//	 * @author Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
//	 */
//	public ArrayList<Fragment> generateFragments(MediaResource wr) {
//
//		int resKey = wr.getResourceKey();
//
//		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
//
//		wr.setGenerationsAmount(h);
//		byte generationsAmount = wr.getGenerationsAmount();
//		// System.out.println("generationsAmount = " + generationsAmount);
//		double fragmentSize = (double) (wr.getSize())
//				/ (double) (generationsAmount * h);
//		// System.out.println("fragmentSize = " + fragmentSize);
//		// for the generation of redundant fragments
//		char fragmentsAmount = (char) Math
//				.round(((float) h * (k_as_n_over_m - 1.0f)));
//		// System.out.println("fragmentsAmount = " + fragmentsAmount);
//
//		for (byte i = 0; i < generationsAmount; ++i) {
//			GFMatrix codingMatrix = new GFMatrix(h, field);
//			codingMatrix.initializeMatrix(h, h);
//
//			// generating h independent fragments into generation
//			for (char j = 0; j < h; ++j) {
//				codingMatrix.get(j)[j] = GaloisField.getOne();
//				Fragment fragment = new Fragment(new FragmentHeader(
//						FragmentHeader.generateKey(), resKey,
//						codingMatrix.get(j), i), fragmentSize);
//				fragments.add(fragment);
//			}
//
//			/*
//			 * if (networkCoding) { //System.out.println("NC"); fragmentsAmount
//			 * = (char)(h * redundancyFactor / 100); // inutile usare
//			 * redundancyFactor se ho gi k } else { //System.out.println("EC");
//			 * fragmentsAmount = (char)Math.round(((float)h * (k_as_n_over_m -
//			 * 1.0f))); }
//			 */
//
//			codingMatrix = this.generateRedundantFragments(fragmentsAmount);
//
//			// addition of remaining fragments to the Arraylist
//			for (char j = 0; j < fragmentsAmount; ++j) {
//				Fragment fragment = new Fragment(new FragmentHeader(
//						FragmentHeader.generateKey(), resKey,
//						codingMatrix.get(j), i), fragmentSize);
//				fragments.add(fragment);
//			}
//		}
//		return fragments;
//	}
//
//	/**
//	 * This method generates a number of redundant fragments passed as argument.
//	 * 
//	 * @param fragmentsAmount
//	 * @return a GFMatrix of codingVectors
//	 */
//	private GFMatrix generateRedundantFragments(char fragmentsAmount) {
//		GFMatrix codingMatrix = new GFMatrix(h, field);
//
//		for (char j = 0; j < fragmentsAmount; ++j) {
//			char codingVector[] = new char[h];
//			// TODO convert!!!
//			// for(byte k = 0; k < h; ++k)
//			// codingVector[k] =
//			// (char)Engine.getDefault().getSimulationRandom().nextInt(
//			// (int) Math.pow(2, n_as_primePower));
//			codingMatrix.add(codingVector);
//		}
//		/*
//		 * In EC, the fragments generated are linear dependent because they are
//		 * more than h so, it's not necessary check their linear independence
//		 * ERRORE! Con EC devo controllare che i frammenti extra generati siano
//		 * indip.
//		 */
//		// if(!networkCoding) // originale di RB
//		if (networkCoding)
//			return codingMatrix;
//		else {
//			if (fragmentsAmount <= h) {
//				if (codingMatrix.size() > 1) {
//					// verification of linear independence of redundant
//					// fragments
//					ArrayList<Byte> indexes = new ArrayList<Byte>();
//					boolean linearIndependance = false;
//
//					do {
//						indexes = codingMatrix.gaussAlgorithm();
//						if (indexes.size() != 0) {
//							// codingMatrix.printMatrix(indexes, 0);
//							codingMatrix.removeAll(indexes);
//
//							for (byte k = 0; k < indexes.size(); ++k) {
//								char codingVector[] = new char[h];
//								// TODO convert!!!
//								// for(byte j = 0; j < h; ++j)
//								// codingVector[j] = (char) Engine.getDefault()
//								// .getSimulationRandom().nextInt(
//								// (int) Math.pow(2, n_as_primePower));
//								codingMatrix.add(codingVector);
//							}
//						} else
//							linearIndependance = true;
//					} while (!linearIndependance);
//				}
//			}
//			return codingMatrix;
//		}
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.peer.SimplePeer#
	 * onReceivedJSONMsg(it.unipr.ce.dsg.s2p.org.json.JSONObject,
	 * it.unipr.ce.dsg.s2p.sip.Address)
	 */
	@Override
	protected void onReceivedJSONMsg(JSONObject jsonMsg, Address sender) {

		try {// get params from the payload
			JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject(
					"params");
			// get type of message
			String type = (String) jsonMsg.get("type");
			
			if(type.equals(FragmentPublishMessage.MSG_FRAGMENT_PUBLISH)){
				System.out.println("[Fragment Request] from "+sender+" to "+this.getAddress());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		super.onReceivedJSONMsg(jsonMsg, sender);
	}

	/**
	 * Publish a file resource in the p2p network
	 * 
	 * @param file
	 * @throws NotEnoughPeerException 
	 */
	public void publishFile(File file) throws NotEnoughPeerException{
		System.out.println("[Send Fragments]");
		List<EncodedFragment> l = new ArrayList<EncodedFragment>();
		
		for(int i=0;i<2;i++){
			char buffer[] = new char[1];
			l.add(new EncodedFragment(null, buffer));
		}

		Iterator<EncodedFragment> i = l.iterator();
		//System.out.println(this.getAddress());
		// TODO controlla che il numero di peer disponibili sia uguale al numero di frammenti!
		Iterator<NeighborPeerDescriptor> p = getRandomPeers(l.size()).iterator();
		//System.out.println("[Try to Send Fragment] to "+peerList.size()+" peers");
		while(i.hasNext()){
			//System.out.println("[Send Fragment] .... ");
			// get a fragment
			EncodedFragment f = i.next();
			NeighborPeerDescriptor n = p.next();
			
			System.out.println("[Send Fragment] to "+n.getName());
			// publish the fragment
			send(n, new FragmentPublishMessage(f));
		}
		
	}
}
