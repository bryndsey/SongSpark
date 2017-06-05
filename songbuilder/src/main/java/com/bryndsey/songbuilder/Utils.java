package com.bryndsey.songbuilder;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Random;

public class Utils {
	
	private static Random randGen = new Random();
	
	/*
	 * Returns a new array with the probabilities in the array normalized.
	 * The result is a new array in which the elements sum to 1.
	 */
	public static double[] normalizeProbs(double[] probs)
	{
		double[] normProbs = new double[probs.length];
		// make sure it has a length
		if (probs.length < 1)
			return probs;
		
		double probTotal = 0.0;
		for (int prob = 0; prob < probs.length; prob++)
		{			
			probTotal += probs[prob];
		}
		
		for (int i = 0; i < probs.length; i++)
		{
			normProbs[i] = probs[i] / probTotal;
		}
		
		return normProbs;
	}
	
	/*
	 * returns an index into an array based on the probabilities in the array
	 * The values in the array do not have to add up to 1. They will be scaled if not.
	 * returns -1 if something goes wrong
	 */
	public static int pickNdxByProb(double[] probs)
	{
		// make sure it has a length
		if (probs.length < 1)
			return -1;
		
		double probTotal = 0.0;
		for (int prob = 0; prob < probs.length; prob++)
		{			
			//get sum total of all probabilities
			probTotal += probs[prob];
		}
		
		//Random randGen = new Random();
		double goalVal = randGen.nextDouble();
		double curProbSum = 0.0;
		for (int ndx = 0; ndx < probs.length; ndx++)
		{			
			//check probability to see if we found the one
			curProbSum += probs[ndx]/probTotal;
			if (goalVal < curProbSum)
			{
				// found the one we want
				return ndx;
			}
		}
		
		//sentinel: should never get here
		return -1;
		
	}//pickNdxByProb
	
	/*
	 * returns the sum of the values in vals starting with the value at the index of startNdx and increasing 
	 * until (but not including) the value at endNdx If endNdx > startNdx, the iterator will loop to the beginning of vals.
	 * Throws IndexOutOfBoundsException if startNdx or endNdx is not within the bounds of vals
	 */
	public static int sumSubArray(int[] vals, int startNdx, int endNdx) throws IndexOutOfBoundsException
	{
		int sum = 0;
		
		if (startNdx < 0 || startNdx > vals.length || endNdx < 0 || endNdx > vals.length)
			throw new IndexOutOfBoundsException();
		
		int currNdx = startNdx;
		while (currNdx != endNdx)
		{
			sum += vals[currNdx];
			currNdx = (currNdx + 1) % vals.length;
		}
		
		return sum;
	}
	
	public static double[] combineProbs(double[] probs1, double[] probs2)
	{
		int minNumProbs = Math.min(probs1.length, probs2.length);
		int maxNumProbs = Math.max(probs1.length, probs2.length);
		if (maxNumProbs < 1)
			return new double[maxNumProbs];
		
		double[] combinedProbs = new double[maxNumProbs];
		double[] normProbs1 = normalizeProbs(probs1);
		double[] normProbs2 = normalizeProbs(probs2);
		
		for (int ndx = 0; ndx < maxNumProbs; ndx++)
		{
			if (ndx < minNumProbs)
				combinedProbs[ndx] = normProbs1[ndx] * normProbs2[ndx];
			else
				combinedProbs[ndx] = 0;
		}
		
		return combinedProbs;
	}
	
	public static void copyFile(File src, File dst) throws IOException
	{
	    FileInputStream inStream = new FileInputStream(src);
		FileChannel inChannel = inStream.getChannel();
	    FileOutputStream outStream = new FileOutputStream(dst);
		FileChannel outChannel = outStream.getChannel();
	    try
	    {
	        inChannel.transferTo(0, inChannel.size(), outChannel);
	    }
	    finally
	    {
	    	if (inStream != null)
	    		inStream.close();
	        if (inChannel != null)
	            inChannel.close();
	        if (outStream != null)
	    		outStream.close();
	        if (outChannel != null)
	            outChannel.close();
	    }
	}
	
}
