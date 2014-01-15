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

package org.hachreak.projects.networkcodingsip2peer.utils;

import it.unipr.ce.dsg.s2p.org.json.JSONArray;
import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;

import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragment;
import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragmentHeader;

/**
 * The send() of a Peer alter char vectors. 
 * So, this class contains the necessary to encapsulate/decapsulate an EncodedFragment. 
 */
public class EncapsulatedEncodedFragment {

	/**
	 * Encaplusate a Encoded Fragment to send as a Message
	 * 
	 * @param fragment original fragment
	 * @return ecapsulated fragment
	 */
	public static EncodedFragment encapsulate(EncodedFragment fragment) {
		return new EncodedFragment(
				EncapsulatedEncodedFragment.encapsulate(fragment.getHeader()),
				EncapsulatedEncodedFragment.encodeCharVector(fragment.getBuffer()));
	}


	
	/**
	 * Encaplusate a Encoded Fragment as original
	 * 
	 * @param fragment encapsulated fragment
	 * @return orignal fragment
	 */
	public static EncodedFragmentHeader encapsulate(EncodedFragmentHeader header) {
		return new EncodedFragmentHeader(
				header.getResourceKey(),
				EncapsulatedEncodedFragment.encodeCharVector(header
						.getCodingVector()), header.getFragmentSize(),
				header.getGaloisField(), header.getFragmentKey());
	}
	
	/**
	 * Decaplusate a Encoded Fragment as original
	 * 
	 * @param fragment encapsulated fragment
	 * @return orignal fragment
	 */
	public static EncodedFragment decapsulate(EncodedFragment fragment) {
		return new EncodedFragment(
				EncapsulatedEncodedFragment.decapsulate(fragment.getHeader()),
				EncapsulatedEncodedFragment.decodeCharVector(fragment.getBuffer()));
	}
	
	/**
	 * Decapsulate a header
	 * 
	 * @param header
	 * @return
	 */
	public static EncodedFragmentHeader decapsulate(EncodedFragmentHeader header){
		return new EncodedFragmentHeader(
				header.getResourceKey(),
				EncapsulatedEncodedFragment.decodeCharVector(header
						.getCodingVector()), header.getFragmentSize(),
						header.getGaloisField(), header.getFragmentKey());
	}

	public static EncodedFragmentHeader decodeJSONHeader(JSONObject headerJSONArray) throws JSONException{
				// fragment key
				JSONArray fragmentKeyJSONArray = (JSONArray) headerJSONArray
						.get("fragmentKey");
				byte[] fragmentKey = EncapsulatedEncodedFragment.decodeJSONArray2byte(fragmentKeyJSONArray);
				
				// fragment size
				int fragmentSize = headerJSONArray.getInt("fragmentSize");

				// coding vector
				JSONArray codingVectorJSONArray = (JSONArray) headerJSONArray
						.get("codingVector");
				char[] codingVector = new char[codingVectorJSONArray.length()];
				for (int i = 0; i < codingVector.length; i++) {
					codingVector[i] = (Character) codingVectorJSONArray.get(i)
							.toString().charAt(0);
				}

				codingVector = EncapsulatedEncodedFragment.decodeCharVector(codingVector);
				
				// resource key
				JSONArray resourceKeyJSONArray = (JSONArray) headerJSONArray
						.get("resourceKey");
				byte[] resourceKey = EncapsulatedEncodedFragment.decodeJSONArray2byte(resourceKeyJSONArray);
				
				// galois field
				GaloisField galoisField = new GaloisField((byte) headerJSONArray
						.getJSONObject("galoisField").getInt("n"));

				// get original header
				return new EncodedFragmentHeader(resourceKey, codingVector, fragmentSize, galoisField, fragmentKey);

	}
	
	public static byte[] decodeJSONArray2byte(JSONArray buffer) throws JSONException{
		byte[] output = new byte[buffer.length()];
		for (int i = 0; i < output.length; i++) {
			output[i] = Byte.parseByte(buffer.getString(i));					
		}
		return output;
	}
	
	/**
	 * Decode a json object to return an EncodedFragment
	 * 
	 * @param params
	 * @return
	 * @throws JSONException
	 */
	public static EncodedFragment decodeJSONFragment(JSONObject params)
			throws JSONException {
//		int q = 0;
		// reconstruct [ header ]
		JSONObject headerJSONArray = (JSONObject) params.get("header");

		EncodedFragmentHeader header = EncapsulatedEncodedFragment.decodeJSONHeader(headerJSONArray);

		// reconstruct [ buffer ]
		JSONArray bufferJSONArray = (JSONArray) params.get("buffer");
		char[] buffer = new char[bufferJSONArray.length()];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = bufferJSONArray.get(i).toString().charAt(0);
		}
		
		buffer = EncapsulatedEncodedFragment.decodeCharVector(buffer);
		
		// get original fragment
		return new EncodedFragment(header, buffer);
	}

	
	/**
	 * Encode a char vector
	 * 
	 * @param inputBuffer
	 * @return
	 */
	public static char[] encodeCharVector(char[] inputBuffer) {
		byte[] buffer = new byte[inputBuffer.length * 2];
		for (int k = 0; k < inputBuffer.length; k++) {
			// if(k%2 == 0)
			buffer[2 * k] = (byte) (inputBuffer[k] & 255);
			// else
			buffer[2 * k + 1] = (byte) ((inputBuffer[k] >> 8) & 255);
		}
		return new String(
				org.zoolu.tools.Base64.encode(buffer))
//				org.apache.commons.codec.binary.Base64.encodeBase64(buffer))
				.toCharArray();
	}

	/**
	 * Decode a char vector
	 * 
	 * @param encodedBuffer
	 * @return
	 */
	public static char[] decodeCharVector(char[] encodedBuffer) {
		byte[] decoded = org.zoolu.tools.Base64.decode(new String(encodedBuffer));
//				org.apache.commons.codec.binary.Base64.decodeBase64(new String(encodedBuffer));
		char[] buffer = new char[decoded.length / 2];
		// char[] ib = fragment.getBuffer();
		for (int k = 0; k < buffer.length; k++) {
			buffer[k] = (char) ((decoded[2 * k] & 255) | ((decoded[2 * k + 1] & 255) << 8));
		}
		return buffer;
	}
}
