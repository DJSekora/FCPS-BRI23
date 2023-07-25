public class SubstitutionCipher
{
  /* Version 1
     Main idea: pass in two arrays, one listing which characters will be changed
     and the other listing what those characters will change into.
     Outer loop goes through each character in the plaintext.
     Inner loop determines whether this character needs to be transformed,
     and applies the transformation if needed.
     Input lists must be same length!
   */
  public static String encryptSubstitution(String plaintext, char[] original, char[] transformed)
  {
    if(original.length != transformed.length)
    {
      System.out.println("ERROR: input char arrays must have equal length");
      return plaintext;
    }
    StringBuilder ret = new StringBuilder();
    char c;
    for(int i=0; i<plaintext.length(); i++) // for each position in the plaintext
    {
      c = plaintext.charAt(i); // get the character at position i
      for(int j=0; j<original.length; j++) // for each character in our input list
      {
        if(c == original[j]) // if we found one of the chars we need to transform
        {
          c = transformed[j]; // apply transformation
          break; // end inner loop early because we found the char
        }
      }
      ret.append(c); // add the char to the end of the String we're building
    }
    return ret.toString(); // ret is a StringBuilder not a String, so need to convert
  }
  
  /* Version 2 
     Main idea: pass in one array, which describes what each character transforms into
     For example, if you wanted to transform 'A' into 'F', then key['A'] would be 'F'.
     (This works because characters are stored as numbers! So key[65] would also be 'F')
     Pros: This method was much faster to write, and it will also generally run faster
     Cons: The key array has to be long enough to cover all of the characters we
     want to transform
  */
  public static String encryptSubstitutionV2(String plaintext, char[] key)
  {
    StringBuilder ret = new StringBuilder();

    for(int i=0; i<plaintext.length(); i++)
    {
      // no need for an inner loop, because we know exactly where to look
      ret.append(key[plaintext.charAt(i)]);
    }
    return ret.toString();
  }
  
  /* Version 3
     Main idea: use existing methods to save time
     Java's String class provides us with a convenient replace method, which
     replaces all of one character in a string with a different character.
     This seems perfect - that's exactly what we want to do! So let's save
     ourselves some time.
     We need to loop through the arrays rather than the plaintext String
     (although we still essentially have a nested loop, because replace
      uses a loop underneath)
     Pros: Very fast to write!
     Cons: To be determined in class.
   */
  public static String encryptSubstitutionV3(String plaintext, char[] original, char[] transformed)
  {
    String ret = plaintext;
    
    for(int i=0; i<original.length; i++)
    {
      ret.replace(original[i], transformed[i]);
    }
    
    return ret;
  }
  
  
  /* Sample main method to test our code */
  public static void main(String[] args)
  {
    String plain = "WE LOVE CTY ITS THE BEST";
    char[] orig   =  new char[]{'E', 'H', 'L', 'O'};
    char[] trans = new char[]{'H', 'E', 'O', 'L'};
    
    char[] key = new char[65536];
    for(int i=0; i<key.length; i++)
    {
      key[i] = (char)i;
    }
    key['E'] = 'H';
    key['H'] = 'E';
    key['L'] = 'O';
    key['O'] = 'L';
    
    String v1 = encryptSubstitution(plain, orig, trans);
    String v2 = encryptSubstitutionV2(plain, key);
    String v3 = encryptSubstitutionV3(plain, orig, trans);
    
    System.out.println(v1);
    System.out.println(v2);
    System.out.println(v3);
  }
}