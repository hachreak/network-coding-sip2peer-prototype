/**
 * 
 */
package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tmp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author hachreak
 *
 */
public class TestRandomGFMatrix {

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
	 * Test method for {@link it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tmp.RandomGFMatrix#RandomGFMatrix(byte, byte, it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tmp.GaloisField, long)}.
	 */
	@Test
	public void testRandomGFMatrixByteByteGaloisFieldLong() {
		byte m = 2;
		byte n = 3;
		byte b = 8;
		GaloisField gf = new GaloisField(b);
		RandomGFMatrix rgfm = new RandomGFMatrix(m, n, gf, 100);
		//rgfm.printMatrix();
		char matrix[][] = //new char[m][n];
//		matrix[0] = 
			{ { 65328,	65428,	101 },	
				{65505,	65493,	140 }};
		for(char i=0; i<matrix.length;i++){
			for(char j=0; j<matrix[0].length; j++){
				assertTrue(matrix[i][j] == rgfm.get(i)[j]);
				//System.out.println((int)i+":"+(int)j+" - "+(int)matrix[i][j] + " - "+(int)rgfm.get(i)[j]);
			}
		}
		
		for(char i=0; i<matrix.length;i++){
			for(char j=0; j<matrix[0].length; j++){
				assertTrue(matrix[i][j] == rgfm.getColumn(j)[i]);
				//System.out.println((int)i+":"+(int)j+" - "+(int)matrix[i][j] + " - "+(int)rgfm.getColumn(j)[i]);
			}
		}
	}

}
