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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JInternalFrame;

import org.hachreak.projects.gfjama.matrix.GFMatrix;
import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.zoolu.tools.Random;

/**
 * This class represent a generic resource to encode
 * 
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 *
 */
public class MediaResource {

	private File file;
	private int fragmentSize;
	private GaloisField galoisField;
	private byte[] digest;

	/**
	 * 
	 * @param file File linked to the resource
	 * @param fragmentSize fragment size
	 * @param galoisField galois field
	 */
	public MediaResource(File file, int fragmentSize, GaloisField galoisField) {
		this.file = file;
		this.fragmentSize = fragmentSize;
		this.galoisField = galoisField;
	}

	public GaloisField getGaloisField() {
		return galoisField;
	}

	public File getFile() {
		return file;
	}

	public int getFragmentSize() {
		return fragmentSize;
	}

	/**
	 * Get number of fragments in which is divided the resource
	 * @return number of fragments
	 */
	public int getNumberOfFragments() {
		long totalLength = file.length();
		return Math.round( totalLength
		/ fragmentSize) + (totalLength % fragmentSize != 0 ? 1 : 0);
//		return Math.round(file.length() / fragmentSize);
	}

	/**
	 * Return a unique identifier of resource (digest SHA-256)
	 * 
	 * @return SHA-256 of resource
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public byte[] getResourceKey() throws NoSuchAlgorithmException, IOException {
		if (digest == null) {
			InputStream inputStream = new BufferedInputStream(
					new FileInputStream(file));

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte cbuf[] = new byte[fragmentSize];
			int offset = 0;
			byte[] dataBytes = new byte[fragmentSize];
			int nread = 0;
			while ((nread = inputStream.read(cbuf, offset, fragmentSize)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			digest = md.digest();
			inputStream.close();
		}

		// TODO da controllare!
		return digest;
	}

	/**
	 * Save the matrix as a sequence of char[]
	 * 
	 * @param ostream stream where to write
	 * @param data data to write
	 * @throws IOException
	 */
	public static void save(File file, char[][] data, long totalLength)
			throws IOException {
//		Writer w = new BufferedWriter(new OutputStreamWriter(ostream));
		RandomAccessFile w = new RandomAccessFile(file, "rw");
		
//		long delta = totalLength - data.length * (data[0].length - 1);
//		System.out.println("total length "+totalLength);		
//		System.out.println("prima "+(data.length * (data[0].length - 1)));
//		System.out.println("delta: "+delta);
		
		long j = 0;
		long k = 0;
//		int m = data.length;
		int n = data[0].length;
		for(long i=0; i<totalLength; i++){
			j = i % n;
			k = i / n;

//			if(data[(int) k][(int) j] > 255) System.out.println("errore");
//			System.out.println((int) k+" "+(int) j);
			w.write((int)data[(int) k][(int) j]);
		}
//		
//		for (int i = 0; i < (data.length - 1); i++) {
//			w.write(data[i]);
//		}
//		
//		if(delta == 0){
//			w.write(data[data.length - 1]);
//		}else{
//			System.out.println("delta: "+delta+ "size "+data[data.length - 2].length);
////			for(int i=0; i<(int)delta; i++){
////				System.out.println(i);//+" - "+data[data.length - 1][i]);
//////				w.write(data[data.length - 1][i]);
////			}
//		}
	
//		w.flush();
		w.close();
	}

	/**
	 * Load a piece of resource:
	 *   for each piece a_i it get the "index"th byte
	 *   e.g: [a_i0, a_i1, a_i2, ... , a_ij]
	 *   
	 * @param index identify each "index"th byte of a fragment 
	 * @return Matrix 1xm
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public GFMatrix loadTransposedPiece(int index) throws FileNotFoundException, IOException{
		assert index >= fragmentSize : "index >= fragment size";
		
		int n = getNumberOfFragments(); 
		
		RandomAccessFile reader = new RandomAccessFile(file, "r");

		// create GFMatrix object
		GFMatrix matrix = new GFMatrix(1, n, galoisField);

		// get internal matrix
		char A[][] = matrix.getArray();

		// load vector of original data
//		char cbuf[] = new char[fragmentSize];
//		int offset = 0, jndex = 0;
		// List<Character> vect = new ArrayList<Character>();
//System.out.println("seek "+index);		
		reader.seek(index * n);
		
		for (int i=0; i<n; i++){
			A[0][i] = (char)reader.read();
//			System.out.println((char)reader.read());
		}
//		matrix.printChar();
//		int ret = 0;
//		while ((ret = reader.read(cbuf, offset, fragmentSize)) != -1) {
//				if(jndex < A[0].length)
//					A[0][jndex] = cbuf[index];
//			jndex++;
//		}

		reader.close();
		return matrix;

		//		return MediaResource.loadTransposedPiece(new FileInputStream(file), getNumberOfFragments(), fragmentSize, index, galoisField);
	}
	
//	public static GFMatrix loadTransposedPiece(InputStream istream,
//			int numOfFragments, int fragmentSize, int index,
//			GaloisField galoisField) throws IOException {
//		assert index >= fragmentSize : "index >= fragment size";
//
//		// reader of file
//		BufferedReader reader = new BufferedReader(new InputStreamReader(istream));// new
//																			// FileInputStream(file)));
//		// create GFMatrix object
//		GFMatrix m = new GFMatrix(1, (int) Math.round(numOfFragments), galoisField);
//
//		// get internal matrix
//		char A[][] = m.getArray();
//
//		// load vector of original data
//		char cbuf[] = new char[fragmentSize];
//		int offset = 0, jndex = 0;
//		// List<Character> vect = new ArrayList<Character>();
//		int ret = 0;
//		while ((ret = reader.read(cbuf, offset, fragmentSize)) != -1) {
//				if(jndex < A[0].length)
//					A[0][jndex] = cbuf[index];
//			jndex++;
//		}
//
//		reader.close();
//		return m;
//	}
}
