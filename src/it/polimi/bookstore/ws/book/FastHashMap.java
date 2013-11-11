package it.polimi.bookstore.ws.book;

import java.util.HashMap;
import java.util.Map;

public class FastHashMap {
	public HashMap<String, Integer> map;
	public FastHashMap(Map<String, Integer> map){
		this.map = new HashMap<>(map);
	}
}
