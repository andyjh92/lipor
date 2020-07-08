package io.gitlab.lipor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelperFunctions {
	public  static <T> Stream<T> stream(Collection<T> collection) {
		return collection != null ? collection.stream() : Stream.empty();
	}
	public  static <T> Stream<T> stream(T[] array) {
		return array != null ? Arrays.asList(array).stream() : Stream.empty();
	}
	
	public static boolean isEmpty(String str) {
		return str==null || str.trim().length() == 0;
	}
	
	public static <T> boolean isEmpty(T[] array) {
		return array==null || array.length == 0;
	}
	
	public static <T> boolean isEmpty(Collection<T> collection) {
		return collection==null || collection.size() == 0;
	}
	
	public static <T> List<T> nonNullList(Collection<T> collection) {
		return stream(collection).collect(Collectors.toList());
	}
	
	public static <T> List<T> nonNullList(T[] array) {
		return stream(array).collect(Collectors.toList());
	}
}
