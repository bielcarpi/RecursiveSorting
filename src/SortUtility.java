import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * {@code SortUtility} is a class designed to help with sorting arrays of {@code Objects}.
 * <p>As we want to order an Object T, we'll use {@code Generics}.
 * <br>
 * <p>The class will provide methods for sorting an array with different algorithms, such as merge sort, quick sort or bucket sort
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/java/generics/types.html">Generics</a>
 * @author bielcarpi
 * @version 1.0
 */
public class SortUtility {

    public enum SortType{
        MERGE_SORT,
        QUICKSORT,
        BUCKET_SORT
    }

    /**
     * Orders (using merge sort) an array of {@code T Objects} given a {@link Comparator<T>} for that same {@code object}
     * <p>The array passed as parameter won't be modified. The one ordered will be returned
     *
     * @see <a href="https://en.wikipedia.org/wiki/Merge_sort">Merge Sort</a>
     *
     * @param array The array that wants to be ordered
     * @param comparator The criteria that will order the array
     * @return the array provided, but ordered
     */
    public static <T> T[] mergeSort(T[] array, Comparator<T> comparator){
        //Trivial case --> If length of the array == 1, then the array is already sorted
        if(array.length == 1)
            return array;

        //Non-Trivial case --> if length != 0
        int mid = array.length/2; //Calculate middle position of the array

        //Split the current array in two parts: left and right
        T[] leftPart = Arrays.copyOfRange(array, 0, mid);
        T[] rightPart = Arrays.copyOfRange(array, mid, array.length);

        leftPart = mergeSort(leftPart, comparator); //MergeSort the left part of the current array
        rightPart = mergeSort(rightPart, comparator); //Merge sort the right part of the current array
        return merge(leftPart, rightPart, comparator); // Merge both left and right parts, in order to get an ordered array
    }

    private static <T> T[] merge(T[] leftPart, T[] rightPart, Comparator<T> comparator){
        //We need to merge both left and right parts ordered in the arrayOrdered.
        //We know both leftPart and rightPart arrays are ordered! We only need to merge them

        //Create new array of T[], that will contain the ordered elements in leftPart + rightPart
        @SuppressWarnings("unchecked")
        T[] arrayOrdered = (T[]) Array.newInstance(leftPart.getClass().getComponentType(), leftPart.length + rightPart.length);
        int i = 0; //Index of the arrayOrdered
        int l = 0, r = 0; //leftPart and rightPart cursors

        while(l < leftPart.length || r < rightPart.length){ //While the left and right cursors aren't on its end

            if(l == leftPart.length){ //If the whole left part is already on the arrayOrdered
                //Then let's add the next element of the right array
                arrayOrdered[i] = rightPart[r];
                r++;
            }
            else if(r == rightPart.length){ //If the whole right part is already on the arrayOrdered
                //Then let's add the next element of the left array
                arrayOrdered[i] = leftPart[l];
                l++;
            }
            else if(comparator.compare(leftPart[l], rightPart[r]) > 0){ //If leftPart is bigger than rightPart
                arrayOrdered[i] = leftPart[l];
                l++;
            }
            else if(comparator.compare(leftPart[l], rightPart[r]) < 0){ //If rightPart is bigger than leftPart
                arrayOrdered[i] = rightPart[r];
                r++;
            }
            else if(comparator.compare(leftPart[l], rightPart[r]) == 0){ //If rightPart and leftPart are equal, copy leftPart
                arrayOrdered[i] = leftPart[l];
                l++;
            }

            //Increment i
            i++;
        }

        return arrayOrdered;
    }




    /**
     * Orders (using quicksort) an array of {@code T Objects} given a {@link Comparator<T>} for that same {@code object}
     * <p>The array passed as parameter will be the one modified (by reference). If you don't want this behavior,
     *   be sure to pass a copy of the original array
     *
     * @see <a href="https://en.wikipedia.org/wiki/Quicksort">Quicksort</a>
     *
     * @param array The array that wants to be ordered
     * @param comparator The criteria that will order the array
     */
    public static <T> void quickSort(T[] array, Comparator<T> comparator){
        //This method is a facade, let's invoke the real quickSort
        quickSortImplementation(array, comparator, 0, array.length-1);
    }

