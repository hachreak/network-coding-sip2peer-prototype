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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.hachreak.projects.gfjama.matrix.GFMatrix;
import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.hachreak.projects.networkcodingsip2peer.resource.MediaResource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Leonardo Rossi <leonardo.rossi@studenti.unipr.it>
 * 
 */
public class TestMediaResource {

	// private InputStream is;
	// private String inputString =
	// "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).";
	private File file = new File("tests/TestOriginalMediaResourcePiece.txt");
	private int fragmentSize = 5;
	private GaloisField galoisField = new GaloisField((byte) 8);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// is = new ByteArrayInputStream(inputString.getBytes());
	}

	@Test
	public void testLoadTransposedPiece() {
		try {
			int index = 3;
			fragmentSize = 5;
			GFMatrix o = MediaResource.loadTransposedPiece(new FileInputStream(
					file), (int) file.length(), fragmentSize, index,
					galoisField);
			// OriginalMediaResourcePiece o = new MediaResource(file,
			// fragmentSize, index, galoisField);
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

	// @Test
	// public void testGenerateDigest(){
	// int index = 0;
	// OriginalMediaResourcePiece o;
	// try {
	// o = new OriginalMediaResourcePiece(file, fragmentSize, index,
	// galoisField);
	// System.out.println(o.generateResourceDigest().toString());
	// } catch (IOException e) {
	// assertTrue(false);
	// e.printStackTrace();
	// } catch (NoSuchAlgorithmException e) {
	// assertTrue(false);
	// e.printStackTrace();
	// }
	//
	// }
}
