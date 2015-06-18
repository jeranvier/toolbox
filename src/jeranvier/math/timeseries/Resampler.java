package jeranvier.math.timeseries;

import java.util.Map.Entry;

@FunctionalInterface
public interface Resampler<K, V> {
	 public V interpolate(Entry<K, V> floor, K key, Entry<K, V> ceiling);
}
