package main;

import util.FileUtil;

public class Main {

	public static void main(String[] args) {
		if(FileUtil.loadConfigs()) {
			System.out.println("Configs were loaded....");
		}
		
	}

}
