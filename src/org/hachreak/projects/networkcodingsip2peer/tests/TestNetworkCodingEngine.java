/**
 * 
 */
package org.hachreak.projects.networkcodingsip2peer.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
 * @author hachreak
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
			
			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			int size = data[0].length;
			System.out.println(data.length);
			for(int i=0; i<data.length; i++){
				char[] orig = new char[size];
				reader.read(orig);
				assertTrue(new String(orig).equals(new String(data[i])));
//				System.out.println("TEST "+i);
			}
			
			// save
			MediaResource.save(new FileOutputStream(new File(
					"tests/TestNetworkCodingEngine.decoded.txt")), data); 

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

		// byte b = 8;
		// ArrayList< char[] > fragments = new ArrayList< char[] >();
		// char c1[] = "h".toCharArray();
		// char c2[] = "w".toCharArray();
		// fragments.add(c1);
		// fragments.add(c2);
		//
		// GaloisField gf = new GaloisField(b);
		// final MediaResourceOLD m = new MediaResourceOLD(123456, fragments);
		//
		// NetworkCodingEngine e = new NetworkCodingEngine(gf){
		// // public NetworkCodingEngine()
		// public String toString(){
		// List<Fragment> fe = encode(m);
		// for(int i=0; i<fe.size(); i++){
		// System.out.println(fe.get(i).getBuffer());
		// }
		// System.out.println("encoded");
		// return null;
		// }
		// protected void generateGaloisMatrix(MediaResourceOLD ms){
		// System.out.println("fiillll");
		// setOutputNumOfFragments(ms);
		// // System.out.println(outputNumOfFragments);
		// // System.out.println(ms.getTotalNumberOfFragments());
		// // System.out.println(ms.getGenerationsAmount());
		// G = new RandomGFMatrix(ms.getGenerationsAmount(),
		// outputNumOfFragments, getGaloisField(), 10);
		// //G.printMatrix();
		// }
		// };
		//
		// e.toString();
	}
}
