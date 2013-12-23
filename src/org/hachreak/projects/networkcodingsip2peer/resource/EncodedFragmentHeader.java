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

package org.hachreak.projects.networkcodingsip2peer.resource;

import org.hachreak.projects.gfjama.matrix.GaloisField;

/**
 * This class represent a fragment's header that contains info about the fragment's key,
 * the generation's tag, the resource's key and the codingVector.
 * 
 *
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 *
 */
public class EncodedFragmentHeader {
	
	private int fragmentKey;
	
//	private byte generationTag;
	
	private char[] codingVector;
	
	private byte[] resourceKey;

	private int fragmentSize;

	private GaloisField galoisField;
	
	public EncodedFragmentHeader(int fragmentKey, byte[] fileId, char[] codingVector, int fragmentSize, GaloisField galoisField){//, byte generationTag) {
		this.fragmentKey = fragmentKey;
		this.resourceKey = fileId;
		this.codingVector = codingVector;
		this.fragmentSize = fragmentSize;
		this.galoisField = galoisField;
	}

	public int getFragmentKey() {
		return fragmentKey;
	}

	public char[] getCodingVector() {
		return codingVector;
	}

	public byte[] getResourceKey() {
		return resourceKey;
	}

	/**
	 * TODO Temporary function for generation of key
	 * @return
	 */
	public static int generateKey(){
		return 1 + (int)(Math.random()*100000);
	}

	public int getFragmentSize() {
		return fragmentSize;
	}

	public GaloisField getGaloisField() {
		return galoisField;
	}
	
	
}
