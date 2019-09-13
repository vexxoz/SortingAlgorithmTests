import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * A program to test run times and efficiency of sorting algorithms
 * 
 * Completion time: 3 Hours
 *
 * @author Blake Caldwell, Acuna, Sedgewick
 * @version 1.0
 */


public class BaseNonUniform implements SER222_02_01_HW02_Submission {
    
	
    /**
    * Generates an array of integers where half the data is 0s, half 1s.
    
     * @param size number of elements in the array.
     * @return generated test set.
    **/
    public Integer[] generateTestDataBinary(int size) {
    	Integer[] array = new Integer[size];
    	int index = 0;
    	for(int i=0;i<size;i++) {
    		if(i==size/2) {
    			index++;
    		}
    		array[i] = index;
    	}
    	return array;
    }
    
    /**
     * Generates an array of integers where half the data is 0s, half the
     * remainder is 1s, half the reminder is 2s, half the reminder is 3s, and so
     * forth. 
     * 
     * @param size number of elements in the array.
     * @return generated test set.
     */
    public Integer[] generateTestDataHalfs(int size) {
    	Integer[] array = new Integer[size];
    	int tempSize = size/2;
    	int index = 0;
    	for(int i=0;i<size;i++) {
    		if(i==tempSize) {
    			index++;
    			tempSize += ((size-tempSize)/2);
    		}
    		array[i] = index;
    	}
    	return array;
    }
    
    /**
     * Generates an array of integers where half the data is 0s, and half random
     * int values.
     * @param size
     * @return 
     */
    public Integer[] generateTestDataHalfRandom(int size) {
    	Integer[] array = new Integer[size];
    	int index = 0;
    	for(int i=0;i<size;i++) {
    		if(i>size/2) {
    			Random rand = new Random();
    			index = rand.nextInt();
    		}
    		array[i] = index;
    	}
    	return array;
    }
    
    private static double logBase2(double in) {
    	return Math.log(in)/Math.log(2.0);
    }
    
    /**
     * Computes the double formula value for two run times.
     * 
     * @param t1 first time (double N than t2)
     * @param t2 second time
     * @return b value
     */
    public double computeDoublingFormula(double t1, double t2) {
    	return logBase2(t1/t2);
    }
    
    /**
     * Computes an empirical b value for insertion sort by running it on a pair
     * of inputs and using the doubling formula.
     * 
     * @param small small test data array
     * @param large large test data array. twice the same of small array.
     * @return b value
     */
    public double benchmarkInsertionSort(Integer[] small, Integer[] large) {
    	Stopwatch myWatch = new Stopwatch();
    	insertionSort(small);
    	double smallTime = myWatch.elapsedTime();
    	myWatch = new Stopwatch();
    	insertionSort(large);
    	double largeTime = myWatch.elapsedTime();
    	return computeDoublingFormula(largeTime, smallTime);
    }
    
    /**
     * Computes an empirical b value for shellsort sort by running it on a pair
     * of inputs and using the doubling formula.
     * @param small small test data array
     * @param large large test data array. twice the same of small array.
     * 
     * @return b value
     */
    public double benchmarkShellsort(Integer[] small, Integer[] large) {
    	Stopwatch myWatch = new Stopwatch();
    	shellsort(small);
    	double smallTime = myWatch.elapsedTime();
    	myWatch = new Stopwatch();
    	shellsort(large);
    	double largeTime = myWatch.elapsedTime();
    	return computeDoublingFormula(largeTime, smallTime);
    }
    
    /**
     * Runs the two sorting algorithms on the three types of test data to
     * produce six different b values. B values are displayed to the user.
     * 
     * @param size size of benchmark array. to be doubled later.
     */
    public void runBenchmarks(int size) {
    	Integer[] halfBinary = generateTestDataBinary(size);
    	Integer[] halfBinaryLarge = generateTestDataBinary(size*2);
    	
    	Integer[] halfIncrement = generateTestDataHalfs(size);
    	Integer[] halfIncrementLarge = generateTestDataHalfs(size*2);
    	
    	Integer[] halfRandom = generateTestDataHalfRandom(size);
    	Integer[] halfRandomLarge = generateTestDataHalfRandom(size*2);
    	
    	
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
    	
    	double binInsertion = benchmarkInsertionSort(halfBinary,halfBinaryLarge);
    	double halfInsertion = benchmarkInsertionSort(halfIncrement,halfIncrementLarge);
    	double randIntInsertion = benchmarkInsertionSort(halfRandom,halfRandomLarge);
    	
    	double binShell = benchmarkShellsort(halfBinary,halfBinaryLarge);
    	double halfShell = benchmarkShellsort(halfIncrement,halfIncrementLarge);
    	double randIntShell = benchmarkShellsort(halfRandom,halfRandomLarge);
    	
    	System.out.println("	  Insertion 	  Shellsort");
    	System.out.println("Bin		" + df.format(binInsertion) + "		" + df.format(binShell));
    	System.out.println("Half		" +df.format(halfInsertion) +"		" +df.format(halfShell));
    	System.out.println("RandInt		" +df.format(randIntInsertion) +"		" + df.format(randIntShell));
    }
	
    /***************************************************************************
     * START - SORTING UTILITIES, DO NOT MODIFY (FROM SEDGEWICK)               *
     **************************************************************************/
    
    public static void insertionSort(Comparable[] a) {
        int N = a.length;
        
        for (int i = 1; i < N; i++)
        {
            // Insert a[i] among a[i-1], a[i-2], a[i-3]... ..          
            for (int j = i; j > 0 && less(a[j], a[j-1]); j--)
                exch(a, j, j-1);
        }
    }
    
    
    public static void shellsort(Comparable[] a) {
        int N = a.length;
        int h = 1;
        
        while (h < N/3) h = 3*h + 1; // 1, 4, 13, 40, 121, 364, 1093, ...
        
        while (h >= 1) {
            // h-sort the array.
            for (int i = h; i < N; i++) {
                // Insert a[i] among a[i-h], a[i-2*h], a[i-3*h]... .
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h)
                exch(a, j, j-h);
            }
            h = h/3;
        }
    }
    
    
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
    
    
    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i]; a[i] = a[j]; a[j] = t;
    }
    
    /***************************************************************************
     * END - SORTING UTILITIES, DO NOT MODIFY                                  *
     **************************************************************************/

    //TODO: implement interface methods.

    public static void main(String args[]) {
        SER222_02_01_HW02_Submission me = new BaseNonUniform();
        int size = 4096*15;
        
        //Integer[] test = me.generateTestDataHalfRandom(size);
        
        // Test binary array data
        //System.out.println("Binary Array: " + me.generateTestDataBinary(size).toString());
        // test half array
        //System.out.println("Half Array: " + me.generateTestDataHalfs(size));
        // test rand array
        //System.out.println("Rand Array: " + me.generateTestDataHalfRandom(size));        
        //NOTE: feel free to change size here. all other code must go in the
        //      methods.
        
        me.runBenchmarks(size);
    }
}