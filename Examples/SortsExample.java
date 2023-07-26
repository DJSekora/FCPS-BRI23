/* This class includes examples of several sorting algorithms. */
public class SortsExample
{
  // Sample main method
  public static void main(String[] args)
  {
    int[] list = new int[]{6,2,8,43,87,-23,37, 20};
    
    long start = System.currentTimeMillis();
    slowBubbleSort(list);
    long end = System.currentTimeMillis();
    System.out.println(end - start);
  }
  
  // Utility method to print an array
  public static void printArray(int[] banana)
  {
    for(int i=0; i<banana.length; i++)
    {
      System.out.print(banana[i] + " ");
    }
    System.out.println();
  }

  /* The core idea of bubble sort is that we swap adjacent entries if they are
     out of order. We keep doing this until the list is sorted!
     
     This works because the ordering systems we use for lists generally have
     the transitive property. So, if every adjacent pair in the list is in
     order, the entire list is in order.
   */
  public static void bubbleSort(int[] list)
  {
    // keeps track of if we need to continue sorting
    boolean sorted = false;
    int cap = list.length;
    while(!sorted)
    {
      sorted = true;
      for(int i=1; i<cap; i++)
      {
        // if entries are out of order, swap them
        if(list[i-1] > list[i])
        {
          int temp = list[i]; // make sure we don't lose data
          list[i] = list[i-1];
          list[i-1] = temp;
          sorted = false;
        }
      }
      cap--;
    }
  }
  
  public static void slowBubbleSort(int[] list)
  {
    for(int step = 1; step <list.length; step++)
    {
      for(int i=1; i<list.length; i++)
      {
        // if entries are out of order, swap them
        if(list[i-1] > list[i])
        {
          int temp = list[i]; // make sure we don't lose data
          list[i] = list[i-1];
          list[i-1] = temp;
        }
      }
    }
  }
  
  /* For selection sort, we find the smallest element of the list and put it
     into the correct location. Repeat this until the list is sorted.
   */
  public static void selectionSort(int[] list)
  {
    // outer for loop iterates through indices
    // When i is k, that means we are finding the correct element
    // to put in slot k
    for(int i = 0; i<list.length; i++)
    {
      int smallestIndex = i;
      // Find the LOCATION OF the smallest thing in the unsorted area of the list
      for(int j = i + 1; j < list.length; j++)
      {
        // if we found a smaller thing, update smallestIndex
        if(list[j] < list[smallestIndex])
        {
          smallestIndex = j;
        }
      }
      
      // Now swap that smallest item into the correct location
      int temp = list[i];
      list[i] = list[smallestIndex];
      list[smallestIndex] = temp;
    }
  }
  
  /* For insertion sort, we maintain a sorted list and an unsorted list.
     We take the next element from the unsorted list and insert it into the
     correct location in the sorted list so that the sorted list remains
     sorted.
     
     It might be more natural to do this with two different lists, but we
     prefer to do "in-place" sorting if possible (we want to avoid using any
     more space than necessary without sacrificing runtime).
     
     The total number of elements doesn't change, so we just use the first
     part of the array as the sorted list and the rest as the unsorted list.
     Then, all we have to do is keep track of where the boundary is!
   */
  public static void insertionSort(int[] list)
  {
    int current;
    // i tells us the index of the element we are currently trying to insert
    for(int i = 0; i<list.length; i++)
    {
      // j is looking at everything in the sorted portion to figure out where
      // our highlighted element should go
      int j;
      for(j = 0; j<i; j++)
      {
        if(list[i] < list[j])
        {
          break; // at this point, j tells us where the item at slot i should go
        }
      }
      
      int temp = list[i];
      // Now insert the new item in the correct location
      for(int k = i; k > j; k--)
      {
        list[k] = list[k-1];
      }
      list[j] = temp;
    }
  }
  
  public static void bogoSort(int[] list)
  {
 /* while(!isSorted(list))
    {
      randomizeList(list);
    }*/
  }
  
  public static void mergeSort(int[] list)
  {
    if( list.length < 2 )
    {
      return; // end method
    }
  
    // Step 1: separate into two sublists
    int size = list.length/2;
    int[] first = new int[size];
    int[] second = new int[list.length - size];
    // fill first sublist
    for(int i=0; i < size; i++)
    {
      first[i] = list[i];
    }
    // fill second sublist
    for(int i = size; i < list.length; i++)
    {
      second[i-size] = list[i];
    }
   
    // Step 2: sort the sublists (we'll come back to this later)
    mergeSort(first);
    mergeSort(second);
    
    // Step 3: merge the sorted sublists
    int i1 = 0; // where we are in the first list
    int i2 = 0; // where we are in the second list
    
    // For each slot in the list:
    for(int i=0; i<list.length; i++)
    {
      if(i1 >= first.length)
      {
        list[i] = second[i2];
        i2++;
      }
      else if(i2 >= second.length)
      {
        list[i] = first[i1];
        i1++;
      }
      else
      {
        if(first[i1] < second[i2])
        {
          list[i] = first[i1];
          i1++;
        }
        else
        {
          list[i] = second[i2];
          i2++;
        }
      }
    }
  }
}