package util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UndoHashMap <K,V> extends java.util.AbstractMap<K,V> implements java.util.Map<K,V>, java.lang.Cloneable, java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int STATE_CLEAN = 1;
	public static final int STATE_DIRTY = 2;

	HashMap<K, V> map = new HashMap<K, V>();
	HashMap<K, V> mapChanged;
	Iterator<V> iterator;	
	
	int state;
		
	@SuppressWarnings("unchecked")
	public V put(K key, V value){
		if (mapChanged==null){
			mapChanged = (HashMap<K, V>) map.clone();
		}
		
		mapChanged.put(key, value);
		state = STATE_DIRTY;
		
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public void remove(V value){
		if (mapChanged==null){
			mapChanged = (HashMap) map.clone();
		}
		
		mapChanged.remove(value);
		state = STATE_DIRTY;
		
	}


	public void commit(){
		if (state== STATE_DIRTY){
			map = mapChanged;
			clean();			
		}	
	}
	
	public void rollback(){
		clean();
	}
	
	public void clean(){
		mapChanged = null;
		state = STATE_CLEAN;
		iterator = null;
	}
	
	public V getNextElement(){
		if (iterator == null){
			iterator = map.values().iterator();
		}
		
		while (iterator.hasNext()){
			V obj = iterator.next();
			if (mapChanged==null || mapChanged.containsValue(obj))
				return obj;
		}
		
		return null;
	}

	public Object getFirstElement() {
		if (map.values()!=null){
			Iterator<V> iterator = map.values().iterator();
			while (iterator.hasNext()){
				V obj = iterator.next();
				if (mapChanged==null || mapChanged.containsValue(obj))
					return obj;
			}
		}
		
		return null;
	}
	
	@Override
	public void clear() {
		map.clear();		
	}

	@Override
	public boolean containsKey(Object key) {
		if (state==STATE_DIRTY){
			return mapChanged.containsKey(key);
		}
		
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		if (state==STATE_DIRTY){
			return mapChanged.containsValue(value);
		}
		
		return map.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean isEmpty() {
		if (state==STATE_DIRTY){
			return mapChanged.isEmpty();
		}
		
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		if (state==STATE_DIRTY){
			return mapChanged.keySet();
		}
		return map.keySet();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Entry<? extends K, ? extends V> entry: m.entrySet()){
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public int size() {
		if (state==STATE_DIRTY){
			return mapChanged.size();
		}
		
		return map.size();
	}

	@Override
	public Collection<V> values() {
		if (state==STATE_DIRTY){
			return mapChanged.values();
		}

		return map.values();
	}
}
