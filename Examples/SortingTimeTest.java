import java.util.Random;
public class SortingTimeTest
{
  public static void main(String[] args)
  {
    int n = 100000;
    
    Random rng = new Random();
    
    int[] myArray = new int[n]; // make a blank array with n slots
    for(int i=0; i < n; i++)
    {
      myArray[i] = rng.nextInt(n);
    }
    
    int[] sorted = new int[n];
    for(int i=0; i < n; i++)
    {
      sorted[i] = i;
    }
    
    long start = System.currentTimeMillis();
    SortsExample.mergeSort(myArray);
    long end = System.currentTimeMillis();
    System.out.println(end - start);
    
    start = System.currentTimeMillis();
    SortsExample.mergeSort(sorted);
    end = System.currentTimeMillis();
    System.out.println(end - start);
  }
}