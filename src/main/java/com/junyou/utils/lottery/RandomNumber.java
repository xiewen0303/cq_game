package com.junyou.utils.lottery;

/*
 *
 * 随机数字bean
 */

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Random;

public class RandomNumber {

	private Long randomnum = null;
	private Float randomfloat = null;
	private boolean floatvalue = false;
	private long upper = 100;
	private long lower = 0;
	private String algorithm = null;
	private String provider = null;
	private boolean secure = false;
	private Random random = null;
	private SecureRandom secrandom = null;

	private final float getFloat() {
		if (random == null){
			return secrandom.nextFloat();
		}else{
			return random.nextFloat();
		}
	}

	public final void generateRandomObject() throws Exception {

		// check to see if the object is a SecureRandom object
		if (secure) {
			try {
				// get an instance of a SecureRandom object
				if (provider != null)
					// search for algorithm in package provider
					secrandom = SecureRandom.getInstance(algorithm, provider);
				else
					secrandom = SecureRandom.getInstance(algorithm);
			} catch (NoSuchAlgorithmException ne) {
				throw new Exception(ne.getMessage());
			} catch (NoSuchProviderException pe) {
				throw new Exception(pe.getMessage());
			}
		} else
			random = new Random();
	}

	/**
	 * generate the random number
	 * 
	 */
	private final void generaterandom() {
		// check to see if float value is expected
		if (floatvalue)
			randomfloat = new Float(getFloat());
		else
			randomnum = Long.valueOf(lower + (long) ((getFloat() * (upper - lower))));
	}

	public final Number getRandom() {
		generaterandom();
		if (floatvalue)
			return randomfloat;
		else
			return randomnum;
	}

	public final void setRange(long low, long up) {

		// set the upper and lower bound of the range
		lower = low;
		upper = up;

		// check to see if a float value is expected
		if ((lower == 0) && (upper == 1))
			floatvalue = true;
	}

	/**
	 * set the algorithm name
	 * 
	 * @param value
	 *            name of the algorithm to use for a SecureRandom object
	 * 
	 */
	public final void setAlgorithm(String value) {
		algorithm = value;
		secure = true; // a SecureRandom object is to be used
	}

	public final void setProvider(String value) {
		provider = value;
	}

	public final void setRange(String value) throws Exception {
		try {
			upper = new Integer(value.substring(value.indexOf('-') + 1))
					.longValue();
		} catch (Exception ex) {
			throw new Exception("upper attribute could not be"
					+ " turned into an Integer default value was used");
		}

		try {
			lower = new Integer(value.substring(0, value.indexOf('-')))
					.longValue();
		} catch (Exception ex) {
			throw new Exception("lower attribute could not be"
					+ " turned into an Integer default value was used");
		}

		if ((lower == 0) && (upper == 1))
			floatvalue = true;

		if (upper < lower)
			throw new Exception("You can't have a range where the lowerbound"
					+ " is higher than the upperbound.");

	}
	
	public static void main(String[] args) throws Exception {
		RandomNumber r = new RandomNumber();
		r.generateRandomObject();
		r.setRange(0, 1);
		
		
		for (int i = 0; i < 100; i++) {
//			r.setProvider(value);
			
			System.out.println(r.getRandom());
		}
	}

}