    private static <T> void quickSortImplementation(T[] array, Comparator<T> comparator, int i, int j){
        //Remind that i = the right index of the array & j = the left index of the array.
        //The array is always full, but we want only to modify it from i to j (j included)

        //Trivial Case --> If i >= j, return (it means that the portion of the array we want to modify
        //  is length 1. In this case it is already ordered, so return)
        if(i >= j)
            return;

        //Non-Trivial Case --> If the portion of the array we want to modify isn't length 1
        int pivotIndex = partition(array, comparator, i , j); //Select a pivot. After this function ends, the pivot
        //  has to be in its correct place inside the array. All elements bigger on its left, all elements smaller
        //  on its right.
        quickSortImplementation(array, comparator, i, pivotIndex - 1); //Perform a quicksort with the array from i to pivotIndex
        quickSortImplementation(array, comparator, pivotIndex + 1, j); //Perform a quicksort with the array from pivotIndex+1 to j
    }

    private static <T> int partition(T[] array, Comparator<T> comparator, int i, int j){
        if(i >= j) return -1; //i can't be bigger or equal than j. If it's the case, return error.

        int l = i; //l is our leftCursor, starting from i
        int r = j - 1; //j is our rightCursor, starting from j-1(as j will be the position where we'll put the pivot)
        //The objective of this method is to select a pivot (object in the array) and, on the end, the pivot has to be in its correct place
        //  inside the array. That means: all elements bigger of the pivot on its left, all elements smaller on its right.

        int pivotIndex = (i+j)/2;
        T pivot = array[pivotIndex]; //Our pivot in this particular implementation will be the center element of the range of the array we're dealing with
        swap(array, j, pivotIndex); //Move the pivot to the last position of the array (j)
        //Before the method ends, we'll swap the rightCursor element with our pivot
        //  as the rightCursor element will be the last element that's smaller than our pivot
        //  when we end the algorithm.

        //There can be a special case when we decrease j. In a 2 position array, i == j after decreasing.
        //The process implemented won't work on this case, so we'll handle length 2 arrays on this conditional
        if(r == l){
            if(comparator.compare(array[l], pivot) < 0) { //If the element with position l is less than the pivot, swap
                swap(array, l, j); //Swap pivot with l. Now the array is ordered
                return l; //Return the position of the pivot
            }
            else return j; //If the element with position l is bigger than the pivot, return the pivotIndex (the array is ordered)
        }


        //Infinite loop. It will break when i<=j, but first we need to move our cursors to its
        //  correct position. In order to achieve this, we'll put the breaking conditional just
        //  before moving the cursors.
        while(true){

            //While the element on the left is bigger than the pivot (that's what we want), increase the leftCursor (i)
            //We'll also check that the cursorLeft isn't getting out of bounds
            while(l < j && comparator.compare(array[l], pivot) > 0)
                l++;

            //While the element on the right is smaller than the pivot (that's what we want), decrease rightCursor (j)
            //We'll also check that the cursorRight isn't getting out of bounds
            while(r > i && comparator.compare(array[r], pivot) < 0)
                r--;

            //If the leftCursor is bigger than the rightCursor (they'll never be equal) stop, we're done.
            if(l >= r) break;

            //If not, it means that we've found two values that need to be swapped
            //  (as we have a value on the right of the pivot that's bigger than
            //  it, and a value on the left of the pivot that's smaller than it)
            swap(array, l, r);

            //As we've swapped, move a position both in j and i
            l++;
            r--;
        }


        swap(array, l, j); //As explained before, move the pivot (stored in j) to the position it should be
        return l; //Return the position of our pivot
    }

