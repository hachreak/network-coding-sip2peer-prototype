package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource;

import org.hachreak.projects.gfjama.matrix.GFMatrix;
import org.hachreak.projects.gfjama.matrix.GaloisField;

public class EncodedMediaResourcePiece extends GFMatrix{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1303627365946299070L;

	public EncodedMediaResourcePiece(int m, int n, GaloisField galoisField) {
		super(m, n, galoisField);
	}

}
