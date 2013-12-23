package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tests;

import static org.junit.Assert.*;
import it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource.OriginalMediaResourcePiece;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestOriginalMediaResourcePiece {

	private InputStream is;
	//private String inputString = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).";
	private File file = new File("tests/TestOriginalMediaResourcePiece.txt"); 
	private int fragmentSize = 5;
	private GaloisField galoisField = new GaloisField((byte) 8);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		//is = new ByteArrayInputStream(inputString.getBytes());
	}

	@Test
	public void testOriginalMediaResourcePiece() {
		try {
			int index = 3;
			fragmentSize = 5;
			OriginalMediaResourcePiece o = new OriginalMediaResourcePiece(file, fragmentSize, index, galoisField);
			assertTrue(o.get(0, 0) == 'i');
			assertTrue(o.get(0, 1) == 'l');
			assertTrue(o.get(0, 2) == 'e');
			assertTrue(o.get(0, 3) == 'l');
			assertTrue(o.get(0, 4) == 'd');
			assertTrue(o.get(0, 5) == 't');
		} catch (IOException e) {
			assertTrue(false);
		}
	}
	
//	@Test
//	public void testGenerateDigest(){
//		int index = 0;
//		OriginalMediaResourcePiece o;
//		try {
//			o = new OriginalMediaResourcePiece(file, fragmentSize, index, galoisField);
//			System.out.println(o.generateResourceDigest().toString());
//		} catch (IOException e) {
//			assertTrue(false);
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			assertTrue(false);
//			e.printStackTrace();
//		}
//		
//	}
}
