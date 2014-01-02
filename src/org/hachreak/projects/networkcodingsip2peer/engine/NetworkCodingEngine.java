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
import java.util.ArrayList;
import java.util.List;

import org.hachreak.projects.gfjama.matrix.GFMatrix;
import org.hachreak.projects.gfjama.matrix.GFMatrixException;
import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.hachreak.projects.gfjama.matrix.RandomGFMatrix;
import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragment;
import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragmentHeader;
import org.hachreak.projects.networkcodingsip2peer.resource.MediaResource;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class NetworkCodingEngine implements CodingEngine {

	/**
	 * Chosen according to the rate of redundancy n / m that you want to have.
	 * The higher this value, the more reliable the recovery, but it is the most
	 * inefficient code. So there is a tradeoff. For example, in our various
	 * jobs we have always considered as 2:05 rates.
	 */
	private float redundancyRate = 5 / 2;

	/**
	 * n - number of fragments in output = m * redundancyRate
	 */
	protected int outputNumOfFragments;

	/**
	 * codingMatrix
	 */
	protected GFMatrix G;

	/**
	 * 
	 * @param gf
	 *            Galois Field
	 * @param redundancyRate
	 *            redundancy rate (n/m -> number of fragments encoded / number
	 *            of fragments of resource in input
	 */
	public NetworkCodingEngine(float redundancyRate) {
		setRedundancyRate(redundancyRate);
	}

	/**
	 * 
	 * @param gf
	 */
//	public NetworkCodingEngine() {
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hachreak.projects.networkcodingsip2peer.engine.CodingEngine#encode
	 * (org.hachreak.projects.networkcodingsip2peer.resource.MediaResource)
	 */
	public List<EncodedFragment> encode(MediaResource mediaResource)
			throws NoSuchAlgorithmException, IOException {
		// generate coefficient of matrix G
		generateGaloisMatrix(mediaResource);

		// set a resource key
		byte[] resKey = mediaResource.getResourceKey();
		// number of fragments in output
		int n = G.getColumnDimension();
		// number of fragments in input
//		int m = G.getRowDimension();

		// create fragments encoded
		ArrayList<EncodedFragment> result = new ArrayList<EncodedFragment>();
		for (int i = 0; i < mediaResource.getFragmentSize(); i++) {
			// get l(1xm) matrix
			GFMatrix l = mediaResource.loadTransposedPiece(i);
			// s(1xn) = l(1xm)*G(mxn)
			GFMatrix s = l.times(G);

			for (int j = 0; j < n; j++) {
				EncodedFragment f = null;
				try {
					f = result.get(j);
				} catch (IndexOutOfBoundsException e) {
					// create Fragment Header
					EncodedFragmentHeader fh = new EncodedFragmentHeader(
							j/*EncodedFragmentHeader.generateKey()*/, resKey,
							G.getColumnCopy(j), mediaResource.getFragmentSize(),
							mediaResource.getGaloisField());
					// create a new fragment
					f = new EncodedFragment(fh);
				}

				// add piece of encoded version of resource
				f.getBuffer()[i] = s.get(0, j);

				// save
				try {
					// update
					result.set(j, f);
				} catch (IndexOutOfBoundsException e) {
					// if not exist, add value
					result.add(f);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hachreak.projects.networkcodingsip2peer.engine.CodingEngine#decode
	 * (java.util.List)
	 */
	public char[][] decode(List<EncodedFragment> fragments)
			throws GFMatrixException {
		int m = fragments.get(0).getHeader().getCodingVector().length;//fragments.size();
		GaloisField gf = fragments.get(0).getHeader().getGaloisField();

		// G(mxm)
		GFMatrix G = new GFMatrix(m, m, gf);
		// for each fragment of 'm' available
		for (int j = 0; j < m; j++) {
			EncodedFragment f = fragments.get(j);
			// fill G matrix
			G.getArray()[j] = f.getHeader().getCodingVector();
		}
		// traspose G
		G = G.transpose();
		// Perform: G inversion (G^-1)
		GFMatrix G_1 = G.inverse();

		char[][] result = new char[fragments.get(0).getSize()][m];

		// for each byte of fragment's buffer
		for (int i = 0; i < fragments.get(0).getSize(); i++) {
			// s(1xm)
			GFMatrix s = new GFMatrix(1, m, gf);

			// for each fragment of 'm' available
			for (int j = 0; j < m; j++) {
				EncodedFragment f = fragments.get(j);
				// fill s matrix
				s.getArray()[0][j] = f.getBuffer()[i];
			}
			// Perform reconstruction: I = s*G^-1
			GFMatrix I = s.times(G_1);
			// save decoded values
			result[i] = I.getArray()[0];
		}
		return new GFMatrix(result, gf)/*.transpose()*/.getArray();
	}

	public void setRedundancyRate(float rate) {
		this.redundancyRate = rate;
	}

	/**
	 * Get number of fragments in output
	 * 
	 * @param mediaResource
	 */
	public int getOutputNumOfFragments(MediaResource mediaResource) {
		return outputNumOfFragments = (Math.round(mediaResource.getNumberOfFragments()
				* redundancyRate));
	}

	/**
	 * Generate the Galois Matrix (coefficient useful for linear combinations)
	 * 
	 * @param mediaResource
	 */
	protected void generateGaloisMatrix(MediaResource mediaResource) {
		// set n (lenght of a line in GF matrix)
		
		// init matrix with random values
		G = new RandomGFMatrix(mediaResource.getNumberOfFragments(), getOutputNumOfFragments(mediaResource),
				mediaResource.getGaloisField());
	}
}
