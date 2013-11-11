package it.polimi.bookstore.ws.book;

import java.util.HashMap;
import java.util.Map;

public class HashMapWrapper {
	public HashMap<String, Integer> map;
	public HashMapWrapper(Map<String, Integer> map){
		this.map = new HashMap<>(map);
	}
}
