package com.toocol.utilities;

/**
 * The filter interface
 *
 * @author Joezeo
 */
@FunctionalInterface
public interface Filter<T> {
	/**
	 * is accept obj
	 *
	 * @param t the obj to accept
	 * @return is accept obj
	 */
	boolean accept(T t);
}