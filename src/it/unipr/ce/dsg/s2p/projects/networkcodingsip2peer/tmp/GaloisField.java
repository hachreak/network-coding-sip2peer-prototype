package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GaloisField {
	
	private static char ZERO = 'z';
	private static final char ONE = 0;
	
	private static HashMap<Character,Character> zechLogs = null;
	
	private static byte n = 0;
	private static char q_1 = 0;
	
	public GaloisField(byte n) {
		GaloisField.n = n;
		GaloisField.q_1 = (char) (Math.pow(2, n) - 1);
		GaloisField.ZERO = GaloisField.q_1; 
		GaloisField.getZechLogTable();
	}
	
	public static char getZero(){
		return GaloisField.q_1;
	}
	
	public static char getOne(){
		return GaloisField.ONE;
	}
	
	private static void getZechLogTable() {
		zechLogs = new HashMap<Character,Character>((int)(q_1- 1),1.0f);
		File file = new File("GaloisField/ZechLogGF" + (int)Math.pow(2, n) + ".txt");
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			String line = br.readLine();
			String[] lineDigits = new String[2];
			while(line!=null) {
				lineDigits = line.split("\t");
				zechLogs.put((char)Integer.parseInt(lineDigits[0]),(char)Integer.parseInt(lineDigits[1]));
			       line=br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private char convertToSymbol(char valueReaded){
		if(valueReaded == 0)
			return ONE;
		else if(valueReaded == q_1)
			return ZERO;
		else
			return valueReaded;
	}
	
	private char convertToRepresentation(char element){
		if(element == ONE)
			return 0;
		else if(element == ZERO)
			return q_1;
		else
			return element;
	}
	
	public char sum(char a, char b){
		char elA = convertToSymbol(a);
		char elB = convertToSymbol(b);
		
		if(elA == elB)
			return convertToRepresentation(ZERO);
		
		if(elA == ZERO)
			return convertToRepresentation(elB);
		if(elB == ZERO)
			return convertToRepresentation(elA);
		
		if(elA == ONE)
			return convertToRepresentation(zechLogs.get(elB));
		if(elB == ONE)
			return convertToRepresentation(zechLogs.get(elA));
		
		char zechArg = (char) ((q_1 + elB - elA) % q_1);
		return convertToRepresentation((char) ((zechLogs.get(zechArg) + elA) % q_1));
		
	}
	
	public char minus(char a, char b){
		return this.sum(a,b);
	}
	
	public char product(char a, char b){
		char elA = convertToSymbol(a);
		char elB = convertToSymbol(b);
		
		if(elA == ZERO || elB == ZERO)
			return convertToRepresentation(ZERO);
		
		if(elA == ONE)
			return convertToRepresentation(elB);
		
		if(elB == ONE)
			return convertToRepresentation(elA);
		
		return convertToRepresentation((char) ((elA + elB) % q_1));
	}
	
	public char divide(char a, char b) throws ArithmeticException{
		char elA = convertToSymbol(a);
		char elB = convertToSymbol(b);
		
		if(elA == ZERO)
			return convertToRepresentation(ZERO);
		
		if(elB == ZERO)
			throw new ArithmeticException("Division by Zero");
		
		if(elA == ONE){
			if(elB == ONE)
				return convertToRepresentation(ONE);
			return convertToRepresentation((char) (q_1 - elB));
		}
		
		return convertToRepresentation((char) ((q_1 + elA - elB) % q_1));
	}
	
	public boolean isZero(char representation){
		if(this.convertToSymbol(representation)  == ZERO)
				return true;
		else
			return false;
	}

}
