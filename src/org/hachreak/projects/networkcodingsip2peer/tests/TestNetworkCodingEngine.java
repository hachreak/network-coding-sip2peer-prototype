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

package org.hachreak.projects.networkcodingsip2peer.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.hachreak.projects.gfjama.matrix.GFMatrixException;
import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.hachreak.projects.networkcodingsip2peer.engine.CodingEngine;
import org.hachreak.projects.networkcodingsip2peer.engine.NetworkCodingEngine;
import org.hachreak.projects.networkcodingsip2peer.resource.EncodedFragment;
import org.hachreak.projects.networkcodingsip2peer.resource.MediaResource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class TestNetworkCodingEngine {
	
	GaloisField galoisField = new GaloisField((byte) 16);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEncode() {
		File f = new File("tests/TestNetworkCodingEngine.txt.zip");
		int fragmentSize = 5;
		
		MediaResource o = new MediaResource(f, fragmentSize, galoisField);
		//MediaResource.loadTransposedPiece(new FileImageInputStream(f), f.length(), fragmentSize, index, galoisField);

		CodingEngine c = new NetworkCodingEngine();//galoisField);
		try {
			// encode
			List<EncodedFragment> fragments = c.encode(o);
			
			// get only m fragments
			List<EncodedFragment> fragmentsToDecode = new ArrayList<EncodedFragment>();
			for (int i = 1; i <= o.getNumberOfFragments(); i++) {
				fragmentsToDecode.add(fragments.get(i));
			}

			// decode
			char[][] data = c.decode(fragmentsToDecode);

			isEquals(data, f);	

//			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
//			int size = data[0].length;
//			//System.out.println(data.length);
//			for(int i=0; i<data.length; i++){
//				char[] orig = new char[size];
//				reader.read(orig);
//				assertTrue(new String(orig).equals(new String(data[i])));
////				System.out.println("TEST "+i);
//			}
			
			// save
			MediaResource.save(new FileOutputStream(new File(
					"tests/TestNetworkCodingEngine.decoded.txt.zip")), data, f.length()); 

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GFMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEncode2() {
		File f = new File("tests/TestNetworkCodingEngineNotMultiple.txt");
		int fragmentSize = 7001;
		
		MediaResource o = new MediaResource(f, fragmentSize, galoisField);
		//MediaResource.loadTransposedPiece(new FileImageInputStream(f), f.length(), fragmentSize, index, galoisField);

		CodingEngine c = new NetworkCodingEngine();//galoisField);
		try {
			// encode
			List<EncodedFragment> fragments = c.encode(o);
			
			// get only m fragments
			List<EncodedFragment> fragmentsToDecode = new ArrayList<EncodedFragment>();
			for (int i = 10; i < o.getNumberOfFragments() + 10; i++) {
				fragmentsToDecode.add(fragments.get(i));
			}

			// decode
			char[][] data = c.decode(fragmentsToDecode);
			
			isEquals(data, f);	

//			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
//			int size = data[0].length;
//			//System.out.println(data.length);
//			for(int i=0; i<data.length; i++){
//				char[] orig = new char[size];
//				reader.read(orig);
//				assertTrue(new String(orig).equals(new String(data[i])));
////				System.out.println("TEST "+i);
//			}
			
			// save
			MediaResource.save(new FileOutputStream(new File(
					"tests/TestNetworkCodingEngineNotMultiple.decoded.txt")), data, f.length()); 

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GFMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testEncode3() {
		File f = new File("tests/TestNetworkCodingEngine.txt");
		int fragmentSize = 5;
		
		MediaResource o = new MediaResource(f, fragmentSize, galoisField);
		//MediaResource.loadTransposedPiece(new FileImageInputStream(f), f.length(), fragmentSize, index, galoisField);

		CodingEngine c = new NetworkCodingEngine();//galoisField);
		try {
			// encode
			List<EncodedFragment> fragments = c.encode(o);
			
			// get only m fragments
			List<EncodedFragment> fragmentsToDecode = new ArrayList<EncodedFragment>();
			for (int i = 1; i <= o.getNumberOfFragments(); i++) {
				fragmentsToDecode.add(fragments.get(i));
			}

			// decode
			char[][] data = c.decode(fragmentsToDecode);
			
			// total file length
			long totalLength = f.length();
			
			isEquals(data, f);	
				//assertTrue(new String(orig).equals(new String(data[i])));
//				System.out.println("TEST "+i);
			
			
			// save
			MediaResource.save(new FileOutputStream(new File(
					"tests/TestNetworkCodingEngine.decoded.txt")), data, totalLength); 

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GFMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testEncode4() {
		File f = new File("tests/nodequeue-6.x-2.11.tar.gz");
		int fragmentSize = 45670;
		
		MediaResource o = new MediaResource(f, fragmentSize, galoisField);
		//MediaResource.loadTransposedPiece(new FileImageInputStream(f), f.length(), fragmentSize, index, galoisField);

		CodingEngine c = new NetworkCodingEngine();//galoisField);
		try {
			// encode
			List<EncodedFragment> fragments = c.encode(o);
			
			// get only m fragments
			List<EncodedFragment> fragmentsToDecode = new ArrayList<EncodedFragment>();
			for (int i = 1; i <= o.getNumberOfFragments(); i++) {
				fragmentsToDecode.add(fragments.get(i));
			}

			// decode
			char[][] data = c.decode(fragmentsToDecode);
			
			// total file length
			long totalLength = f.length();
			
			isEquals(data, f);	
				//assertTrue(new String(orig).equals(new String(data[i])));
//				System.out.println("TEST "+i);
			
			
			// save
			MediaResource.save(new FileOutputStream(new File(
					"tests/nodequeue-6.x-2.11.tar.decoded.gz")), data, totalLength); 

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GFMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void isEquals(char[][] data, File originalFile) throws IOException{
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(originalFile)));
		int size = data[0].length;
		//System.out.println(data.length);
//		long jndex = 0;
		for(int i=0; i<(data.length - 1); i++){
			char[] orig = new char[size];
			reader.read(orig);

			// System.out.println(new String(data[i]));
			// System.out.println(new String(orig));
			// System.out.println("#####");
				assertTrue(new String(orig).equals(new String(data[i])));

		}
		
		long delta = originalFile.length() - (data.length - 1) * data[0].length;
		char[] orig = new char[(int) delta];
		reader.read(orig);

		for(int i=0; i<delta; i++){
//			System.out.println(data[data.length - 1][i]);
//			System.out.println(orig[i]);
//			System.out.println("@@@@@");
			assertTrue(data[data.length - 1][i] == orig[i]);
		}	

	}
}
