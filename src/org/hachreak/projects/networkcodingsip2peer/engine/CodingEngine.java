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

package org.hachreak.projects.networkcodingsip2peer.engine;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.hachreak.projects.gfjama.matrix.GFMatrixException;
import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragment;
import org.hachreak.projects.networkcodingsip2peer.resource.MediaResource;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 *
 */
public interface CodingEngine {

	/**
	 * Encode resource in a list of fragments
	 * 
	 * @param ms resource to encode
	 * @return list of fragments encoded
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 */
	public List<EncodedFragment> encode(MediaResource ms) throws NoSuchAlgorithmException, IOException;
	
	/**
	 * Rebuild a resource encoded
	 * 
	 * @param fragments fragments encoded
	 * @return resource decoded
	 * @throws GFMatrixException 
	 */
	public char[][] decode(List<EncodedFragment> fragments) throws GFMatrixException;
	
	/**
	 * Set redundancy rate
	 * 
	 * @param rate
	 */
	public void setRedundancyRate(float rate);
}
