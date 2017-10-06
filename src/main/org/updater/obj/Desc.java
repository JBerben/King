package src.main.org.updater.obj;

import java.util.ArrayList;
import java.util.Arrays;

import src.main.org.updater.trans.Classes;

public class Desc {

	private String desc;
	private ArrayList<String> descriptors = new ArrayList<>(Arrays.asList("B", "C", "D", "F", "I", "S", "Z", "L"));
	public int b;
	public int c;
	public int d;
	public int f;
	public int i;
	public int s;
	public int z;
	public int l;
	
	public Desc(String desc) {
		this.desc = desc;
		getCount();
	}
	
	/*
	 * GETTERS
	 */
	public String getDesc() {
		return this.desc;
	}
	
	/*
	 * SETTERS
	 */
	public String setDesc(String desc) {
		return this.desc = desc;
	}
	
	/*
	 * FUNCTIONS
	 */
	
	// Doesnt count arrays :(
	private void getCount() {
		String descriptor;
		setDesc(Classes.parseObjectDesc(desc));
		for (int i = 0; i < this.desc.length(); i++) {
			if (descriptors.contains(desc.charAt(i))) {
				descriptor = Character.toString(desc.charAt(i));
				switch(descriptor) {
				case "B" : this.b++;
				case "C" : this.c++;
				case "D" : this.d++;
				case "F" : this.f++;
				case "I" : this.i++;
				case "S" : this.s++;
				case "Z" : this.z++;
				case "L" : this.l++;
				}
			}
		}
	}
}
