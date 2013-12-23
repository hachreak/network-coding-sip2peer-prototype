/**
 * 
 */
package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tests;

import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.engine.CodingEngine;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.engine.NetworkCodingEngine;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.EncodedFragment;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.MediaResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.hachreak.projects.gfjama.matrix.GFMatrixException;
import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author hachreak
 * 
 */
public class TestNetworkCodingEngine {

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

	/**
	 * Test method for
	 * {@link it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.engine.NetworkCodingEngine#encodeFragment(java.util.ArrayList, it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.FragmentHeader)}
	 * .
	 */
	@Test
	public void testEncodeFragment() {
		// fail("Not yet implemented");
	}

	// @Test
	// public void testMuxAjGFvector(){
	// byte b = 8;
	// ArrayList< char[] > fragments = new ArrayList< char[] >();
	// char c1[] = "hello".toCharArray();
	// char c2[] = "world".toCharArray();
	// fragments.add(c1);
	// fragments.add(c2);
	//
	// GaloisField gf = new GaloisField(b);
	// final MediaResourceOLD m = new MediaResourceOLD(123456, fragments);
	//
	// NetworkCodingEngine e = new NetworkCodingEngine(gf){
	// public String toString(){
	// //generateGaloisMatrix(m);
	// byte b = 8;
	// GaloisField gf = new GaloisField(b);
	// char gfVector[] = "test1".toCharArray();
	// char c1[] = "hello".toCharArray();
	// char output[] = "‹Ÿ‡‡„".toCharArray();
	// //char output[] = "‹Ÿ‡er".toCharArray();
	// char ret[] = muxAjGFvector(gfVector[0], c1);
	// for(int i=0; i<output.length; i++){
	// assertTrue(ret[i] == output[i]);
	// //System.out.println(ret[i]+ " - " + output[i]);
	// }
	// return null;
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
	// };
	//
	// e.toString();
	// }
	//
	// @Test
	// public void testSumEncoded(){
	// byte b = 8;
	// ArrayList< char[] > fragments = new ArrayList< char[] >();
	// char c1[] = "hello".toCharArray();
	// char c2[] = "world".toCharArray();
	// fragments.add(c1);
	// fragments.add(c2);
	//
	// GaloisField gf = new GaloisField(b);
	// final MediaResourceOLD m = new MediaResourceOLD(123456, fragments);
	//
	// NetworkCodingEngine e = new NetworkCodingEngine(gf){
	// public String toString(){
	// //generateGaloisMatrix(m);
	// byte b = 8;
	// GaloisField gf = new GaloisField(b);
	// char gfVector[] = "test1".toCharArray();
	// char c1[] = "qwllo".toCharArray();
	// char c2[] = "world".toCharArray();
	// char output[] = "18,ˇZ".toCharArray();
	// //char output[] = "‹Ÿ‡er".toCharArray();
	// char ret[] = sumEncoded(c1, c2);
	// for(int i=0; i<output.length; i++){
	// //System.out.println(ret[i]+ " - " + output[i]+ " - "+ gf.sum('q', 'w'));
	// assertTrue(ret[i] == output[i]);
	// }
	// return null;
	// }
	//
	// protected char[] sumEncoded(char[] base, char[] muxAiGFvector) {
	// char result[] = new char[muxAiGFvector.length];
	// for (int i = 0; i < muxAiGFvector.length; i++) {
	// result[i] = gf.sum(base[i], muxAiGFvector[i]);
	// }
	// return result;
	// }
	// };
	//
	// e.toString();
	// }

	@Test
	public void testEncode() {
		File f = new File("tests/TestNetworkCodingEngine.txt");
		int fragmentSize = 10;
		GaloisField g = new GaloisField((byte) 8);
		MediaResource o = new MediaResource(f, fragmentSize, g);
		//MediaResource.loadTransposedPiece(new FileImageInputStream(f), f.length(), fragmentSize, index, galoisField);

		CodingEngine c = new NetworkCodingEngine(g);
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
			
//			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
//			int size = data[0].length;
//			for(int i=0; i<data.length; i++){
//				char[] orig = new char[size];
//				reader.read(orig);
//				assertTrue(new String(orig).equals(new String(data[i])));
//			}
			
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
