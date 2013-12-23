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

package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.engine;

import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.EncodedFragment;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.EncodedFragmentHeader;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.OriginalMediaResource;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.OriginalMediaResourcePiece;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.hachreak.projects.gfjama.matrix.GFMatrix;
import org.hachreak.projects.gfjama.matrix.GFMatrixException;
import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.hachreak.projects.gfjama.matrix.RandomGFMatrix;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class NetworkCodingEngine implements CodingEngine {

	protected GaloisField gf;

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
	protected byte outputNumOfFragments;

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
	public NetworkCodingEngine(GaloisField gf, float redundancyRate) {

		this.gf = gf;
		setRedundancyRate(redundancyRate);

	}

	/**
	 * 
	 * @param gf
	 */
	public NetworkCodingEngine(GaloisField gf) {
		this.gf = gf;
	}

	/*
	 * 
	 * @see
	 * it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.engine.CodingEngine
	 * #encode
	 * (it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.MediaResource
	 * )
	 */
	public List<EncodedFragment> encode(OriginalMediaResource ms) throws NoSuchAlgorithmException, IOException {
		// generate coefficient of matrix G
		generateGaloisMatrix(ms);
//G.print();
		int resKey = ms.getResourceKey();
		// number of fragments in output
		int n = G.getColumnDimension();
		// number of fragments in input
		int m = G.getRowDimension();

		// execute matrix product: s = l*G
		// char f[][] = new char[1][ms.getNumberOfFragments()];
		// f[0] = ms.getFragments();
		// GFMatrix l = new GFMatrix(f, new GaloisField((byte) 8));

		// create fragments encoded
		ArrayList<EncodedFragment> result = new ArrayList<EncodedFragment>();
		for (int i = 0; i < ms.getFragmentSize(); i++) {
			// get l(1xm) matrix
			GFMatrix l = ms.getPiece(i);
			// s(1xn) = l(1xm)*G(mxn)
			GFMatrix s = l.times(G);

			for (int j = 0; j < n; j++) {
				EncodedFragment f = null;
				try {
					f = result.get(j);
				} catch (IndexOutOfBoundsException e) {
					// create Fragment Header
					EncodedFragmentHeader fh = new EncodedFragmentHeader(
							EncodedFragmentHeader.generateKey(), resKey,
							G.getColumnCopy(j), ms.getFragmentSize(), ms.getGaloisField());
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
		
//			// get GF column values
//			char col[] = G.getColumnCopy(i);
//
//			// TODO quale chiave (primo parametro) ha bisogno????
//			FragmentHeader fh = new FragmentHeader(
//					FragmentHeader.generateKey(), resKey, col);
//			char e[] = new char[1];
//			// ogni frammento e' formato da un solo char
//			e[0] = s.get(0, i);
//			Fragment fr = new Fragment(fh, e);
//			result.add(fr);
		

		return result;
	}

	// protected char[] encodeFragment(ArrayList< char[] > fragments,
	// FragmentHeader header) {
	//
	// char gfVector[] = header.getCodingVector();
	// char fragmentEncoded[] = new char[fragments.get(0).length];
	//
	// for (int j = 0; j < fragments.size(); j++) {
	// char[] a_j = fragments.get(j);
	// fragmentEncoded = sumEncoded(fragmentEncoded, muxAjGFvector(gfVector[j],
	// a_j));
	// }
	//
	// return fragmentEncoded;
	// }

	// protected char[] sumEncoded(char[] base, char[] muxAiGFvector) {
	// char result[] = new char[muxAiGFvector.length];
	// for (int i = 0; i < muxAiGFvector.length; i++) {
	// result[i] = gf.sum(base[i], muxAiGFvector[i]);
	// }
	// return result;
	// }
	//
	// protected char[] muxAjGFvector(char gfVector,
	// char[] a_j) {
	// char result[] = new char[a_j.length];
	// for (int i = 0; i < a_j.length; i++) {
	// result[i] = gf.product(gfVector, a_j[i]);
	// }
	// return result;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.engine.CodingEngine
	 * #decode(java.util.List)
	 */
	public List<GFMatrix> decode(List<EncodedFragment> fragments) throws GFMatrixException {
		int m = fragments.size();
		GaloisField gf = fragments.get(0).getHeader().getGaloisField();
		List<GFMatrix> result = new ArrayList<GFMatrix>();
		
		// for each byte of fragment's buffer
		for(int i=0; i<fragments.get(0).getSize(); i++){
			// s(1xm)
			GFMatrix s = new GFMatrix(1, m, gf);
			// G(mxm)
			GFMatrix G = new GFMatrix(m, m, gf);
			// for each fragment of 'm' available
			for(int j=0; j<m; j++){
				EncodedFragment f = fragments.get(j);
				// fill s matrix
				s.getArray()[0][j] = f.getBuffer()[i];
				// fill G matrix
				G.getArray()[j] = f.getHeader().getCodingVector();
			}
			// traspose G
			G = G.transpose();
			// Perform: G inversion (G^-1)
			GFMatrix G_1 = G.inverse();
			// Perform reconstruction: I = s*G^-1
			GFMatrix I = s.times(G_1);
			I.printChar();
			result.add(I);
//			System.out.println(I.transpose().getColumnDimension());
//			System.out.println(I.transpose().getRowDimension());
//			System.out.println(I.getColumnDimension());
//			System.out.println(I.getRowDimension());
		}
		return result;
	}

	public void setRedundancyRate(float rate) {
		this.redundancyRate = rate;
	}

	/**
	 * Set number of fragments in output TODO test it!
	 * 
	 * @param ms
	 */
	protected void setOutputNumOfFragments(OriginalMediaResource ms) {
		outputNumOfFragments = (byte) (Math.round(ms.getNumberOfFragments()
				* redundancyRate) + 1);
	}

	/**
	 * Generate the Galois Matrix (coefficient useful for linear combinations)
	 * 
	 * @param ms
	 */
	protected void generateGaloisMatrix(OriginalMediaResource ms) {
		// set n (lenght of a line in GF matrix)
		setOutputNumOfFragments(ms);
		// init matrix with random values
		G = new RandomGFMatrix(ms.getNumberOfFragments(), outputNumOfFragments,
				gf);
	}

	public GaloisField getGaloisField() {
		return gf;
	}

}
