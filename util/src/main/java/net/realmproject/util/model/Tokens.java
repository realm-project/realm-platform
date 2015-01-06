package net.realmproject.util.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Tokens {

	private final static String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private final static Random rand = new Random();
	
	public static String create() {
		return create(3);
	}
	
	private static String create(int blocks) {
		return create(blocks, 4, rand);
	}
	
	private static String create(int blocks, int blockSize, Random random) {
		List<String> blockList = new ArrayList<>();
		for (int i = 0; i < blocks; i++) {
			blockList.add(block(blockSize, random));
		}
		return blockList.stream().collect(Collectors.reducing((a, b) -> a + "-" + b)).get();
	}
	
	private static String block(int blockSize, Random random) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < blockSize; i++) {
			builder.append(character(random));
		}
		return builder.toString();
	}
		
	private static String character(Random random) {
		int index = random.nextInt(characters.length() - 1);
		return characters.substring(index, index+1);
	}


}
