import java.lang.reflect.Array;
import java.util.Arrays;
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
        //Trivial case --> If length of the array == 0, then the array is already sorted
        if(array.length == 1){
            return array;
        }

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
     * <p>The array passed as parameter won't be modified. The one ordered will be returned
     *
     * @see <a href="https://en.wikipedia.org/wiki/Quicksort">Quicksort</a>
     *
     * @param array The array that wants to be ordered
     * @param comparator The criteria that will order the array
     * @return the array provided, but ordered
     */
    public static <T> T[] quickSort(T[] array, Comparator<T> comparator){
    }
}