    private static <T> void swap(T[] array, int pos1, int pos2){
        if(pos1 == pos2) return;

        //This method swaps pos1 and pos2 of the array. pos1 will become pos2, and vice versa.
        T aux = array[pos1];
        array[pos1] = array[pos2]; //pos1 is pos2
        array[pos2] = aux; //pos2 is pos1
    }




    /**
     * Orders (using Recursive Bucket Sort) an array of {@code Series} from bigger popularity to smaller popularity
     * <p>The array passed as parameter won't be modified. The ordered one will be returned.
     * <p><b>This implementation only works with k=2, and it's not optimized. Use it for reference only.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Bucket_sort">Bucket Sort</a>
     * @param array The array that wants to be ordered
     */
    public static ArrayList<Series> bucketSort(ArrayList<Series> array){
        //Custom bucket sort (with k=2 always) -->
        //  We'll split the original array on two buckets each time (k=2).
        //  In one bucket, we'll put the values from biggerNum to midNum+1
        //  In the other bucket, we'll put the values from midNum to smallerNum
        //  We'll do this process recursively, until the array passed is of length <= 2. In
        //  this case, we'll order them (simple comparison of which one is bigger) and return
        //  it ordered. The different arrays will then get merged.

        //Some work is needed in order to, in this facade, be given int[] instead of ArrayList<Integer>
        return bucketSortImplementation(array);
    }

    private static ArrayList<Series> bucketSortImplementation(ArrayList<Series> array){
        //Trivial case --> If array length is >= 2, order it and return it
        if(array.size() == 2){ //If its size is 2, we need to order it before returning it
            //If the second position is bigger than the first position, swap them (we want them ordered from big to small)
            if(array.get(1).getPopularity() > array.get(0).getPopularity()) Collections.swap(array, 1, 0);
            return array;
        }
        else if(array.size() < 2){ //If it's less than size 2, it's already ordered
            return array;
        }

        //Non-trivial case --> The array length is more than 2, so we need to create two
        //  buckets and put the current array's values in these
        int minNum = Integer.MAX_VALUE;
        int maxNum = Integer.MIN_VALUE;
        for(int i = 0; i < array.size(); i++){
            if(array.get(i).getPopularity() < minNum) minNum = array.get(i).getPopularity(); //If minT is bigger than array[i], array[i] is our new smaller
            if(array.get(i).getPopularity() > maxNum) maxNum = array.get(i).getPopularity(); //If array[i] is bigger than maxT, array[i] is our new bigger
        }

        //We have a special case when minNum+2 is bigger than maxNum. We need at least a minimum, mid and maximum value that aren't equal
        //  for the algorithm to work. In this case (the offset of the elements is either 0 or 1, and we'll
        //  order them in this conditional.
        if(minNum + 1 >= maxNum){
            if(minNum == maxNum) return array; //If both min and maxNum are equal, the array is already sorted

            //If not, let's push all the minNum elements to the end of the array (we're sorting from big to small)
            for(int i = 0, k = 0; i < array.size() - k; i++){ //K is an aux to keep track of the values we remove from the array
                Series currentValue = array.get(i);
                if(currentValue.getPopularity() == minNum){ //If currentValue is the minimum value, push it to the end of the array
                    array.remove(i);
                    array.add(currentValue);
                    k++;
                    i--;
                }
            }
            return array;
        }

        int midNum = (maxNum+minNum)/2;

        ArrayList<Series> firstBucket = new ArrayList<>();
        ArrayList<Series> secondBucket = new ArrayList<>();

        for(int i = 0; i < array.size(); i++){
            if(array.get(i).getPopularity() > midNum) firstBucket.add(array.get(i)); //Add number to the first bucket if it's greater or equal than midNum
            else if(array.get(i).getPopularity() <= midNum) secondBucket.add(array.get(i)); //Add number to the second bucket if it's less than midNum
        }

        firstBucket = bucketSortImplementation(firstBucket);
        secondBucket = bucketSortImplementation(secondBucket);

        firstBucket.addAll(secondBucket); //Merge secondBucketOrdered into firstBucketOrdered
        return firstBucket; //Return both buckets ordered
    }

}
