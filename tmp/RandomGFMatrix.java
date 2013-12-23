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

package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tmp;

import java.util.Random;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 *
 */
public class RandomGFMatrix extends GFMatrix {

	//protected byte colSize;
	
	private Random randomizer;

	public RandomGFMatrix(byte m, byte n, GaloisField field) {
		super(n, field);
		//this.colSize = colSize;
//		Random generater = new Random((long) Math.random());
//		// init matrix[m][n]
//		matrix = new char[m][n];
		//System.out.println(Math.random() % 254);
		init(m, n, -1);
	}
	
	public RandomGFMatrix(byte m, byte n, GaloisField field, long seed) {
		super(n, field);
		//this.colSize = colSize;
		init(m, n, seed);
	}
	
	private void init(byte m, byte n, long seed){
		if(seed != -1)
			randomizer = new Random(seed);
		else
			randomizer = new Random();
		// init matrix[m][n]
		matrix = null;//new char[m][n];
		// fill matrix
		fillWithRandomValues(m, n);
	}

	/**
	 * Generate a random row
	 * TODO test!!!
	 * 
	 * @return row filled with random values 
	 */
	protected char[] generateRandomRow(byte colNum){
		char codingVector[] = new char[(int)colNum];
		//System.out.println((int)getColumnsNumber());
		for(char k = 0; k < colNum; ++k){
			// generate random value [0 - 254]
			codingVector[k] = (char)( randomizer.nextInt() % 254);
			//System.out.println("add: "+codingVector[k]+ " - "+randomizer.nextInt()%254);
		}
		return codingVector;
	}
	
	/**
	 * Fill the matrix with random values
	 * TODO Test!!!
	 */
	protected void fillWithRandomValues(byte rowNum, byte colNum){
		//System.out.println("rows: "+(int)getRowsNumber());
		// generate random values
		for(char i=0; i<rowNum; i++){
			//System.out.println("add "+(int)i+" - "+(int)getRowsNumber());
			
			add(generateRandomRow(colNum));
		}
	}
	
}
