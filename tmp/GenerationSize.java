package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource;

/**
 * Enum that lets to set the generation's size
 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
 *
 */
public enum GenerationSize {
	TEST ((byte)5),
	LITTLE ((byte)50),
	BIG ((byte)100);
	
	private final byte value;
	GenerationSize(byte value){
		this.value = value;
	}
	
	public byte getValue(){
		return this.value;
	}

}
