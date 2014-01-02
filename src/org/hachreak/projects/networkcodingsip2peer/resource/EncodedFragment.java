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



/**
 * This class represents a fragment into the system. It stores a EncodedFragmentHeader object, 
 * and the buffer of data encoded
 * 
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 *
 */
public class EncodedFragment {
	
	private EncodedFragmentHeader header;
	
	private char[] buffer;
	

	public int getSize() {
		return buffer.length;
	}

	/**
	 * 
	 * @param header header informations
	 * @param buffer fragment object
	 */
	public EncodedFragment(EncodedFragmentHeader header, char[] buffer) {

		this.header = header;
		this.buffer = buffer;
	}
	
	public EncodedFragment(EncodedFragmentHeader header){
		this.header = header;
		this.buffer = new char[header.getFragmentSize()];
	}

	public EncodedFragmentHeader getHeader() {
		return header;
	}

	public void setHeader(EncodedFragmentHeader header) {
		this.header = header;
	}

	/**
	 * @return the buffer
	 */
	public char[] getBuffer() {
		return buffer;
	}
//public String getBuf(){return new String(buffer); }
	/**
	 * @param buffer the buffer to set
	 */
	public void setBuffer(char[] buffer) {
		this.buffer = buffer;
	}
}
