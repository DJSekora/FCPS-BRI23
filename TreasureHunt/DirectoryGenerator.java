/* Treasure Hunt activity generator
 * For use in CTY courses and other teaching ventures by David Sekora
 *
 * Must have wordsEn.txt and probably Decrypter.class in the same folder as
 * this file (or accessible from the classpath) for it to work properly.
 *
 * Code is provided as-is and with no guarantee that it is well-documented.
 *
 * The source code and scripts that this program writes and inserts
 * were written and tested separately before being ported into this file.
 *
 * This is meant for ease of distribution, which is why everything has been
 * consolidated into a single source code file. This is not general good
 * practice, and much of the code here is difficult to read and interpret.
 *
 * The first few hundred lines contain the main workhorse methods, and the
 * end contains the "game script".
 *
 * Developed over the course of 8 years, with wildly different programming
 * skills and philosophies. So, style is inconsistent.
 *
 * Code and scenario by David Sekora
 * (A few libraries used in a handful of places from third party sources, I think those are credited there)
 */

import java.io.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/* Known issues:
 * Some seeds don't work (investigate later if less tired)
 *
 * Don't remember what seed got used for CTY2023, need to check flash drive
 *
 * This version might have small bugs, don't remember if I fixed here after fixing release version
 */
public class DirectoryGenerator
{
  public static final int MAX_FILES = 100;
  public static final int MAX_CHARS_PER_FILE = 2000;
  public static final int PUNCTUATION_FREQUENCY = 10;

  public static Random random = new Random(198273);
  public static ArrayList<String> wordList = new ArrayList<String>();
  public static String ps = System.getProperty("file.separator");
  
  // List of all leaf directories that can contain files. Remove from this list
  // when one is used
  public static ArrayList<LeafDir> leafDirs = new ArrayList<LeafDir>();
  
  public static Scanner user;
  
  // default encryption offset
  public static final int DFO = 14;
  public static final int CLASS_STRING_DFO = -22;
  
  // Unused but maybe for a future version
  public static final String badPassword = "ChatGPT will solve all our problems";
  public static final String goodPassword = "I will not play games in class that I did not program";
  
  // test main method, not part of real program
  public static void maidn(String[] args)
  {
    int[] test = pickFileNums(30,new LeafDir("test",30));
    for(int i:test)
      System.out.println(i);
  }

  // actual main method
  public static void main(String[] args)
  {
    if(ps.equals("\\"))
    {
      ps = "\\\\";
    }
  
    //generateRandomGIF(300,300,100,50,"test.gif");
    //generateRandomWAV(5,100,600,"test.wav");
    user = new Scanner(System.in);
    loadEnglishWords("wordsEn.txt");
    
    //String rootName = "Start"; // this has been moved to the script area very far down
    makeDir(rootName);
    buildDirTree(new File(rootName), 10, 5);
    
    addSpecialFiles();
    user.close();
    
    debug();
  }
  
  public static void debug()
  {
    System.out.println(asciiInterludeDir);
    System.out.println(firstInstructionsDir);
    System.out.println(nextStepDir);
    System.out.println(gatemasterDir);
    System.out.println(gatemasterDir2);
    System.out.println(gatemasterDir3);
    System.out.println(snitchDir);
    System.out.println(snitchCousinDir);
    System.out.println(revealerDir);
    System.out.println(passcode2Dir);
    System.out.println(passcode3Dir);
    System.out.println(passcode4Dir);
    System.out.println(selfDestructDir);
  }

  public static void loadEnglishWords(String fname)
  {
    try
    {
      BufferedReader rd = new BufferedReader(new FileReader(fname));
      String nl;
      while((nl = rd.readLine()) != null)
      {
        wordList.add(nl);
      }
    }
    catch(Exception e)
    {System.out.println(e.getMessage());}
  }

  public static String getRandomUppercaseWord()
  {
    String r = wordList.get(random.nextInt(wordList.size()));
    if(r.length() > 1)
      return r.substring(0,1).toUpperCase() + r.substring(1);
    else
      return r.toUpperCase();
  }
  
  public static String getRandomWord()
  {
    String r = wordList.get(random.nextInt(wordList.size()));
    return r;
  }

  /* Makes a directory with maxWidth folders that extend at most maxDepth
     layers deep.
     Method is recursive on maxDepth (decreases by at least 1, chosen at
     random).*/
  public static void buildDirTree(File root, int maxWidth, int maxDepth)
  {
    if(maxDepth > 0)
    {
      int w = random.nextInt(maxWidth) + 1;
      for(int i=0;i<w;i++)
      {
        makeDir(root.getPath() + ps + getRandomUppercaseWord());
      }

      File[] fList = root.listFiles();
      for(File f : fList)
        buildDirTree(f,maxWidth,random.nextInt(maxDepth));
    }
    else
    { 
      int fcap = random.nextInt(MAX_FILES);
      for(int i=0;i<fcap;i++)
      {
        gobbledegook(random.nextInt(MAX_CHARS_PER_FILE), root.getPath() + ps + "File" + i + ".txt");
      }
      
      leafDirs.add(new LeafDir(root.getPath(), fcap));
    }
  }

  // Create a directory with the specified name
  public static void makeDir(String dn)
  {
    (new File(dn)).mkdirs();
  }

  public static void gobbledegook(int chars, String fname)
  {
    try
    {
      PrintWriter out = new PrintWriter(new File(fname));
      for(int i=0; i<chars; i++)
      {
        out.print(randChar());
      }
      out.close();
    }
    catch(Exception e)
    {}
  }
  
  public static void gibberish(int words, String fname)
  {
    try
    {
      PrintWriter out = new PrintWriter(new File(fname));
      boolean capflag = true;
      for(int i=0; i<words; i++)
      {
        if(capflag)
        {
          out.print(getRandomUppercaseWord() + " ");
          capflag = false;
        }
        else
        {
          out.print(getRandomWord());
          int rand = random.nextInt(PUNCTUATION_FREQUENCY);
          if(rand == 0 || i==words-1)
          {
            double punc = random.nextDouble();
            if(punc < .5 || i==words-1)
            {
              out.print(". ");
              capflag = true;
            }
            else if(punc < .75)
            {
              out.print(", ");
            }
            else if(punc < .80)
            {
              out.print("! ");
              capflag = true;
            }
            else if(punc < .85)
            {
              out.print("? ");
              capflag = true;
            }
            else if(punc < .87)
            {
              out.print("; ");
            }
            else if(punc < .89)
            {
              out.print("-");
            }
            else if(punc < .98)
            {
              out.print(".\r\n   ");
              capflag = true;
            }
            else if(punc < .99)
            {
              out.print("?\r\n   ");
              capflag = true;
            }
            else
            {
              out.print("!\r\n   ");
              capflag = true;
            }
          }
          else
            out.print(" ");
        }
      }
      out.close();
    }
    catch(Exception e)
    {}
  }

  public static String randChar()
  {
    int r = (random.nextInt(96) + 32);
    if(r == 127)
      return "\r\n";
    else
      return ((char)r) + "";
  }
  
  public static BufferedImage randomBWImage(int width, int height)
  {
    BufferedImage ret = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    for(int i=0; i<height; i++)
    {  
      for(int j=0; j<width; j++)
      {
        int r = random.nextInt(2);
        if(r == 0)
          ret.setRGB(j,i,0xFFFFFFFF);
        else
          ret.setRGB(j,i,0xFF000000);
      }
    }
    return ret;
  }
  
  public static void generateRandomPNG(int width, int height, String filename)
  {
    try{ImageIO.write(randomBWImage(width,height),"png",new File(filename));}catch(Exception e){e.printStackTrace();}
  }
  
  /* Outputs a random black and white gif to a file */
  /*public static void generateRandomGIF(int width, int height, int frames, int delay, String filename)
  {
    AnimatedGifEncoder e = new AnimatedGifEncoder();
    e.start(filename);
    e.setDelay(delay);   // 1000/delay frames per sec
    for(int i=0; i<frames; i++)
    {
      e.addFrame(randomBWImage(width, height));
    }
    e.finish();
  }*/
  
  /* Generates a random wav file that probably plays static.
     Duration specified by duration in seconds.
     low and high are the lowest and highest possible frequencies in hz*/
  /*public static void generateRandomWAV(double duration, double low, double high, String filename)
  {
    try
    {
      int sampleRate = 44100;		// Samples per second
      
      // Calculate the number of frames required for specified duration
      long numFrames = (long)(duration * sampleRate);
      
      // Create a wav file with the name specified as the first argument
      WavFile wavFile = WavFile.newWavFile(new File(filename), 2, numFrames, 16, sampleRate);
      
      // Create a buffer of 100 frames
      double[][] buffer = new double[2][100];
      
      // Initialise a local frame counter
      long frameCounter = 0;
      
      double range = high-low;
      
      // Loop until all frames written
      while (frameCounter < numFrames)
      {
        // Determine how many frames to write, up to a maximum of the buffer size
        long remaining = wavFile.getFramesRemaining();
        int toWrite = (remaining > 100) ? 100 : (int) remaining;
        
        // Fill the buffer, one tone per channel
        for (int s=0 ; s<toWrite ; s++, frameCounter++)
        {
          buffer[0][s] = Math.sin(2.0 * Math.PI * (low+ random.nextDouble()*range) * frameCounter / sampleRate);
          buffer[1][s] = Math.sin(2.0 * Math.PI * (low+ random.nextDouble()*range) * frameCounter / sampleRate);
        }
        
        // Write the buffer
        wavFile.writeFrames(buffer, toWrite);
      }
      
      // Close the wavFile
      wavFile.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }*/
  
  /* Generates a random wav file that plays two notes simultaneously.
     Duration specified by duration in seconds. */
  /*public static void generateRandomTwoToneWAV(double duration, double low, double high, String filename)
  {
    try
    {
      int sampleRate = 44100;		// Samples per second
      
      // Calculate the number of frames required for specified duration
      long numFrames = (long)(duration * sampleRate);
      
      // Create a wav file with the name specified as the first argument
      WavFile wavFile = WavFile.newWavFile(new File(filename), 2, numFrames, 16, sampleRate);
      
      // Create a buffer of 100 frames
      double[][] buffer = new double[2][100];
      
      // Initialise a local frame counter
      long frameCounter = 0;
      
      double range = high-low;
      double firstTone = random.nextDouble()*range + low;
      double secondTone = random.nextDouble()*range + low;
      
      // Loop until all frames written
      while (frameCounter < numFrames)
      {
        // Determine how many frames to write, up to a maximum of the buffer size
        long remaining = wavFile.getFramesRemaining();
        int toWrite = (remaining > 100) ? 100 : (int) remaining;
        
        // Fill the buffer, one tone per channel
        for (int s=0 ; s<toWrite ; s++, frameCounter++)
        {
          buffer[0][s] = Math.sin(2.0 * Math.PI * (firstTone) * frameCounter / sampleRate);
          buffer[1][s] = Math.sin(2.0 * Math.PI * (secondTone) * frameCounter / sampleRate);
        }
        
        // Write the buffer
        wavFile.writeFrames(buffer, toWrite);
      }
      
      // Close the wavFile
      wavFile.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }*/
  
  public static void javac(String filename)
  {
    javac(filename, false);
  }
  
  public static void javac(String filename, boolean verbose)
  {
    while(!javacOneAttempt(filename,verbose))
    {
      System.out.println("Error while attempting to compile file " + filename + ".");
      System.out.println("Press enter to retry.");
      user.nextLine();
    }
  }
  
  /* Compiles a Java source code file, return true if it went through
     (whether or not the compilation was error-free). */
  public static boolean javacOneAttempt(String filename, boolean verbose)
  {
    if(verbose)
    {
      System.out.println("Executing command javac " + filename);
    }
    
    Scanner sc = null;
    boolean ret = true;
    
    try
    {
      Process p = Runtime.getRuntime().exec("javac " + filename);
      sc = new Scanner(p.getErrorStream());
      while(sc.hasNextLine())
      {
        String line = sc.nextLine();
        if(line.contains("error"))
        {
          ret = false;
        }
        if(verbose)
        {
          System.out.println(sc.nextLine());
        }
      }
      p.waitFor();
      sc.close();
    }
    catch(Exception e)
    {
      ret = false;
    }
    
    if(sc != null)
    {
      sc.close();
    }
    
    return ret;
  }
  
  public static void java(String classname, String[] args)
  {
    java(classname, args, false);
  }
  
  /* Runs a Java source code file */
  public static void java(String classname, String[] args, boolean verbose)
  {
    while(!javaOneAttempt(classname,args,verbose))
    {
      System.out.println("Error while attempting to run file " + classname + ".");
      System.out.println("Press enter to retry.");
      user.nextLine();
    }
  }
  
  /* Runs a Java source code file, return true if it went through. */
  public static boolean javaOneAttempt(String classname, String[] args, boolean verbose)
  {
    String runstr = "java " + classname;
    for(int i=0; i<args.length; i++)
    {
      runstr = runstr + " " + args[i];
    }
    if(verbose)
    {
      System.out.println("Executing command " + runstr);
    }
    
    Scanner sc = null;
    boolean ret = true;
    
    try
    {
      Process p = Runtime.getRuntime().exec(runstr);
      sc = new Scanner(p.getErrorStream());
      String line;
      while(sc.hasNextLine())
      {
        line = sc.nextLine();
        if(line.contains("Exception") || line.contains("error"))
        {
          ret = false;
        }
        if(verbose)
        {
          System.out.println(sc.nextLine());
        }
      }
      p.waitFor();
      sc.close();
    }
    catch(Exception e)
    {
      ret = false;
    }
    
    if(sc != null)
    {
      sc.close();
    }
    
    return ret;
  }
  
  public static void copy(String filename, String path)
  {
    copy(filename,filename,path);
  }
  
  public static void copy(String sourceName, String destName, String path)
  {
    while(!copyOneAttempt(sourceName,destName,path))
    {
      System.out.println("Error while attempting to copy file " + sourceName + " to " + destName + ".");
      System.out.println("Press enter to retry.");
      user.nextLine();
    }
  }
  
  public static boolean copyOneAttempt(String sourcename, String destname, String path)
  {
    try
    {
      Files.copy(new File(sourcename).toPath(), new File(path + ps + destname).toPath(), StandardCopyOption.REPLACE_EXISTING);
      return true;
    }
    catch(IOException e){return false;}
  }
  
  public static void delete(String filename)
  {
    while(!deleteOneAttempt(filename))
    {
      System.out.println("Error while attempting to delete file " + filename + ".");
      System.out.println("Press enter to retry.");
      user.nextLine();
    }
  }
  
  public static boolean deleteOneAttempt(String filename)
  {
    try
    {
      Files.delete(new File(filename).toPath());
      return true;
    }
    catch(IOException e){return false;}
  }
  
  public static void move(String origName, String newName, String path)
  {
    copy(origName, newName, path);
    delete(origName);
  }
  
  public static LeafDir pullLeafDir()
  {
    int dirNumToGet = random.nextInt(leafDirs.size());
    LeafDir ret = leafDirs.get(dirNumToGet);
    leafDirs.remove(dirNumToGet);
    return ret;
  }
  
  public static void printLinesToFile(String[] lines, String filename)
  {
    PrintWriter out = null;
    try
    {
      out = new PrintWriter(new File(filename));
      for(int i=0; i<lines.length; i++)
      {
        out.print(lines[i] + "\r\n");
      }
      out.close();
    }
    catch(IOException e)
    {
      System.out.println(e.getMessage());
    }
    if(out != null)
    {
      out.close();
    }
  }
  
  public static void encryptAndPrintLinesToFile(String[] lines, String filename, int offset)
  {
    String[] encLines = new String[lines.length];
    for(int i=0; i<lines.length; i++)
    {
      encLines[i] = encryptLine(lines[i], offset);
    }
    
    printLinesToFile(encLines,filename);
  }
  
  // Code mostly from Encrypter
  // Encrypt a line of text
  public static String encryptLine(String line, int offset)
  {
    StringBuilder enc = new StringBuilder();

    for(int i=0; i<line.length(); i++)
    {
      enc.append(encryptChar(line.charAt(i), offset));
    }
    
    return enc.toString();
  }

  // Encrypt a single character
  public static char encryptChar(int ori, int offset)
  {
    char r = (char) ori;
    if(r > 31 && r < 127)
    {
      r += offset;

      // Loop around so we don't go out of bounds with printables
      while(r < 32)
      {
        r+=95;
      }
      while(r > 126)
      {
        r-=95;
      }
    }
    return r;
  }

  public static int convertPass(String pass)
  {
    int r = 0;
    for(int i=0;i<pass.length();i++)
      r+=pass.charAt(i);

    while(r>94)
      r-=95;

    return r;
  }
 
  // Includes encryption on Strings to avoid class reading hack
  public static void writeJavaFile(String jf, String filename, int offset)
  {
   String[] temp;
   
   String[] filepath = filename.split(ps);
   String pkg = "";
   
   if(filepath.length > 1)
   { 
     pkg = filepath[0];
     for(int i=1; i < filepath.length - 1; i++)
     {
       pkg = pkg + "." + filepath[i];
     }
   }
   
   if(offset != 0 && jf.length() > 0)
   {
    StringBuilder encryptedJF = new StringBuilder();
    char c = 'a';
    char prev = 'a';
    boolean inString = false;
    
    // find the last } so we can insert extra methods
    int endOfFile = jf.length() - 1;
    while(jf.charAt(endOfFile) != '}' && endOfFile > 0)
    {
      endOfFile--;
    }
    for(int i=0; i<endOfFile; i++)
    {
      prev = c;
      c = jf.charAt(i);
      if(c == '"' && prev != '\\')
      {
        if(inString || prev != '\'')
        {
          inString = !inString;
          if(!inString)
          {
            encryptedJF.append(c + ")");
          }
          else
          {
            encryptedJF.append("clarify(" + c);
          }
        }
      }
      else
      {
        if(inString)
        {
          if(c == '\\')
          {
            i++;
            c = jf.charAt(i);
          }
          char ec = encryptChar(c,offset);
          if(ec == '\\' || ec == '"')
          {
            encryptedJF.append('\\');
          }
          encryptedJF.append(ec);
        }
        else
        {
          encryptedJF.append(c);
        }
      }
    }
    
    // extra methods for encryption
    encryptedJF.append(" public static String clarify(String text)   {     String dec = \"\";     char c;     for(int i=0; i<text.length(); i++)     {       c = decryptChar(text.charAt(i), " + (-offset) + ");       dec += c;     }     return dec;   }       public static char decryptChar(int ori, int offset)   {     char r = (char) ori;     if(r > 31 && r < 127)     {       r += offset;               while(r < 32)       {         r+=95;       }       while(r > 126)       {         r-=95;       }     }     return r;   } }");
  
    if(pkg.equals(""))
    {
      temp = new String[]{encryptedJF.toString()};
    }
    else
    {
      temp = new String[]{"package " + pkg + ";",encryptedJF.toString()};
    }
   }
   else
   {
    if(pkg.equals(""))
    {
      temp = new String[]{jf};
    }
    else
    {
      temp = new String[]{"package " + pkg + ";",jf};
    }
   }
   
   printLinesToFile(temp,filename);
  }
  
  public static void compileAndInsertClass(String classname, String body, String path, int fileNum)
  {
    String sourcename = classname + ".java";
    String compname = classname + ".class";
    writeJavaFile(body, sourcename, CLASS_STRING_DFO);
    javac(sourcename);
    copy(compname, "File" + fileNum + ".txt", path);
    delete(sourcename);
    delete(compname);
  }
  
  public static void createAndInsertEncryptedSource(String classname, String body, String path, int fileNum, int offset)
  {
    String sourcename = classname + ".java";
    encryptAndPrintLinesToFile(body.split("NEWLINE"), sourcename, offset);
    copy(sourcename, "File" + fileNum + ".txt", path);
    delete(sourcename);
  }
  
  public static int[] pickFileNums(int filesToPick, LeafDir dir)
  {
    // pick files in range if we can, or extend range if needed
    return pickXUniqueRandomNumbersInRange(filesToPick, Math.max(filesToPick,dir.getNumFiles()));
  }
  
  public static int[] pickXUniqueRandomNumbersInRange(int x, int range)
  {
    int[] ret = new int[x];
    int[] temp = new int[range];
    for(int i=0; i<range; i++)
    {
      temp[i] = i;
    }
    
    int choice;
    int rmi;
    for(int i=0; i<x; i++)
    {
      rmi = range-i;
      choice = random.nextInt(rmi);
      ret[i] = temp[choice];
      temp[choice] = temp[rmi-1];
    }
    return ret;
  }
  
  // Convert a String to a series of 8-bit ASCII representations
  // separated by spaces
  public static String decToBinChar(int dec)
  {
    int rem = dec;
    StringBuilder ret = new StringBuilder("0");
    for(int i=64; i>0; i/=2)
    {
      if(rem >= i)
      {
        rem = rem - i;
        ret.append('1');
      }
      else
      {
        ret.append('0');
      }
    }
    return ret.toString();
  }
  
  public static String strToBin(String s)
  {
    if(s.length() == 0)
    {
      return s;
    }
    
    String ret = decToBinChar(s.charAt(0));
    
    for(int i=1; i<s.length(); i++)
    {
      ret = ret + " " + decToBinChar(s.charAt(i));
    }
    return ret;
  }
  
  
  
  
  
  
  // SCRIPT FROM HERE DOWN
  
  
  
  public static String rootName = "Start";
  
  public static LeafDir asciiInterludeDir;
  public static int asciiInterludeFileNum;
  
  public static LeafDir firstInstructionsDir;
  public static int firstInstructionsFileNum;
  public static int firstInstructionsTargetNum;
  
  public static LeafDir nextStepDir;
  public static int nextStepFileNum;
  
  public static LeafDir secretMethodDir;
  public static int secretMethodFileNum;
  
  public static LeafDir gatemasterDir;
  public static LeafDir gatemasterDir2;
  public static LeafDir gatemasterDir3;
  public static int gatemasterFileNum;
  public static int gatemasterDirectionsFileNum;
  public static int gatemasterCode1FileNum;
  public static int gatemasterCode2FileNum;
  public static int gatemasterCode3FileNum;
  
  public static LeafDir snitchDir;
  public static int snitchFileNum;
  public static LeafDir snitchCousinDir;
  public static int snitchCousinFileNum;
  public static int gatemasterKey2FileNum;
  public static int gatemasterKey3FileNum;
  
  public static LeafDir revealerDir;
  public static int revealerFileNum;
  public static int secretClassFileNum;
  public static int guruFileNum;
  //public static int zeldaSongFileNum;
  public static int gifEncoderFileNum;
  public static int lzwEncoderFileNum;
  public static int neuQuantFileNum;
  public static int wavFileFileNum;
  public static int wavFileIOStateFileNum;
  public static int wavFileExceptionFileNum;
  
  public static LeafDir wordListDir;
  public static int wordListFileNum;
  
  public static LeafDir passcode2Dir;
  public static LeafDir passcode3Dir;
  public static LeafDir passcode4Dir;
  public static int passcode2FileNum;
  public static int mysteryArrayFileNum;
  public static int passcode3FileNum;
  public static int mysteryArray2FileNum;
  public static int passcode4FileNum;
  public static int impossiblePassFileNum;
  
  public static LeafDir selfDestructDir;
  public static int selfDestructFileNum;
  public static int sdAnonFileNum;

  public static void addSpecialFiles()
  {
    // set up constants (maybe move this to another method)
  
    asciiInterludeDir = pullLeafDir();
    asciiInterludeFileNum = pickFileNums(1, asciiInterludeDir)[0];
  
    firstInstructionsDir = pullLeafDir();
    int[] fiar = pickFileNums(2,firstInstructionsDir);
    firstInstructionsFileNum = fiar[0];
    firstInstructionsTargetNum = fiar[1];
    
    nextStepDir = pullLeafDir();
    int[] nsar = pickFileNums(1, nextStepDir);
    nextStepFileNum = nsar[0];
    
    secretMethodDir = pullLeafDir();
    int[] smar = pickFileNums(1, secretMethodDir);
    secretMethodFileNum = smar[0];
    
    gatemasterDir = pullLeafDir();
    int[] gateAr = pickFileNums(3, gatemasterDir);
    gatemasterFileNum = gateAr[0];
    gatemasterDirectionsFileNum = gateAr[1];
    gatemasterCode1FileNum = gateAr[2];
    
    gatemasterDir2 = pullLeafDir();
    int[] gateAr2 = pickFileNums(1, gatemasterDir2);
    gatemasterCode2FileNum = gateAr2[0];
    
    gatemasterDir3 = pullLeafDir();
    int[] gateAr3 = pickFileNums(1, gatemasterDir3);
    gatemasterCode3FileNum = gateAr3[0];
    
    snitchDir = pullLeafDir();
    int[] snitar = pickFileNums(1, snitchDir);
    snitchFileNum = snitar[0];
    
    snitchCousinDir = pullLeafDir();
    int[] scAr = pickFileNums(3, snitchCousinDir);
    snitchCousinFileNum = scAr[0];
    gatemasterKey2FileNum = scAr[1];
    gatemasterKey3FileNum = scAr[2];
    
    revealerDir = pullLeafDir();
    int[] revar = pickFileNums(10, revealerDir);
    revealerFileNum = revar[0];
    secretClassFileNum = revar[1];
    guruFileNum = revar[2];
    //zeldaSongFileNum = revar[3];
    gifEncoderFileNum = revar[4];
    lzwEncoderFileNum = revar[5];
    neuQuantFileNum = revar[6];
    wavFileFileNum = revar[7];
    wavFileIOStateFileNum = revar[8];
    wavFileExceptionFileNum = revar[9];
    
    wordListDir = pullLeafDir();
    int[] wlar = pickFileNums(1, wordListDir);
    wordListFileNum = wlar[0];
    
    passcode2Dir = pullLeafDir();
    int[] pcar2 = pickFileNums(2, passcode2Dir);
    passcode2FileNum = pcar2[0];
    mysteryArrayFileNum = pcar2[1];
    
    passcode3Dir = pullLeafDir();
    int[] pcar3 = pickFileNums(2, passcode3Dir);
    passcode3FileNum = pcar3[0];
    mysteryArray2FileNum = pcar3[1];
    
    passcode4Dir = pullLeafDir();
    int[] pcar4 = pickFileNums(2, passcode4Dir);
    passcode4FileNum = pcar4[0];
    impossiblePassFileNum = pcar4[1];
    
    selfDestructDir = pullLeafDir();
    int[] sdar = pickFileNums(2, selfDestructDir);
    selfDestructFileNum = sdar[0];
    sdAnonFileNum = sdar[1];
            
    createREADME();
    createSuperSecretFile();
    createAsciiInterlude();
    createFirstInstructions();
    createNextStep();
    createSecretMethod();
    createGatemaster();
    createSnitch();
    createRevealer();
    createPasscodeGuru();
    createPasscodeChallenges();
    createSecretClass();
    createSelfDestruct();
    
    copy("Decrypter.class", rootName);
    copy("wordsEn.txt", "File" + wordListFileNum + ".txt", wordListDir.getPath());
  }
  
  public static void createREADME()
  {
    String[] readme = new String[]{
      "Hello!",
      "",
      "See that suspicious looking file named \"supersecretfile.txt\"? Why don't we try running the ol' Decrypter on it?",
      "",
      "Try navigating to this directory in the command line or Terminal and entering the command \"java Decrypter supersecretfile.txt\" (omit quotes) to get started!",
      "(Remember, you can navigate through the command line by using the cd command.)"};
    printLinesToFile(readme, rootName + ps + "README.txt");
  }
  
  public static void createSuperSecretFile()
  {
    String[] sss = new String[]{
      "Comrade! We need your help. David has been kidnapped and replaced by an evil impostor!",
      "",
      "We believe it was the work of our rival, the evil Youth Torture Center run by Hons-Jhopkins University. As our sworn enemies, we know they would stop at nothing to sabotage us and further their own dastardly ends, even if that would mean absconding with one of our most valued teachers.",
      "",
      "If this is the case, then the key to saving David lies somewhere in this directory, a veritable labyrinth of folders and files with a peculiar naming scheme and even stranger contents. We've managed to hack partway in, and have left help for you where we can, but we need your help to finish the job!",
      "",
      "Your mission, should you choose to accept it, is to maneuver through the increasingly difficult layers of security in this directory in order to get to the secrets locked within. The ultimate goal is to access one of the admin user accounts, from where we can initiate the self-destruct sequence. This will be no easy task - you must use your wits, the tools provided, and the knowledge you have gained in this course so far in order to crack the code!",
      "",
      "To begin, navigate to the " + asciiInterludeDir.getPath() + " directory and open File" + asciiInterludeFileNum + ".txt, where we've smuggled in your first set of instructions. hopefully you'll be able to ASCertaIn the meanIng.",
      ""};
    encryptAndPrintLinesToFile(sss, rootName + ps + "supersecretfile.txt", DFO);
  }
  
  public static void createAsciiInterlude()
  {
    String[] ai = new String[]{
      strToBin(firstInstructionsDir.getPath() + ps + "File" + firstInstructionsFileNum)};
    printLinesToFile(ai, asciiInterludeDir.getPath() + ps + "File" + asciiInterludeFileNum + ".txt");
  }
  
  public static void createFirstInstructions()
  {
    String[] fi = new String[]{
      "It looks like you chose to accept the mission! Splendid. There may be hope of saving David yet.",
      "",
      "Alright, so for your first task, I hope you brought your Decrypter! The File" + firstInstructionsTargetNum + ".txt in this folder looks like gibberish, but it's actually a secret message."
    };
    printLinesToFile(fi, firstInstructionsDir.getPath() + ps + "File" + firstInstructionsFileNum + ".txt");
    
    
    String[] fit = new String[]{
      "You're really getting the hang of this!",
      "",
      "Before you receive your next task, you'll need a bit of background. You know the part at the end of almost every file, the one that says \".txt\" or \".jpg\" or \".java\" or \".class\"? That doesn't actually mean anything special - it's just a code that tells your computer (and you) what program to open the file with. But, as you may have seen previously if you've renamed .txt files into .java files, the actual contents could be anything!",
      "",
      "Hons-Jhopkins is a crafty lot - they don't just leave their programs out in the open where anyone can see them. No, they've disguised them as text files, and hidden them throughout. For example, did you notice that " + nextStepDir.getPath() + ps + "File" + nextStepFileNum + ".txt is actually a .class file? How about you try renaming it as \"NextStep.class\" and running it?"
    };
    encryptAndPrintLinesToFile(fit, firstInstructionsDir.getPath() + ps + "File" + firstInstructionsTargetNum + ".txt",DFO);
  }
  
  public static void createNextStep()
  {
    String ns = "public class NextStep {   public static void main(String[] args)   {     System.out.println(\"Great job! You'll be in the core in no time.\");     System.out.println(\"What you just did is great, but sometimes just running the main method isn't enough. In that case, we need to be a bit sneakier. To see what I mean, navigate to " + secretMethodDir.getPath() + ", rename File" + secretMethodFileNum + ".txt to \\\"SecretMethod.class\\\" and try running it.\");   } } ";
    compileAndInsertClass("NextStep", ns, nextStepDir.getPath(), nextStepFileNum);
  }
  
  public static void createSecretMethod()
  {
    String[] snitchSpecial = snitchDir.getPath().split(ps);
    
    String snitchSpecial1 = snitchSpecial[1];
    String snitchSpecial2 = "";
    if(snitchSpecial.length < 4)
    {
      for(int i=2; i< snitchSpecial.length; i++)
      {
        snitchSpecial1 += " " + snitchSpecial[i];
      }
      snitchSpecial2 = "hot spots";
    }
    else
    {
      for(int i=2; i<= snitchSpecial.length/2; i++)
      {
        snitchSpecial1 += " " + snitchSpecial[i];
      }
      snitchSpecial2 = snitchSpecial[snitchSpecial.length/2 + 1];
      for(int i=snitchSpecial.length/2 + 2; i< snitchSpecial.length; i++)
      {
        snitchSpecial2 += " " + snitchSpecial[i];
      }
    }
    String sm = "public class SecretMethod {   public static void main(String[] args)   {     System.out.println(\"HAHAHA! Fool, running this main method will do you no good.\");     System.out.println();     System.out.println(\"If only you could somehow call the static getNextClue() method contained in this file, you might have a chance. Fwahahahahahaha!\");   }    public static void getNextClue()   {     System.out.println(\"Curses! I was sure you would never be able to figure out how to access my secret method. I guess I'll have to tell you something.\");     System.out.println();     System.out.println(\"*grumble grumble*\");     System.out.println();    System.out.println(\"I don't actually know the way to the user directories, but I hear the Snitch knows something. He likes to hang out at the " + snitchSpecial1 + "; I believe he's currently masquerading as a .txt File down by the " + snitchSpecial2 + ". Renaming him as Snitch.class should root him out. But, I don't know which number to find him at - only the GateMaster would know. Check " + gatemasterDir.getPath() + ps + "File" + gatemasterDirectionsFileNum + ".txt for information on the GateMaster.\");     System.out.println();    System.out.println(\"But that's all you'll get from me. Now get off my lawn!\");   } } ";
    compileAndInsertClass("SecretMethod", sm, secretMethodDir.getPath(), secretMethodFileNum);
  }
  
  public static void createGatemaster()
  {
    String gm = "import javax.swing.JFrame; import javax.swing.JPanel;   import java.awt.Graphics; import java.awt.Color; import java.awt.Font; import java.awt.FontMetrics;   import java.awt.event.MouseEvent; import java.awt.event.MouseListener;  import java.io.File; import java.util.Scanner;  public class GateMaster3000 extends JPanel implements MouseListener {   static final int FPS = 1;    static final int MSPF = 1000/FPS;       static final int NONE = 0;   static final int NOT = 1;   static final int AND = 2;   static final int OR = 3;   static final int XOR = 4;   static final int NAND = 5;   static final int NOR = 6;   static final int XNOR = 7;   static final int INPUT = 8;   static final int OUTPUT = 9;      static final int XCOORD = 0;   static final int YCOORD = 1;   static final int ID = 2;      boolean go = true;         int screenWidth;   int screenHeight;      int[][] gates;   int[] outputCoords;   int[][] connections;   boolean[][] targetOutput = new boolean[][]{   {},   {true,false,true,true},   {false, false, false, true, false, true, true, false},   {false, false, false, true, false, true, true, false}};      int[][] backrefs;   int outputBackref;   boolean[] powered;   boolean checking = false;   int numInputs;      int selected = -1;      int[][] gateSelectors;      int gateSize = 50;   int outputSize = 150;   int inputSpacing = 10;   int fontSize = 24;   Color borderColor = Color.BLACK;   Color selectionColor = new Color(0,255,255);   Color outputBoxColor = new Color(64, 64, 64);   Color outputLitColor = new Color(255, 255, 0);   Color[] gateColors = new Color[]{           new Color(0,0,0,0),           new Color(255,0,0),           new Color(0,0,255),           new Color(0,255,0),           new Color(128,128,0),           new Color(255,0,255),           new Color(128,128,128),           new Color(255,128,0)};                              Color[] wireColors = new Color[]{           new Color(0,0,0),           new Color(255,0,0),           new Color(0,255,0),           new Color(0,0,255),           new Color(255,255,0),           new Color(255,0,255),           new Color(0,255,255),           new Color(64,64,64),           new Color(128,0,0),           new Color(0,128,0),           new Color(0,0,128),           new Color(128,128,0),           new Color(128,0,128),           new Color(0,128,128)   };                              int checkButtonX = 640;   int checkButtonY = 50;   int checkButtonW = 140;   int checkButtonH = 60;   int errorTimer = 0;      int lockX = 640;   int lockY = 450;   int lockW = 120;   int lockH = 80;      private boolean cracked = false;   private int lockID = 0;   long[] hashes = new long[]{-1, 14191, 55198, 39351};   String[] info = new String[]{\"Nothing was inside!\", \"Snitch's address: File " + snitchFileNum + ".txt\", \"Lock 3 located at " + gatemasterDir3.getPath() + ", File " + gatemasterCode3FileNum + "\", \"Passcode Piece: nt your c\"};    public GateMaster3000(String filename, int w, int h)   {     screenWidth = w;     screenHeight = h;          Scanner in = null;     long hash = 0;     try     {       in = new Scanner(new File(filename));              numInputs = in.nextInt();       gates = new int[numInputs + in.nextInt()][];       connections = new int[gates.length][];                     for(int i=0; i<numInputs; i++)       {         gates[i] = new int[]{in.nextInt(), in.nextInt(), INPUT};       }       for(int i=numInputs; i<gates.length; i++)       {         gates[i] = new int[]{in.nextInt(), in.nextInt(), NONE};       }       for(int i=0; i<connections.length; i++)       {         connections[i] = new int[in.nextInt()*2];         for(int j=0; j<connections[i].length; j++)         {           hash += (connections[i][j] = in.nextInt())*7;         }       }              hash += numInputs + gates.length;       for(int i=0; i<gates.length; i++)       {         hash += (i+1)*gates[i][XCOORD] + (i+2)*gates[i][YCOORD]*gates[i][ID];       }              if(hash != in.nextInt())       {         loadError();       }              for(int i=0; i<hashes.length; i++)       {         if(hash == hashes[i])         {           lockID = i;           break;         }       }              powered = new boolean[gates.length];                     int[] numInputs = new int[gates.length];       backrefs = new int[gates.length][];              for(int i=0; i<connections.length; i++)       {         for(int c = 0; c<connections[i].length; c+=2)         {           if(connections[i][c] > -1)           {             numInputs[connections[i][c]]++;           }         }       }       for(int i=0; i<gates.length; i++)       {         backrefs[i] = new int[numInputs[i]];         numInputs[i] = 0;       }       for(int i=0; i<connections.length; i++)       {         for(int c = 0; c<connections[i].length; c+=2)         {           if(connections[i][c] > -1)           {             backrefs[connections[i][c]][numInputs[connections[i][c]]] = i;             numInputs[connections[i][c]]++;           }           else           {             outputBackref = i;           }         }       }                 outputCoords = new int[]{600, 200};                     gateSelectors = new int[8][2];       for(int i=0; i<8; i++)       {         gateSelectors[i][XCOORD] = 200 + i*gateSize;         gateSelectors[i][YCOORD] = 500;       }              setFocusable(true);       addMouseListener(this);     }     catch(Exception e)     {       e.printStackTrace();       loadError();       if(in != null)       {         in.close();       }       System.exit(0);     }   }      public void loadError()   {     System.out.print(\"Error: Invalid lock (.eil) file. Shutting down\");     try{Thread.sleep(500);}catch(Exception e){}     System.out.print(\".\");     try{Thread.sleep(500);}catch(Exception e){}     System.out.print(\".\");     try{Thread.sleep(500);}catch(Exception e){}     System.out.println(\".\");     try{Thread.sleep(500);}catch(Exception e){}     System.out.print(\"Shutdown complete. Goodbye!\");     System.exit(0);   }      public void paintComponent(Graphics g)   {     super.paintComponent(g);          int gd2 = gateSize/2;               Font f = new Font(\"Arial\", Font.BOLD, fontSize);     g.setFont(f);     FontMetrics fm = g.getFontMetrics(f);          String td;     for(int i=0; i<gates.length; i++)     {       if(gates[i][ID] == INPUT)       {         if(checking)         {           if(powered[i])           {             g.setColor(outputLitColor);             g.fillOval(gates[i][XCOORD], gates[i][YCOORD], gateSize, gateSize);             g.setColor(Color.BLACK);             g.drawOval(gates[i][XCOORD], gates[i][YCOORD], gateSize, gateSize);           }           else           {             g.setColor(Color.BLACK);             g.fillOval(gates[i][XCOORD], gates[i][YCOORD], gateSize, gateSize);             g.setColor(Color.WHITE);           }         }         else         {           g.setColor(Color.BLACK);         }         td = \"\" + (char)('P' + i);         g.drawString(td, gates[i][XCOORD] + gd2 - fm.stringWidth(td)/2, gates[i][YCOORD] + (gateSize + fontSize*4/5)/2);       }       else       {         drawGate(g, gates[i][XCOORD], gates[i][YCOORD], gates[i][ID]);       }     }               if(powered[outputBackref])     {       g.setColor(outputLitColor);     }     else     {       g.setColor(outputBoxColor);     }     g.fillRect(outputCoords[0], outputCoords[1], outputSize, outputSize);     if(powered[outputBackref])     {       g.setColor(Color.BLACK);     }     else     {       g.setColor(Color.WHITE);     }     g.drawString(\"OUTPUT\", outputCoords[0] + (outputSize - fm.stringWidth(\"OUTPUT\"))/2, outputCoords[1] + (outputSize + fontSize)/2);          g.setColor(Color.BLACK);          int x1,x2,y1,y2;     int curWire = 0;          for(int i=0; i<connections.length; i++)     {       for(int j = 0; j < connections[i].length; j+=2)       {         x1 = gates[i][XCOORD] + gateSize;         y1 = gates[i][YCOORD] + gd2;         if(connections[i][j] > -1)         {           x2 = gates[connections[i][j]][XCOORD];           y2 = gates[connections[i][j]][YCOORD] + gd2 + inputSpacing*connections[i][j+1];         }         else         {           x2 = outputCoords[0];           y2 = outputCoords[1] + outputSize/2;         }                  g.setColor(wireColors[curWire]);                  int midX = (x1 + x2*4)/5;         g.drawLine(x1, y1, midX, y1);         g.drawLine(midX, y1, midX, y2);         g.drawLine(midX, y2, x2, y2);       }       curWire++;       curWire%=wireColors.length;     }               if(selected > -1)     {       g.setColor(selectionColor);       g.drawRect(gates[selected][XCOORD] - 1, gates[selected][YCOORD] - 1, gateSize + 2, gateSize + 2);       g.drawRect(gates[selected][XCOORD] - 2, gates[selected][YCOORD] - 2, gateSize + 4, gateSize + 4);     }               for(int i=0; i<gateSelectors.length; i++)     {       drawGate(g, gateSelectors[i][XCOORD], gateSelectors[i][YCOORD], i);     }     g.setColor(Color.BLACK);     g.drawString(\"Gate Selector:\", 10, gateSelectors[0][YCOORD] + fontSize);               if(checking)     {       g.setColor(Color.GRAY);     }     else     {       g.setColor(Color.RED);     }     g.fillRect(checkButtonX, checkButtonY, checkButtonW, checkButtonH);     g.setColor(Color.BLACK);     g.drawRect(checkButtonX, checkButtonY, checkButtonW, checkButtonH);     g.drawString(\"EXECUTE\", checkButtonX + (checkButtonW - fm.stringWidth(\"EXECUTE\"))/2, checkButtonY + (checkButtonH + fontSize*4/5)/2);          g.drawRect(lockX, lockY, lockW, lockH);     g.drawArc(lockX + 20, lockY - 40, lockW - 40, 80, cracked ? 30 : 0, cracked ? 150 : 180);     g.drawArc(lockX + 30, lockY - 30, lockW - 60, 60, cracked ? 30 : 0, cracked ? 150 : 180);               if(errorTimer > 0)     {       g.setColor(new Color(0, 0, 0, 64));       g.fillRect(0,0,screenWidth,screenHeight);       g.setColor(new Color(255,0,0));       g.setFont(new Font(\"Arial\", Font.PLAIN, 200));       g.drawString(\"ERROR\", 50, 350);     }   }      public void drawGate(Graphics g, int x, int y, int id)   {     g.setColor(borderColor);     g.drawRect(x, y, gateSize, gateSize);     g.setColor(gateColors[id]);     switch(id)     {      case NOT:       g.fillPolygon(new int[]{x + 1, x + 1, x + gateSize - 2}, new int[]{y + 1, y + gateSize - 2, y + (gateSize/2)}, 3);       break;      default:       g.fillRect(x + 1, y + 1, gateSize - 2, gateSize - 2);     }   }      public void mainLoop()   {     long nextFrame = System.currentTimeMillis();     int curCheck = -1;     int maxCheck = 1;     for(int i=0; i<numInputs; i++)     {       maxCheck *= 2;     }          boolean correct = true;          while(go)     {       if(errorTimer > 0)       {         errorTimer--;       }       else if(checking)       {         curCheck++;         if(curCheck < maxCheck)         {           for(int i=0; i<numInputs; i++)           {             powered[i] = ((curCheck >> i)&1) > 0 ? true:false;           }           for(int i=numInputs; i<gates.length; i++)           {             switch(gates[i][ID])             {              case NONE: powered[i] = powered[backrefs[i][0]]; break;              case NOT: powered[i] = !powered[backrefs[i][0]]; break;              case AND: powered[i] = powered[backrefs[i][0]] && powered[backrefs[i][1]]; break;              case OR: powered[i] = powered[backrefs[i][0]] || powered[backrefs[i][1]]; break;              case XOR: powered[i] = (powered[backrefs[i][0]] && !powered[backrefs[i][1]]) || (!powered[backrefs[i][0]] && powered[backrefs[i][1]]); break;              case NAND: powered[i] = !(powered[backrefs[i][0]] && powered[backrefs[i][1]]); break;              case NOR: powered[i] = !(powered[backrefs[i][0]] || powered[backrefs[i][1]]); break;              case XNOR: powered[i] = (powered[backrefs[i][0]] && powered[backrefs[i][1]]) || (!powered[backrefs[i][0]] && !powered[backrefs[i][1]]); break;             }           }           if(powered[outputBackref] != targetOutput[lockID][curCheck])           {             correct = false;           }         }         else         {           if(correct)           {             cracked = true;             System.out.println(info[lockID]);             repaint();             try{Thread.sleep(2000);}catch(Exception e){}             System.exit(0);           }           curCheck = -1;           checking = false;           for(int i=0; i<gates.length; i++)           {             powered[i] = false;           }           correct = true;         }       }       nextFrame += MSPF;       repaint();              long curTime = System.currentTimeMillis();       if(curTime < nextFrame)       {         try         {           Thread.sleep(nextFrame - curTime);         }         catch(Exception e){e.printStackTrace();}       }     }          System.exit(0);   }      public void mousePressed(MouseEvent e)   {   }   public void mouseReleased(MouseEvent e)   {     if(!checking)     {       int x = e.getX();       int y = e.getY();              boolean registered = false;              for(int i=0; i<gates.length; i++)       {         if(gates[i][ID] < INPUT)         {           if(x >= gates[i][XCOORD] && y >= gates[i][YCOORD] && x < gates[i][XCOORD] + gateSize && y < gates[i][YCOORD] + gateSize)           {             selected = i;             registered = true;             break;           }         }       }              if(!registered && x >= checkButtonX && x < checkButtonX + checkButtonW && y >= checkButtonY && y < checkButtonY + checkButtonH)       {         registered = true;         selected = -1;                  boolean good = true;                  for(int i=numInputs; i<gates.length; i++)         {           if((gates[i][ID] < 2 && backrefs[i].length > 1) || (gates[i][ID] >= 2 && backrefs[i].length < 2))           {             good = false;             break;           }         }                  if(good)         {           checking = true;         }         else         {           errorTimer = 2;         }       }              if(!registered && selected > -1)       {         if(y >= gateSelectors[0][YCOORD] && y < gateSelectors[0][YCOORD] + gateSize)         {           int ind = (x - gateSelectors[0][XCOORD])/gateSize;           if(ind > -1 && ind < 8)           {             gates[selected][ID] = ind;             registered = true;           }         }         if(!registered)         {           selected = -1;         }       }              repaint();     }   }   public void mouseClicked(MouseEvent e)   {   }   public void mouseEntered(MouseEvent e)   {   }   public void mouseExited(MouseEvent e)   {   }      public static void main(String[] args)   {     int w = 800;     int h = 600;     JFrame frame = new JFrame();     frame.setSize(w,h);     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);          if(args.length > 0)     {       GateMaster3000 panel = new GateMaster3000(args[0], w,h);            frame.add(panel);       frame.setVisible(true);              panel.mainLoop();     }     else     {       System.out.println(\"Error: Missing input file name\");     }   } } ";
    compileAndInsertClass("GateMaster3000", gm, gatemasterDir.getPath(), gatemasterFileNum);
    String[] gmI = new String[]{
      "USER MANUAL FOR YTC GATEMASTER 3000",
      "",
      "Installation:",
      " Simply rename File" + gatemasterFileNum + ".txt to GateMaster3000.class.",
      "",
      " Run the program from the command line with a single command line argument,",
      " the filename of the Electronic Information Lock file to be opened.",
      "",
      "  (handwritten scrawl) first lock in file " + gatemasterCode1FileNum,
      "  (handwritten scrawl) first key is T F T T",
      "",
      "Operation:",
      " Click a gate slot in the circuit to select it.",
      "",
      " Once a gate slot has been selected, click a gate from the Gate Selector",
      " interface to insert that gate into the selected slot.",
      "",
      " Refer to the KEY that matches your Electronic Information Lock to help you",
      " figure out which gates to insert into which slots.",
      "",
      " When an appropriate circuit has been constructed, press the EXECUTE",
      " button to attempt to open the electronic information lock. The GateMaster",
      " 3000 (TM) will run through all possible combinations of inputs, and will verify",
      " that the outputs produced in each case match the KEY.",
      "",
      "IMPORTANT NOTE: Make sure the KEY does not fall into CTY hands! CTY agents",
      "are probably too foolish to figure out how to use the KEY to construct an",
      "appropriate circuit, but we cannot take any chances if we hope to succeed.",
      "",
      "",
      "Troubleshooting guide:",
      " If an ERROR message pops up after pressing EXECUTE, then an invalid",
      " circuit has been constructed. Be careful to make sure all gates have the",
      " correct number of inputs!",
      "",
      " If no ERROR message pops up but the electronic information lock does not",
      " open, then a valid circuit has been constructed, but it does not compute",
      " the correct logical function (the correct sequence of True and False",
      " outputs) as specified by the KEY. You will need to try again!"
    };
    printLinesToFile(gmI, gatemasterDir.getPath() + ps + "File" + gatemasterDirectionsFileNum + ".txt");
    
    String[] gmc1 = new String[]{"2 3 50 150 50 350 200 150 200 350 350 250 1 2 0 1 3 0 1 4 -1 1 4 1 1 -1 0 14191"};
    String[] gmc2 = new String[]{"3 11 20 100 40 250 60 400 150 50 150 200 150 320 250 125 250 375 250 250 345 300 350 100 355 200 450 150 550 250 3 3 0 6 -1 8 -1 3 4 0 6 1 7 -1 3 5 0 7 1 8 1 1 10 -1 1 11 -1 1 9 1 1 9 -1 1 10 1 1 11 1 1 12 1 1 12 -1 1 13 1 1 13 -1 1 -1 0 55198"};
    String[] gmc3 = new String[]{"3 8 20 100 40 250 60 400 170 150 150 250 180 400 250 200 270 350 350 150 350 300 450 250 2 3 0 8 -1 1 4 0 1 5 0 1 9 -1 2 6 -1 7 -1 2 6 1 7 1 1 8 1 1 9 1 1 10 -1 1 10 1 1 -1 0 39351"};
    printLinesToFile(gmc1, gatemasterDir.getPath() + ps + "File" + gatemasterCode1FileNum + ".txt");
    printLinesToFile(gmc2, gatemasterDir2.getPath() + ps + "File" + gatemasterCode2FileNum + ".txt");
    printLinesToFile(gmc3, gatemasterDir3.getPath() + ps + "File" + gatemasterCode3FileNum + ".txt");
  }
  
  public static void createSnitch()
  {
    String[] stsTemp = revealerDir.getPath().split(ps);
    
    String snitchTalkSpecial = stsTemp[1];
    for(int i=2; i<stsTemp.length - 1; i++)
    {
      snitchTalkSpecial += " " + stsTemp[i];
    }
    snitchTalkSpecial += ", " + stsTemp[stsTemp.length-1] + " " + revealerFileNum;
    
    String[] sts2Temp = snitchCousinDir.getPath().split(ps);
    String snitchCousinAddress = sts2Temp[1];
    for(int i=2; i<sts2Temp.length - 1; i++)
    {
      snitchCousinAddress += " " + sts2Temp[i];
    }
    snitchCousinAddress += ", " + sts2Temp[sts2Temp.length-1] + " " + snitchCousinFileNum;
    
    String sn = "public class Snitch {    public static void main(String[] args)   {     System.out.println(\"The Snitch appears occupied. To get his attention, you'll need to write a program! First, create and instantiate a new Snitch variable, then you can use his \\\"talk()\\\" method.\");   }    public void talk()   {     System.out.println(\"The user directories? Yea, I might know somethin' about that. Maybe you could help me jog my memory? Maybe a little \\\"bribe(int amount)\\\" call, if you know what I mean?\");   }    public void bribe(int amount)   {     if(amount < 5)       System.out.println(\"Seriously? $\" + amount + \"? C'mon, that's just offensive.\");     else if(amount < 20)       System.out.println(\"Dont'cha think that's a bit low?\");     else if (amount < 100)     {       System.out.println(\"Now that's what I'm talking about! Alright, I'll talk, listen up and maybe you'll hear something useful.\");       System.out.println(\"Useta be they just encrypted text files, but nowadays I hear they'll encrypt anything and everything. I recall there used to be a useful bit of source code over in the " + snitchTalkSpecial + " - maybe it's still there? I think it useta be called \\\"Revealer.java\\\" or somesuch.\");       System.out.println(\"By the way, that's a nice looking Decrypter you've got there - I hear on those newer models, you can type in an extra parameter on the command line to name the destination file right away!\");     }     else if(amount == 31337357)    {   System.out.println(\"I see, you're here for the part of the passcode I stole. I'd give it to yas, but un-for-tu-nately... YTC got here first and reclaimed what was theirs.\"); System.out.println(\"Last I heard, they got it in maximum security lockup thanks to that new GateMasta Three Thousand doohicky.\"); System.out.println(\"I swiped one of them Electronic Information Lock files and stashed it with my cousin Switch over at " + snitchCousinAddress + ", give him a call! Rename him to Switch.class, give him the ol' .talk() - you know the drill.\");  }   else       System.out.println(\"Whoa whoa whoa, I don't think I can take that much money without somebody catching wind.\");   } } ";
    compileAndInsertClass("Snitch", sn, snitchDir.getPath(), snitchFileNum);
  }
  
  public static void createRevealer()
  {
    String rev = "import java.io.File;NEWLINEimport java.io.PrintWriter;NEWLINEimport javax.sound.sampled.AudioSystem;NEWLINEimport javax.sound.sampled.AudioFormat;NEWLINEimport javax.sound.sampled.SourceDataLine;NEWLINEpublic class RevealerNEWLINE{NEWLINE  public static void main(String[] args)NEWLINE  {NEWLINE    System.out.println(\"(turn on your sound)\");NEWLINE    tryNEWLINE    {NEWLINE      System.out.print(\"Found new file. Revealing\");NEWLINE      Thread.sleep(800);NEWLINE      System.out.print(\".\");NEWLINE      Thread.sleep(800);NEWLINE      System.out.print(\" .\");NEWLINE      Thread.sleep(800);NEWLINE      System.out.println(\" .\");NEWLINENEWLINE      File revealed = new File(\"File" + secretClassFileNum + ".txt\");NEWLINE      revealed.renameTo(new File(\"SecretClass.class\"));NEWLINE      System.out.println(\"SecretClass.class revealed successfully!\");NEWLINE      playSong();NEWLINENEWLINE      System.out.println();NEWLINENEWLINE      System.out.print(\"Found new file. Revealing\");NEWLINE      Thread.sleep(800);NEWLINE      System.out.print(\".\");NEWLINE      Thread.sleep(800);NEWLINE      System.out.print(\" .\");NEWLINE      Thread.sleep(800);NEWLINE      System.out.println(\" .\");NEWLINENEWLINE      File revealed2 = new File(\"File" + guruFileNum + ".txt\");NEWLINE      revealed2.renameTo(new File(\"PasscodeGuru.class\"));NEWLINE      System.out.println(\"PasscodeGuru.class revealed successfully!\");NEWLINE      playSong();NEWLINENEWLINE      System.out.println();NEWLINENEWLINE      System.out.print(\"Creating instructions for PasscodeGuru\");NEWLINE      Thread.sleep(800);NEWLINE      System.out.print(\".\");NEWLINE      Thread.sleep(800);NEWLINE      System.out.print(\" .\");NEWLINE      Thread.sleep(800);NEWLINE      System.out.println(\" .\");NEWLINENEWLINE      File docs = new File(\"PasscodeGuruDocumentation.txt\");NEWLINE      PrintWriter out = new PrintWriter(docs);NEWLINE      out.println(\"PasscodeGuru.class defines the PasscodeGuru object. As such, it is a class that contains no main method, nor any other static method.\");NEWLINE      out.println(\"Please note that the constructor for the PasscodeGuru class requires a single String as a parameter, for a name. You can name the Guru whatever you want!\");NEWLINE      out.println();NEWLINE      out.println(\"Non-static methods available in PasscodeGuru.class:\");NEWLINE      out.println();NEWLINE      out.println(\"askAboutPasscode(int number) - prints a message\");NEWLINE      out.println(\"askNumberOfPasscodes() - prints a message\");NEWLINE      out.println(\"chat() - prints a message\");NEWLINE      out.println(\"giveSandwich(String variety) - gives the Guru a sandwich\");NEWLINE      out.println(\"thanks() - returns a String\");NEWLINE      out.close();NEWLINE      System.out.println(\"PasscodeGuruDocumentation.txt created successfully!\");NEWLINE      playSong();NEWLINE    }NEWLINE    catch(Exception e)NEWLINE    {e.printStackTrace();}NEWLINE    NEWLINE    System.exit(0);NEWLINE  }NEWLINE  NEWLINE  public static void playSong()NEWLINE  {NEWLINE    tryNEWLINE    {NEWLINE      AudioFormat af = new AudioFormat( (float)44100, 8, 1, true, false );NEWLINE      SourceDataLine sdl = AudioSystem.getSourceDataLine( af );NEWLINE      sdl.open();NEWLINE      sdl.start();NEWLINE      playTriad(sdl, 440*Math.pow(2, -7.0/12), 100, 236);NEWLINE      playTriad(sdl, 440*Math.pow(2, -6.0/12), 100, 236);NEWLINE      playTriad(sdl, 440*Math.pow(2, -5.0/12), 100, 236);NEWLINE      playTriad(sdl, 440*Math.pow(2, -4.0/12), 100, 1400);NEWLINE      sdl.drain();NEWLINE      sdl.stop();NEWLINE    }NEWLINE    catch(Exception e){e.printStackTrace();}NEWLINE  }NEWLINE  NEWLINE  public static void playTriad(SourceDataLine sdl, double f1, double volume, double duration)NEWLINE  {NEWLINE    byte[] buf = new byte[ 1 ];NEWLINE    double[] ang = new double[3];NEWLINE    double rinc = 2.0/44100;NEWLINE    NEWLINE    double f2 = f1 * Math.pow(2, 4.0/12);NEWLINE    double f3 = f2 * Math.pow(2, 3.0/12);NEWLINE    NEWLINE    int fade = 1000;NEWLINE    double fcomp = volume/fade;NEWLINE    NEWLINE    tryNEWLINE    { NEWLINE      double cap = duration * (float)44100 / 1000;NEWLINE      NEWLINE      double[] inc = new double[]{f1*rinc, f2*rinc, f3*rinc};NEWLINE      NEWLINE      double i;NEWLINE      for(i=0; i < fade; i++ )NEWLINE      {NEWLINE        buf[0] = (byte)((Math.signum(ang[0]) + Math.signum(ang[1]) + Math.signum(ang[2]))*(i*fcomp)/3);NEWLINE        sdl.write( buf, 0, 1 );NEWLINE        for(int j=0; j<ang.length; j++)NEWLINE        {NEWLINE          ang[j] += inc[j];NEWLINE          if(ang[j] > 1) ang[j]-=2;NEWLINE        }NEWLINE      }NEWLINE      for(; i<cap; i++)NEWLINE      {NEWLINE        buf[0] = (byte)((Math.signum(ang[0]) + Math.signum(ang[1]) + Math.signum(ang[2]))*volume/3);NEWLINE        sdl.write( buf, 0, 1 );NEWLINE        for(int j=0; j<ang.length; j++)NEWLINE        {NEWLINE          ang[j] += inc[j];NEWLINE          if(ang[j] > 1) ang[j]-=2;NEWLINE        }NEWLINE      }NEWLINE    }NEWLINE    catch(Exception e){e.printStackTrace();}NEWLINE  }NEWLINE}NEWLINE";
    createAndInsertEncryptedSource("Revealer", rev, revealerDir.getPath(), revealerFileNum, DFO);
  }
  
  public static void createPasscodeGuru()
  {
    String pg = "import java.io.*; public class PasscodeGuru {   String name;   boolean pastrami = false;   String passcodePiece = \"s c0mm3\";    public PasscodeGuru(String n)   {     name = n;   }    public void askAboutPasscode(int number)   {     String message = \"I'm afraid I don't know of a passcode piece with that number.\";     switch(number)     {       case 1:         message = \"I might be able to help you with that, but I'm pretty hungry.\";         break;       case 2:         message = \"Navigate to " + passcode2Dir.getPath() + " and use your Decrypter to read File" + passcode2FileNum + ".txt.\";         break;       case 3:         message = \"Navigate to " + passcode3Dir.getPath() + " and take a look at File" + passcode3FileNum + ".txt\";         break;       case 4:         message = \"Navigate to " + passcode4Dir.getPath() +  ", decrypt File" + passcode4FileNum +  ".txt into BruteForce.java, and compile and run it.\";         break;       default:         break;     }     System.out.println(message);   }    public void askNumberOfPasscodes()   {     System.out.println(\"There are 4 pieces to the passcode in all.\");   }    public void chat()   {     if(!pastrami)     {       System.out.println(\"I know many things about the SecretClass passcode! Also, I sure do love \\\"pastrami\\\" sandwiches!\");     }     else     {       System.out.println(\"Wow, I'm flattered that you're still interested in talking to me even after I gave you my piece of the passcode. What a nice person.\");     }   }    public void giveSandwich(String variety)   {     variety = variety.toLowerCase();     if(variety.contains(\"pastrami\") && !(variety.contains(\"not pastrami\")) && !(variety.contains(\"not a pastrami\")))     {       pastrami = true;       System.out.println(\"Oh, is that a \" + variety + \" sandwich you have there? For me? Thank you so much!\");       System.out.println(\"My thanks() method will now give you my piece of the passcode! I hope you find whatever it is you're looking for.\");     }   }    public String thanks()   {     if(pastrami)       return(passcodePiece);     else       return \"You haven't done anything to make the Guru thank you yet!\";   } } ";
    compileAndInsertClass("PasscodeGuru", pg, revealerDir.getPath(), guruFileNum);
  }
  
  public static void createPasscodeChallenges()
  {
    String[] p2 = new String[]{
      "We've got another lead to follow! First, rename File" + mysteryArrayFileNum + ".txt to MysteryArray.class.",
      "",
      "MysteryArray.class contains a one-dimensional character array that has a number of entries that is a perfect square. Your job is to build a method to print out the entries of such an array in a square grid format, such that consecutive entries read top-to-bottom and left-to-right (in that order).",
      "",
      "Examples of what your method should do on different inputs:",
      "",
      "Input: {'a','b','c','d','e','f','g','h','i'}",
      "Output: adg",
      "        beh",
      "        cfi",
      "",
      "Input: {'h','m','t','p','e','e','r','e','l','i','a','d','p','m','p','!'}",
      "Output: help",
      "        meim",
      "        trap",
      "        ped!",
      "",
      "Once you've built your method and you're convinced it's working properly (hint: try testing it out on the above 2 examples to make sure you get the right output), you can get the array from MysteryArray.class by calling the MysteryArray.getCharArray() method!",
      "",
      "We're counting on you to crack this code!"
    };
    
    encryptAndPrintLinesToFile(p2, passcode2Dir.getPath() + ps + "File" + passcode2FileNum + ".txt", DFO);
    
    String sc = "public class Switch {   public Switch()   {   }      public static void main(String[] args)   {     System.out.println(\"Snitch, Switch. They're all the same, main's no use here.\");   }      public void talk()   {     System.out.println(\"Hey there, I assume my cousin Snitchy sent yas.\");     wait(2000);     System.out.println(\"The Electronic Information Lock? Yeah, I got it, it's down at " + gatemasterDir2.getPath() + ", File " + gatemasterCode2FileNum + "\");     wait(2000);     System.out.println();     System.out.println(\"You gotta use the GateMaster3000 to open it, but that won't do ya much good without a key.\");     System.out.println(\"Lucky for us, I just happened to liberate these two keys from some unsuspecting YTC goons\");     System.out.println(\"last Friday. Normally, I'd charge out the nose for info this good, but for friends of Snitchy....\");     wait(2000);     System.out.println();     System.out.println(\"*mumbling*('sides, I can't make heads or tails of 'em without a Decrypter anyway.)\");     wait(2000);     System.out.println();     System.out.println(\"Wait here, 'kay?\");     wait(1000);     System.out.println(\"*rattling*\");     wait(1000);     System.out.println(\"*clanging*\");     wait(1000);     System.out.println(\"*rustling*\");     wait(1000);     System.out.println(\"*more rustling*\");     wait(1000);     System.out.println();     System.out.println(\"Alright, here they are. File " + gatemasterKey2FileNum + " and File " + gatemasterKey3FileNum + ".\");     System.out.println(\"They're right in this here folder, no need to go hunting around.\");     System.out.println(\"Key 2 should be the one you need for the lock we got, but hang on to Key 3 - it might come in handy later.\");     wait(2000);     System.out.println();     System.out.println(\"That's all I know. Now scram, before YTC realizes I helped ya.\");   }      public void bribe(int amount)   {     if(amount < 50)     {       System.out.println(\"Like that amount's going to do anybody any good. Beat it, punk.\");     }     else     {       System.out.println(\"Switch hungrily eyes the money in your hand. Before you can react, he snatches it and runs away!\");       System.out.println();       System.out.println(\"You notice two pieces of paper lying on the ground, with handwritten notes scrawled on them.\");       System.out.println(\"They seem to have fallen out of Switch's pockets in his haste to escape.\");       System.out.println();       System.out.println(\"The first says: 'Output is on when exactly two inputs are on. Probably ANDs inside ORs?'\");       System.out.println(\"The second says only: '(P ^ (Q XOR R)) v (~P ^ (Q ^ R))'. What could it mean?\");       System.out.println();       System.out.println(\"Maybe Switch knew more than he was letting on?\");     }   }      public void giveSandwich(String type)   {     if(type.equals(\"pastrami\"))     {       System.out.println(\"Give that to the Guru!\");     }     else if(type.equals(\"peanut butter\"))     {       System.out.println(\"How the heck did you know to do that? Thanks, I guess!\");     }   }      private void wait(int dur)   {     try{Thread.sleep(dur);}catch(Exception e){}   } } ";
    compileAndInsertClass("Switch", sc, snitchCousinDir.getPath(), snitchCousinFileNum);
    
    String[] gmk2 = new String[]{
    "KEY 2",
    "(output on the left)",
    "",
    "    R Q P",
    "   -------",
    "F | T T T",
    "T | T T F",
    "T | T F T",
    "F | T F F",
    "T | F T T",
    "F | F T F",
    "F | F F T",
    "F | F F F",
    "",
    "With NOT AND OR, you can open the door"
    };
    encryptAndPrintLinesToFile(gmk2, snitchCousinDir.getPath() + ps + "File" + gatemasterKey2FileNum + ".txt", DFO);
    
    String[] gmk3 = new String[]{
    "KEY 3",
    "(output on the left)",
    "",
    "    R Q P",
    "   -------",
    "F | T T T",
    "T | T T F",
    "T | T F T",
    "F | T F F",
    "T | F T T",
    "F | F T F",
    "F | F F T",
    "F | F F F",
    "",
    "Same as KEY 2, but more gates for you"
    };
    encryptAndPrintLinesToFile(gmk3, snitchCousinDir.getPath() + ps + "File" + gatemasterKey3FileNum + ".txt", DFO);
    
    String ma = "public class MysteryArray {   private static char[] c = {'B','e',' ','v','3','t','p','t','r',' ','w','a','1','o','i','h','i','s','i','l','3',' ','e','e','b','n','t','u','3','g','c',' ','e','i','h','e','7','e','e','c',' ','t',' ',' ','3','t',' ','o','t','c','a','o','5',' ','o','d','h','h',' ','f','7','a','f','e'};    public static char[] getCharArray()   {     return c;   } } ";
    compileAndInsertClass("MysteryArray", ma, passcode2Dir.getPath(), mysteryArrayFileNum);
    
    
    String[] p3 = new String[]{
      "YTC PASSCODE PIECE RECOVERY SYSTEM",
      "",
      "Forgot piece 3 of the passcode? Read this file in its entirety for instructions on how to retrieve it!",
      "",
      "Rename File" + mysteryArray2FileNum + ".txt to MysteryArray2.class",
      "",
      "MysteryArray2.class contains a 1-dimensional array of characters, along with a static getCharArray() method which returns that array. You need to write a program to retrieve that character array, then convert it into a 2D array of Color objects with the following key:",
      "  'k': black",
      "  'r': red",
      "  'g': green",
      "  'b': blue",
      "  'y': yellow",
      "  'm': magenta",
      "  'c': cyan",
      "  'w': white",
      "",
      "The array is 1-dimensional, but the resulting image should be 2-dimensional. As an extra layer of security, the rows have all been combined into one, in their original order. We have left it up to you to figure out how many rows and columns the image should have - keep in mind that the image is a perfect rectangle, and every character in the original array must be used. We're confident that CTY agents won't be able to break such impregnable security!",
      "",
      "Once you make your 2D array of colors, you can view it by calling the static transformAndDisplay(Color[][] pic) method in the MysteryArray2 class.",
      "",
      "The picture itself does NOT contain a piece of the SecretClass passcode. Instead, MysteryArray2 has a main method, which requires a password (one English word) to be put in as a single command line argument. This word corresponds to what is depicted in the image. When the correct word is entered, a piece of the SecretClass passcode will be unlocked.",
      "",
      "VERY IMPORTANT: DO NOT REVEAL THE PASSCODE TO ANY CTY AGENTS!!!"
    };
    printLinesToFile(p3, passcode3Dir.getPath() + ps + "File" + passcode3FileNum + ".txt");    
    
    String ma2 = "import java.awt.image.BufferedImage; import javax.swing.JPanel; import javax.swing.JFrame; import java.awt.Graphics; import java.awt.Color; public class MysteryArray2 extends JPanel {   private static char[] c = \"wwwwwwgrrrrrmrrrmrrrrrcccmmmgrrmrrrrggmmgwwkkkwggbbbrcwwwwwwwwwwwwwwwwwwwwwwwwrgbbwkkkkkwwwwrrbwkkwwwwwwwwwwwwwwwwwwwwwgbbwwkkkkkkkwwwrcwkwkwwwwwwwwwwwwwwwwwwwwwgbwwkkkkyykkwwwcwkkwwwkwwkkkwwkwwkkkwwkwwgbmwwkkyyyykkwwcwkwkwkkkwkwkwkkkwkwkwkkkwbbmbwwkyyyyykkwwwkkkwkwkwkwkwkwkwkwkwkwkwbrmrbwwkyykyyykkwwwwwwwwwwwwwwwwwwwwwwwwwrrmrrbwkkyykyyykkwwwwbbwrrwwrrrrrrrwwwwrrrmwcrrwwkkyykkyykkkkwwwbrrrrrrrrwbbbbrrrbbbwccrgwwkyyyykyyyykkkwwwwwwmmmmmrwwbbbbwbbbrrrggwwkyyyykyyyyykkkkwwwwmmmmrrrrrrbbbwbgrrrggwkkyyyykkyyyyyykkkkwwwwwbbbwwrrwbbbgrrrmgwwkkyyyyykkyyyyyyykkkkwwwwbbbwrrwwbgrccmmgwwkkyyyyyykyyyyyyyyykkkkwwwbbwrrrbgrcrrmmbwwkkyyyyyyykkyyyyyyyyykkkwwwwwwwbrrcrrrccwwwkkkyyyyyyykykyyyyyyyykkkkkkkwwrrrrrccrrcwwwkkyyyyyyyyyykkyyyyyyyyyyykkwrrrrccrrrrccwwkkkyyyyyyyyyykykyyyyyyyykwwmggccrrrrrrrcwwwkkkkkyyyyyyyyyykkyyykkwwbmcccbbmmmmmrrrrwwwwwkkkkkyyyyyyyyykkkwwbrmgbbbbmmmmmrrrrrrrcwwwwwkkkkkkkkkkkwwbbrmmggggbbbbbwrrwwwrrrrcccwwwwwwwwwwwwwbbmmm\".toCharArray();    public static void main(String[] args)   {     if(args.length>0)     {       if(args[0].equalsIgnoreCase(\"banana\"))         System.out.println(\"YOUR PASSCODE PIECE IS \\\"4lway\\\"\");       else         System.out.println(\"INVALID ARGUMENT\");     }     else       System.out.println(\"ARGUMENT REQUIRED\");   }      public static char[] getCharArray()   {     return c;   }      private static BufferedImage magnify(BufferedImage in, int times)   {     BufferedImage ret = new BufferedImage(in.getWidth()*times, in.getHeight()*times, in.getType());     int w = ret.getWidth();     int h = ret.getHeight();     for(int row = 0; row < h; row++)     {       for(int col = 0; col < w; col++)       {         ret.setRGB(col, row, in.getRGB(col/times, row/times));       }     }     return ret;   }      private BufferedImage image = null;   public void paintComponent(Graphics g)   {     g.drawImage(image, 0, 0, null);   }      public static void transformAndDisplay(Color[][] pic)   {     BufferedImage im = new BufferedImage(pic[0].length, pic.length, BufferedImage.TYPE_INT_ARGB);          for(int row = 0; row < pic.length; row++)     {       for(int col = 0; col < pic[0].length; col++)       {         im.setRGB(col, row, pic[row][col].getRGB());       }     }          showImage(im);   }      public static void showImage(BufferedImage original)   {     BufferedImage im = magnify(original, 12);     JFrame frame = new JFrame();     frame.setSize(600,600);     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     MysteryArray2 panel = new MysteryArray2();     panel.image = im;     frame.add(panel);     frame.setVisible(true);   } } ";
    compileAndInsertClass("MysteryArray2", ma2, passcode3Dir.getPath(), mysteryArray2FileNum);
    
    String bf = "public class BruteForceNEWLINE{NEWLINE  public static void main(String[] args)NEWLINE  {NEWLINE    System.out.println(\"You'll find your next challenge waiting for you in File" + impossiblePassFileNum +  ".txt. Rename it to ImpossiblePass.class\");NEWLINE    System.out.println();NEWLINE    System.out.println(\"When you call the tryPassword(String password) method of ImpossiblePass, it will check if you have the correct password. If you do, it will return a String containing the next piece of the passcode; else, you'll get a message like this (uncomment out the next line of source code after you've renamed File" + impossiblePassFileNum + ".txt to ImpossiblePass.class):\");NEWLINE //System.out.println(ImpossiblePass.tryPassword(\"anana\"));   NEWLINENEWLINE    System.out.println();NEWLINE    System.out.println(\"The password contains exactly 5 characters, which are all lowercase letters. And that's it! That's all you get! No puzzle to solve, no cryptic hints about what the password might be. Just you, me, and some code. Whatever will you do?\");NEWLINE  }NEWLINE}NEWLINE";
    createAndInsertEncryptedSource("BruteForce", bf, passcode4Dir.getPath(), passcode4FileNum, DFO);
    
    String ip = "public class ImpossiblePass {   public static String tryPassword(String pass)   {     if(pass.equals(\"hbyzw\"))       return \"PASSWORD CORRECT! PIECE OF PASSCODE IS \\\"0d3\\\"\";     else       return \"PASSWORD INCORRECT\";   } } ";
    compileAndInsertClass("ImpossiblePass", ip, passcode4Dir.getPath(), impossiblePassFileNum);
  }
  
  public static void createSecretClass()
  {
    String scPath = revealerDir.getPath();
    
    String[] awwCode = new String[3];
    awwCode[0] = "import java.io.*; import java.awt.*; import java.awt.image.*;  /**  * Class AnimatedGifEncoder - Encodes a GIF file consisting of one or more  * frames.  *   * <pre>  *  Example:  * 5   AnimatedGifEncoder e = new AnimatedGifEncoder();  *     e.start(outputFileName);  *     e.setDelay(1000);     *     e.addFrame(image1);  *     e.addFrame(image2);  *     e.finish();  * </pre>  *   * No copyright asserted on the source code of this class. May be used for any  * purpose, however, refer to the Unisys LZW patent for restrictions on use of  * the associated LZWEncoder class. Please forward any corrections to  * kweiner@fmsware.com.  *   * @author Kevin Weiner, FM Software  * @version 1.03 November 2003  *   */  public class AnimatedGifEncoder {    protected int width;     protected int height;    protected Color transparent = null;     protected int transIndex;     protected int repeat = -1;     protected int delay = 0;     protected boolean started = false;     protected OutputStream out;    protected BufferedImage image;     protected byte[] pixels;     protected byte[] indexedPixels;     protected int colorDepth;     protected byte[] colorTab;     protected boolean[] usedEntry = new boolean[256];     protected int palSize = 7;     protected int dispose = -1;     protected boolean closeStream = false;     protected boolean firstFrame = true;    protected boolean sizeSet = false;     protected int sample = 10;     /**    * Sets the delay time between each frame, or changes it for subsequent frames    * (applies to last frame added).    *     * @param ms    *          int delay time in milliseconds    */   public void setDelay(int ms) {     delay = Math.round(ms / 10.0f);   }    /**    * Sets the GIF frame disposal code for the last added frame and any    * subsequent frames. Default is 0 if no transparent color has been set,    * otherwise 2.    *     * @param code    *          int disposal code.    */   public void setDispose(int code) {     if (code >= 0) {       dispose = code;     }   }    /**    * Sets the number of times the set of GIF frames should be played. Default is    * 1; 0 means play indefinitely. Must be invoked before the first image is    * added.    *     * @param iter    *          int number of iterations.    * @return    */   public void setRepeat(int iter) {     if (iter >= 0) {       repeat = iter;     }   }    /**    * Sets the transparent color for the last added frame and any subsequent    * frames. Since all colors are subject to modification in the quantization    * process, the color in the final palette for each frame closest to the given    * color becomes the transparent color for that frame. May be set to null to    * indicate no transparent color.    *     * @param c    *          Color to be treated as transparent on display.    */   public void setTransparent(Color c) {     transparent = c;   }    /**    * Adds next GIF frame. The frame is not written immediately, but is actually    * deferred until the next frame is received so that timing data can be    * inserted. Invoking <code>finish()</code> flushes all frames. If    * <code>setSize</code> was not invoked, the size of the first image is used    * for all subsequent frames.    *     * @param im    *          BufferedImage containing frame to write.    * @return true if successful.    */   public boolean addFrame(BufferedImage im) {     if ((im == null) || !started) {       return false;     }     boolean ok = true;     try {       if (!sizeSet) {                  setSize(im.getWidth(), im.getHeight());       }       image = im;       getImagePixels();        analyzePixels();        if (firstFrame) {         writeLSD();          writePalette();          if (repeat >= 0) {                      writeNetscapeExt();         }       }       writeGraphicCtrlExt();        writeImageDesc();        if (!firstFrame) {         writePalette();        }       writePixels();        firstFrame = false;     } catch (IOException e) {       ok = false;     }      return ok;   }    /**    * Flushes any pending data and closes output file. If writing to an    * OutputStream, the stream is not closed.    */   public boolean finish() {     if (!started)       return false;     boolean ok = true;     started = false;     try {       out.write(0x3b);        out.flush();       if (closeStream) {         out.close();       }     } catch (IOException e) {       ok = false;     }           transIndex = 0;     out = null;     image = null;     pixels = null;     indexedPixels = null;     colorTab = null;     closeStream = false;     firstFrame = true;      return ok;   }    /**    * Sets frame rate in frames per second. Equivalent to    * <code>setDelay(1000/fps)</code>.    *     * @param fps    *          float frame rate (frames per second)    */   public void setFrameRate(float fps) {     if (fps != 0f) {       delay = Math.round(100f / fps);     }   }    /**    * Sets quality of color quantization (conversion of images to the maximum 256    * colors allowed by the GIF specification). Lower values (minimum = 1)    * produce better colors, but slow processing significantly. 10 is the    * default, and produces good color mapping at reasonable speeds. Values    * greater than 20 do not yield significant improvements in speed.    *     * @param quality    *          int greater than 0.    * @return    */   public void setQuality(int quality) {     if (quality < 1)       quality = 1;     sample = quality;   }    /**    * Sets the GIF frame size. The default size is the size of the first frame    * added if this method is not invoked.    *     * @param w    *          int frame width.    * @param h    *          int frame width.    */   public void setSize(int w, int h) {     if (started && !firstFrame)       return;     width = w;     height = h;     if (width < 1)       width = 320;     if (height < 1)       height = 240;     sizeSet = true;   }    /**    * Initiates GIF file creation on the given stream. The stream is not closed    * automatically.    *     * @param os    *          OutputStream on which GIF images are written.    * @return false if initial write failed.    */   public boolean start(OutputStream os) {     if (os == null)       return false;     boolean ok = true;     closeStream = false;     out = os;     try {       writeString(\"GIF89a\");      } catch (IOException e) {       ok = false;     }     return started = ok;   }    /**    * Initiates writing of a GIF file with the specified name.    *     * @param file    *          String containing output file name.    * @return false if open or initial write failed.    */   public boolean start(String file) {     boolean ok = true;     try {       out = new BufferedOutputStream(new FileOutputStream(file));       ok = start(out);       closeStream = true;     } catch (IOException e) {       ok = false;     }     return started = ok;   }    /**    * Analyzes image colors and creates color map.    */   protected void analyzePixels() {     int len = pixels.length;     int nPix = len / 3;     indexedPixels = new byte[nPix];     NeuQuant nq = new NeuQuant(pixels, len, sample);          colorTab = nq.process();           for (int i = 0; i < colorTab.length; i += 3) {       byte temp = colorTab[i];       colorTab[i] = colorTab[i + 2];       colorTab[i + 2] = temp;       usedEntry[i / 3] = false;     }          int k = 0;     for (int i = 0; i < nPix; i++) {       int index = nq.map(pixels[k++] & 0xff, pixels[k++] & 0xff, pixels[k++] & 0xff);       usedEntry[index] = true;       indexedPixels[i] = (byte) index;     }     pixels = null;     colorDepth = 8;     palSize = 7;          if (transparent != null) {       transIndex = findClosest(transparent);     }   }    /**    * Returns index of palette color closest to c    *     */   protected int findClosest(Color c) {     if (colorTab == null)       return -1;     int r = c.getRed();     int g = c.getGreen();     int b = c.getBlue();     int minpos = 0;     int dmin = 256 * 256 * 256;     int len = colorTab.length;     for (int i = 0; i < len;) {       int dr = r - (colorTab[i++] & 0xff);       int dg = g - (colorTab[i++] & 0xff);       int db = b - (colorTab[i] & 0xff);       int d = dr * dr + dg * dg + db * db;       int index = i / 3;       if (usedEntry[index] && (d < dmin)) {         dmin = d;         minpos = index;       }       i++;     }     return minpos;   }    /**    * Extracts image pixels into byte array \"pixels\"    */   protected void getImagePixels() {     int w = image.getWidth();     int h = image.getHeight();     int type = image.getType();     if ((w != width) || (h != height) || (type != BufferedImage.TYPE_3BYTE_BGR)) {              BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);       Graphics2D g = temp.createGraphics();       g.drawImage(image, 0, 0, null);       image = temp;     }     pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();   }    /**    * Writes Graphic Control Extension    */   protected void writeGraphicCtrlExt() throws IOException {     out.write(0x21);      out.write(0xf9);      out.write(4);      int transp, disp;     if (transparent == null) {       transp = 0;       disp = 0;      } else {       transp = 1;       disp = 2;      }     if (dispose >= 0) {       disp = dispose & 7;      }     disp <<= 2;           out.write(0 |          disp |          0 |          transp);       writeShort(delay);      out.write(transIndex);      out.write(0);    }    /**    * Writes Image Descriptor    */   protected void writeImageDesc() throws IOException {     out.write(0x2c);      writeShort(0);      writeShort(0);     writeShort(width);      writeShort(height);          if (firstFrame) {              out.write(0);     } else {              out.write(0x80 |            0 |            0 |            0 |            palSize);      }   }    /**    * Writes Logical Screen Descriptor    */   protected void writeLSD() throws IOException {          writeShort(width);     writeShort(height);          out.write((0x80 |          0x70 |          0x00 |          palSize));       out.write(0);      out.write(0);    }    /**    * Writes Netscape application extension to define repeat count.    */   protected void writeNetscapeExt() throws IOException {     out.write(0x21);      out.write(0xff);      out.write(11);      writeString(\"NETSCAPE\" + \"2.0\");      out.write(3);      out.write(1);      writeShort(repeat);      out.write(0);    }    /**    * Writes color table    */   protected void writePalette() throws IOException {     out.write(colorTab, 0, colorTab.length);     int n = (3 * 256) - colorTab.length;     for (int i = 0; i < n; i++) {       out.write(0);     }   }    /**    * Encodes and writes pixel data    */   protected void writePixels() throws IOException {     LZWEncoder encoder = new LZWEncoder(width, height, indexedPixels, colorDepth);     encoder.encode(out);   }    /**    * Write 16-bit value to output stream, LSB first    */   protected void writeShort(int value) throws IOException {     out.write(value & 0xff);     out.write((value >> 8) & 0xff);   }    /**    * Writes string to output stream    */   protected void writeString(String s) throws IOException {     for (int i = 0; i < s.length(); i++) {       out.write((byte) s.charAt(i));     }   } }  /*  * NeuQuant Neural-Net Quantization Algorithm  * ------------------------------------------  *   * Copyright (c) 1994 Anthony Dekker  *   * NEUQUANT Neural-Net quantization algorithm by Anthony Dekker, 1994. See  * \"Kohonen neural networks for optimal colour quantization\" in \"Network:  * Computation in Neural Systems\" Vol. 5 (1994) pp 351-367. for a discussion of  * the algorithm.  *   * Any party obtaining a copy of these files from the author, directly or  * indirectly, is granted, free of charge, a full and unrestricted irrevocable,  * world-wide, paid up, royalty-free, nonexclusive right and license to deal in  * this software and documentation files (the \"Software\"), including without  * limitation the rights to use, copy, modify, merge, publish, distribute,  * sublicense, and/or sell copies of the Software, and to permit persons who  * receive copies from any such party to do so, with the only requirement being  * that this copyright notice remain intact.  */   class NeuQuant {    protected static final int netsize = 256; /* number of colours used */    /* four primes near 500 - assume no image has a length so large */   /* that it is divisible by all four primes */   protected static final int prime1 = 499;    protected static final int prime2 = 491;    protected static final int prime3 = 487;    protected static final int prime4 = 503;    protected static final int minpicturebytes = (3 * prime4);    /* minimum size for input image */    /*    * Program Skeleton ---------------- [select samplefac in range 1..30] [read    * image from input file] pic = (unsigned char*) malloc(3*width*height);    * initnet(pic,3*width*height,samplefac); learn(); unbiasnet(); [write output    * image header, using writecolourmap(f)] inxbuild(); write output image using    * inxsearch(b,g,r)    */    /*    * Network Definitions -------------------    */    protected static final int maxnetpos = (netsize - 1);    protected static final int netbiasshift = 4; /* bias for colour values */    protected static final int ncycles = 100; /* no. of learning cycles */    /* defs for freq and bias */   protected static final int intbiasshift = 16; /* bias for fractions */    protected static final int intbias = (((int) 1) << intbiasshift);    protected static final int gammashift = 10; /* gamma = 1024 */    protected static final int gamma = (((int) 1) << gammashift);    protected static final int betashift = 10;    protected static final int beta = (intbias >> betashift); /* beta = 1/1024 */    protected static final int betagamma = (intbias << (gammashift - betashift));    /* defs for decreasing radius factor */   protected static final int initrad = (netsize >> 3); /*                                                          * for 256 cols, radius                                                          * starts                                                          */    protected static final int radiusbiasshift = 6; /* at 32.0 biased by 6 bits */    protected static final int radiusbias = (((int) 1) << radiusbiasshift);    protected static final int initradius = (initrad * radiusbias); /*                                                                    * and                                                                    * decreases                                                                    * by a                                                                    */    protected static final int radiusdec = 30; /* factor of 1/30 each cycle */    /* defs for decreasing alpha factor */   protected static final int alphabiasshift = 10; /* alpha starts at 1.0 */    protected static final int initalpha = (((int) 1) << alphabiasshift);    protected int alphadec; /* biased by 10 bits */    /* radbias and alpharadbias used for radpower calculation */   protected static final int radbiasshift = 8;    protected static final int radbias = (((int) 1) << radbiasshift);    protected static final int alpharadbshift = (alphabiasshift + radbiasshift);    protected static final int alpharadbias = (((int) 1) << alpharadbshift);    /*    * Types and Global Variables --------------------------    */    protected byte[] thepicture; /* the input image itself */    protected int lengthcount; /* lengthcount = H*W*3 */    protected int samplefac; /* sampling factor 1..30 */       protected int[][] network; /* the network itself - [netsize][4] */    protected int[] netindex = new int[256];    /* for network lookup - really 256 */    protected int[] bias = new int[netsize];    /* bias and freq arrays for learning */   protected int[] freq = new int[netsize];    protected int[] radpower = new int[initrad];    /* radpower for precomputation */    /*    * Initialise network in range (0,0,0) to (255,255,255) and set parameters    * -----------------------------------------------------------------------    */   public NeuQuant(byte[] thepic, int len, int sample) {      int i;     int[] p;      thepicture = thepic;     lengthcount = len;     samplefac = sample;      network = new int[netsize][];     for (i = 0; i < netsize; i++) {       network[i] = new int[4];       p = network[i];       p[0] = p[1] = p[2] = (i << (netbiasshift + 8)) / netsize;       freq[i] = intbias / netsize; /* 1/netsize */       bias[i] = 0;     }   }    public byte[] colorMap() {     byte[] map = new byte[3 * netsize];     int[] index = new int[netsize];     for (int i = 0; i < netsize; i++)       index[network[i][3]] = i;     int k = 0;     for (int i = 0; i < netsize; i++) {       int j = index[i];       map[k++] = (byte) (network[j][0]);       map[k++] = (byte) (network[j][1]);       map[k++] = (byte) (network[j][2]);     }     return map;   }    /*    * Insertion sort of network and building of netindex[0..255] (to do after    * unbias)    * -------------------------------------------------------------------------------    */   public void inxbuild() {      int i, j, smallpos, smallval;     int[] p;     int[] q;     int previouscol, startpos;      previouscol = 0;     startpos = 0;     for (i = 0; i < netsize; i++) {       p = network[i];       smallpos = i;       smallval = p[1]; /* index on g */       /* find smallest in i..netsize-1 */       for (j = i + 1; j < netsize; j++) {         q = network[j];         if (q[1] < smallval) { /* index on g */           smallpos = j;           smallval = q[1]; /* index on g */         }       }       q = network[smallpos];       /* swap p (i) and q (smallpos) entries */       if (i != smallpos) {         j = q[0];         q[0] = p[0];         p[0] = j;         j = q[1];         q[1] = p[1];         p[1] = j;         j = q[2];         q[2] = p[2];         p[2] = j;         j = q[3];         q[3] = p[3];         p[3] = j;       }       /* smallval entry is now in position i */       if (smallval != previouscol) {         netindex[previouscol] = (startpos + i) >> 1;         for (j = previouscol + 1; j < smallval; j++)           netindex[j] = i;         previouscol = smallval;         startpos = i;       }     }     netindex[previouscol] = (startpos + maxnetpos) >> 1;     for (j = previouscol + 1; j < 256; j++)       netindex[j] = maxnetpos; /* really 256 */   }    /*    * Main Learning Loop ------------------    */   public void learn() {      int i, j, b, g, r;     int radius, rad, alpha, step, delta, samplepixels;     byte[] p;     int pix, lim;      if (lengthcount < minpicturebytes)       samplefac = 1;     alphadec = 30 + ((samplefac - 1) / 3);     p = thepicture;     pix = 0;     lim = lengthcount;     samplepixels = lengthcount / (3 * samplefac);     delta = samplepixels / ncycles;     alpha = initalpha;     radius = initradius;      rad = radius >> radiusbiasshift;     if (rad <= 1)       rad = 0;     for (i = 0; i < rad; i++)       radpower[i] = alpha * (((rad * rad - i * i) * radbias) / (rad * rad));            if (lengthcount < minpicturebytes)       step = 3;     else if ((lengthcount % prime1) != 0)       step = 3 * prime1;     else {       if ((lengthcount % prime2) != 0)         step = 3 * prime2;       else {         if ((lengthcount % prime3) != 0)           step = 3 * prime3;         else           step = 3 * prime4;       }     }      i = 0;     while (i < samplepixels) {       b = (p[pix + 0] & 0xff) << netbiasshift;       g = (p[pix + 1] & 0xff) << netbiasshift;       r = (p[pix + 2] & 0xff) << netbiasshift;       j = contest(b, g, r);        altersingle(alpha, j, b, g, r);       if (rad != 0)         alterneigh(rad, j, b, g, r); /* alter neighbours */        pix += step;       if (pix >= lim)         pix -= lengthcount;        i++;       if (delta == 0)         delta = 1;       if (i % delta == 0) {         alpha -= alpha / alphadec;         radius -= radius / radiusdec;         rad = radius >> radiusbiasshift;         if (rad <= 1)           rad = 0;         for (j = 0; j < rad; j++)           radpower[j] = alpha * (((rad * rad - j * j) * radbias) / (rad * rad));       }     }             }    /*    * Search for BGR values 0..255 (after net is unbiased) and return colour    * index    * ----------------------------------------------------------------------------    */   public int map(int b, int g, int r) {      int i, j, dist, a, bestd;     int[] p;     int best;      bestd = 1000; /* biggest possible dist is 256*3 */     best = -1;     i = netindex[g]; /* index on g */     j = i - 1; /* start at netindex[g] and work outwards */      while ((i < netsize) || (j >= 0)) {       if (i < netsize) {         p = network[i];         dist = p[1] - g; /* inx key */         if (dist >= bestd)           i = netsize; /* stop iter */         else {           i++;           if (dist < 0)             dist = -dist;           a = p[0] - b;           if (a < 0)             a = -a;           dist += a;           if (dist < bestd) {             a = p[2] - r;             if (a < 0)               a = -a;             dist += a;             if (dist < bestd) {               bestd = dist;               best = p[3];             }           }         }       }       if (j >= 0) {         p = network[j];         dist = g - p[1]; /* inx key - reverse dif */         if (dist >= bestd)           j = -1; /* stop iter */         else {           j--;           if (dist < 0)             dist = -dist;           a = p[0] - b;           if (a < 0)             a = -a;           dist += a;           if (dist < bestd) {             a = p[2] - r;             if (a < 0)               a = -a;             dist += a;             if (dist < bestd) {               bestd = dist;               best = p[3];             }           }         }       }     }     return (best);   }    public byte[] process() {     learn();     unbiasnet();     inxbuild();     return colorMap();   }    /*    * Unbias network to give byte values 0..255 and record position i to prepare    * for sort    * -----------------------------------------------------------------------------------    */   public void unbiasnet() {      int i, j;      for (i = 0; i < netsize; i++) {       network[i][0] >>= netbiasshift;       network[i][1] >>= netbiasshift;       network[i][2] >>= netbiasshift;       network[i][3] = i; /* record colour no */     }   }    /*    * Move adjacent neurons by precomputed alpha*(1-((i-j)^2/[r]^2)) in    * radpower[|i-j|]    * ---------------------------------------------------------------------------------    */   protected void alterneigh(int rad, int i, int b, int g, int r) {      int j, k, lo, hi, a, m;     int[] p;      lo = i - rad;     if (lo < -1)       lo = -1;     hi = i + rad;     if (hi > netsize)       hi = netsize;      j = i + 1;     k = i - 1;     m = 1;     while ((j < hi) || (k > lo)) {       a = radpower[m++];       if (j < hi) {         p = network[j++];         try {           p[0] -= (a * (p[0] - b)) / alpharadbias;           p[1] -= (a * (p[1] - g)) / alpharadbias;           p[2] -= (a * (p[2] - r)) / alpharadbias;         } catch (Exception e) {         }        }       if (k > lo) {         p = network[k--];         try {           p[0] -= (a * (p[0] - b)) / alpharadbias;           p[1] -= (a * (p[1] - g)) / alpharadbias;           p[2] -= (a * (p[2] - r)) / alpharadbias;         } catch (Exception e) {         }       }     }   }    /*    * Move neuron i towards biased (b,g,r) by factor alpha    * ----------------------------------------------------    */   protected void altersingle(int alpha, int i, int b, int g, int r) {      /* alter hit neuron */     int[] n = network[i];     n[0] -= (alpha * (n[0] - b)) / initalpha;     n[1] -= (alpha * (n[1] - g)) / initalpha;     n[2] -= (alpha * (n[2] - r)) / initalpha;   }    /*    * Search for biased BGR values ----------------------------    */   protected int contest(int b, int g, int r) {      /* finds closest neuron (min dist) and updates freq */     /* finds best neuron (min dist-bias) and returns position */     /* for frequently chosen neurons, freq[i] is high and bias[i] is negative */     /* bias[i] = gamma*((1/netsize)-freq[i]) */      int i, dist, a, biasdist, betafreq;     int bestpos, bestbiaspos, bestd, bestbiasd;     int[] n;      bestd = ~(((int) 1) << 31);     bestbiasd = bestd;     bestpos = -1;     bestbiaspos = bestpos;      for (i = 0; i < netsize; i++) {       n = network[i];       dist = n[0] - b;       if (dist < 0)         dist = -dist;       a = n[1] - g;       if (a < 0)         a = -a;       dist += a;       a = n[2] - r;       if (a < 0)         a = -a;       dist += a;       if (dist < bestd) {         bestd = dist;         bestpos = i;       }       biasdist = dist - ((bias[i]) >> (intbiasshift - netbiasshift));       if (biasdist < bestbiasd) {         bestbiasd = biasdist;         bestbiaspos = i;       }       betafreq = (freq[i] >> betashift);       freq[i] -= betafreq;       bias[i] += (betafreq << gammashift);     }     freq[bestpos] += beta;     bias[bestpos] -= betagamma;     return (bestbiaspos);   } }      class LZWEncoder {    private static final int EOF = -1;    private int imgW, imgH;    private byte[] pixAry;    private int initCodeSize;    private int remaining;    private int curPixel;                     static final int BITS = 12;    static final int HSIZE = 5003;                                    int n_bits;     int maxbits = BITS;     int maxcode;     int maxmaxcode = 1 << BITS;     int[] htab = new int[HSIZE];    int[] codetab = new int[HSIZE];    int hsize = HSIZE;     int free_ent = 0;           boolean clear_flg = false;                                      int g_init_bits;    int ClearCode;    int EOFCode;                                               int cur_accum = 0;    int cur_bits = 0;    int masks[] = { 0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F, 0x003F, 0x007F, 0x00FF, 0x01FF,       0x03FF, 0x07FF, 0x0FFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF };       int a_count;       byte[] accum = new byte[256];       LZWEncoder(int width, int height, byte[] pixels, int color_depth) {     imgW = width;     imgH = height;     pixAry = pixels;     initCodeSize = Math.max(2, color_depth);   }          void char_out(byte c, OutputStream outs) throws IOException {     accum[a_count++] = c;     if (a_count >= 254)       flush_char(outs);   }           void cl_block(OutputStream outs) throws IOException {     cl_hash(hsize);     free_ent = ClearCode + 2;     clear_flg = true;      output(ClearCode, outs);   }       void cl_hash(int hsize) {     for (int i = 0; i < hsize; ++i)       htab[i] = -1;   }    void compress(int init_bits, OutputStream outs) throws IOException {     int fcode;     int i /* = 0 */;     int c;     int ent;     int disp;     int hsize_reg;     int hshift;           g_init_bits = init_bits;           clear_flg = false;     n_bits = g_init_bits;     maxcode = MAXCODE(n_bits);      ClearCode = 1 << (init_bits - 1);     EOFCode = ClearCode + 1;     free_ent = ClearCode + 2;      a_count = 0;       ent = nextPixel();      hshift = 0;     for (fcode = hsize; fcode < 65536; fcode *= 2)       ++hshift;     hshift = 8 - hshift;       hsize_reg = hsize;     cl_hash(hsize_reg);       output(ClearCode, outs);      outer_loop: while ((c = nextPixel()) != EOF) {       fcode = (c << maxbits) + ent;       i = (c << hshift) ^ ent;         if (htab[i] == fcode) {         ent = codetab[i];         continue;       } else if (htab[i] >= 0)        {         disp = hsize_reg - i;          if (i == 0)           disp = 1;         do {           if ((i -= disp) < 0)             i += hsize_reg;            if (htab[i] == fcode) {             ent = codetab[i];             continue outer_loop;           }         } while (htab[i] >= 0);       }       output(ent, outs);       ent = c;       if (free_ent < maxmaxcode) {         codetab[i] = free_ent++;          htab[i] = fcode;       } else         cl_block(outs);     }          output(ent, outs);     output(EOFCode, outs);   }       void encode(OutputStream os) throws IOException {     os.write(initCodeSize);       remaining = imgW * imgH;      curPixel = 0;      compress(initCodeSize + 1, os);       os.write(0);    }       void flush_char(OutputStream outs) throws IOException {     if (a_count > 0) {       outs.write(a_count);       outs.write(accum, 0, a_count);       a_count = 0;     }   }    final int MAXCODE(int n_bits) {     return (1 << n_bits) - 1;   }             private int nextPixel() {     if (remaining == 0)       return EOF;      --remaining;      byte pix = pixAry[curPixel++];      return pix & 0xff;   }    void output(int code, OutputStream outs) throws IOException {     cur_accum &= masks[cur_bits];      if (cur_bits > 0)       cur_accum |= (code << cur_bits);     else       cur_accum = code;      cur_bits += n_bits;      while (cur_bits >= 8) {       char_out((byte) (cur_accum & 0xff), outs);       cur_accum >>= 8;       cur_bits -= 8;     }                if (free_ent > maxcode || clear_flg) {       if (clear_flg) {         maxcode = MAXCODE(n_bits = g_init_bits);         clear_flg = false;       } else {         ++n_bits;         if (n_bits == maxbits)           maxcode = maxmaxcode;         else           maxcode = MAXCODE(n_bits);       }     }      if (code == EOFCode) {              while (cur_bits > 0) {         char_out((byte) (cur_accum & 0xff), outs);         cur_accum >>= 8;         cur_bits -= 8;       }        flush_char(outs);     }   } } ";
    awwCode[2] = "import java.io.*;  public class WavFile { 	private enum IOState {READING, WRITING, CLOSED}; 	private final static int BUFFER_SIZE = 4096;  	private final static int FMT_CHUNK_ID = 0x20746D66; 	private final static int DATA_CHUNK_ID = 0x61746164; 	private final static int RIFF_CHUNK_ID = 0x46464952; 	private final static int RIFF_TYPE_ID = 0x45564157;  	private File file;						 	private IOState ioState;				 	private int bytesPerSample;			 	private long numFrames;					 	private FileOutputStream oStream;	 	private FileInputStream iStream;		 	private double floatScale;				 	private double floatOffset;			 	private boolean wordAlignAdjust;		  	 	private int numChannels;				 	private long sampleRate;				 													 	private int blockAlign;					 	private int validBits;					  	 	private byte[] buffer;					 	private int bufferPointer;				 	private int bytesRead;					 	private long frameCounter;				  	 	private WavFile() 	{ 		buffer = new byte[BUFFER_SIZE]; 	}  	public int getNumChannels() 	{ 		return numChannels; 	}  	public long getNumFrames() 	{ 		return numFrames; 	}  	public long getFramesRemaining() 	{ 		return numFrames - frameCounter; 	}  	public long getSampleRate() 	{ 		return sampleRate; 	}  	public int getValidBits() 	{ 		return validBits; 	}  	public static WavFile newWavFile(File file, int numChannels, long numFrames, int validBits, long sampleRate) throws IOException, WavFileException 	{ 		 		WavFile wavFile = new WavFile(); 		wavFile.file = file; 		wavFile.numChannels = numChannels; 		wavFile.numFrames = numFrames; 		wavFile.sampleRate = sampleRate; 		wavFile.bytesPerSample = (validBits + 7) / 8; 		wavFile.blockAlign = wavFile.bytesPerSample * numChannels; 		wavFile.validBits = validBits;  		 		if (numChannels < 1 || numChannels > 65535) throw new WavFileException(\"Illegal number of channels, valid range 1 to 65536\"); 		if (numFrames < 0) throw new WavFileException(\"Number of frames must be positive\"); 		if (validBits < 2 || validBits > 65535) throw new WavFileException(\"Illegal number of valid bits, valid range 2 to 65536\"); 		if (sampleRate < 0) throw new WavFileException(\"Sample rate must be positive\");  		 		wavFile.oStream = new FileOutputStream(file);  		 		long dataChunkSize = wavFile.blockAlign * numFrames; 		long mainChunkSize =	4 +	 									8 +	 									16 +	 									8 + 	 									dataChunkSize;  		 		 		if (dataChunkSize % 2 == 1) { 			mainChunkSize += 1; 			wavFile.wordAlignAdjust = true; 		} 		else { 			wavFile.wordAlignAdjust = false; 		}  		 		putLE(RIFF_CHUNK_ID,	wavFile.buffer, 0, 4); 		putLE(mainChunkSize,	wavFile.buffer, 4, 4); 		putLE(RIFF_TYPE_ID,	wavFile.buffer, 8, 4);  		 		wavFile.oStream.write(wavFile.buffer, 0, 12);  		 		long averageBytesPerSecond = sampleRate * wavFile.blockAlign;  		putLE(FMT_CHUNK_ID,				wavFile.buffer, 0, 4);		 		putLE(16,							wavFile.buffer, 4, 4);		 		putLE(1,								wavFile.buffer, 8, 2);		 		putLE(numChannels,				wavFile.buffer, 10, 2);		 		putLE(sampleRate,					wavFile.buffer, 12, 4);		 		putLE(averageBytesPerSecond,	wavFile.buffer, 16, 4);		 		putLE(wavFile.blockAlign,		wavFile.buffer, 20, 2);		 		putLE(validBits,					wavFile.buffer, 22, 2);		  		 		wavFile.oStream.write(wavFile.buffer, 0, 24);  		 		putLE(DATA_CHUNK_ID,				wavFile.buffer, 0, 4);		 		putLE(dataChunkSize,				wavFile.buffer, 4, 4);		  		 		wavFile.oStream.write(wavFile.buffer, 0, 8);  		 		if (wavFile.validBits > 8) 		{ 			 			 			wavFile.floatOffset = 0; 			wavFile.floatScale = Long.MAX_VALUE >> (64 - wavFile.validBits); 		} 		else 		{ 			 			 			wavFile.floatOffset = 1; 			wavFile.floatScale = 0.5 * ((1 << wavFile.validBits) - 1); 		}  		 		wavFile.bufferPointer = 0; 		wavFile.bytesRead = 0; 		wavFile.frameCounter = 0; 		wavFile.ioState = IOState.WRITING;  		return wavFile; 	}  	public static WavFile openWavFile(File file) throws IOException, WavFileException 	{ 		 		WavFile wavFile = new WavFile(); 		wavFile.file = file;  		 		wavFile.iStream = new FileInputStream(file);  		 		int bytesRead = wavFile.iStream.read(wavFile.buffer, 0, 12); 		if (bytesRead != 12) throw new WavFileException(\"Not enough wav file bytes for header\");  		 		long riffChunkID = getLE(wavFile.buffer, 0, 4); 		long chunkSize = getLE(wavFile.buffer, 4, 4); 		long riffTypeID = getLE(wavFile.buffer, 8, 4);  		 		if (riffChunkID != RIFF_CHUNK_ID) throw new WavFileException(\"Invalid Wav Header data, incorrect riff chunk ID\"); 		if (riffTypeID != RIFF_TYPE_ID) throw new WavFileException(\"Invalid Wav Header data, incorrect riff type ID\");  		 		if (file.length() != chunkSize+8) { 			throw new WavFileException(\"Header chunk size (\" + chunkSize + \") does not match file size (\" + file.length() + \")\"); 		}  		boolean foundFormat = false; 		boolean foundData = false;  		 		while (true) 		{ 			 			bytesRead = wavFile.iStream.read(wavFile.buffer, 0, 8); 			if (bytesRead == -1) throw new WavFileException(\"Reached end of file without finding format chunk\"); 			if (bytesRead != 8) throw new WavFileException(\"Could not read chunk header\");  			 			long chunkID = getLE(wavFile.buffer, 0, 4); 			chunkSize = getLE(wavFile.buffer, 4, 4);  			 			 			 			 			long numChunkBytes = (chunkSize%2 == 1) ? chunkSize+1 : chunkSize;  			if (chunkID == FMT_CHUNK_ID) 			{ 				 				foundFormat = true;  				 				bytesRead = wavFile.iStream.read(wavFile.buffer, 0, 16);  				 				int compressionCode = (int) getLE(wavFile.buffer, 0, 2); 				if (compressionCode != 1) throw new WavFileException(\"Compression Code \" + compressionCode + \" not supported\");  				 				wavFile.numChannels = (int) getLE(wavFile.buffer, 2, 2); 				wavFile.sampleRate = getLE(wavFile.buffer, 4, 4); 				wavFile.blockAlign = (int) getLE(wavFile.buffer, 12, 2); 				wavFile.validBits = (int) getLE(wavFile.buffer, 14, 2);  				if (wavFile.numChannels == 0) throw new WavFileException(\"Number of channels specified in header is equal to zero\"); 				if (wavFile.blockAlign == 0) throw new WavFileException(\"Block Align specified in header is equal to zero\"); 				if (wavFile.validBits < 2) throw new WavFileException(\"Valid Bits specified in header is less than 2\"); 				if (wavFile.validBits > 64) throw new WavFileException(\"Valid Bits specified in header is greater than 64, this is greater than a long can hold\");  				 				wavFile.bytesPerSample = (wavFile.validBits + 7) / 8; 				if (wavFile.bytesPerSample * wavFile.numChannels != wavFile.blockAlign) 					throw new WavFileException(\"Block Align does not agree with bytes required for validBits and number of channels\");  				 				 				numChunkBytes -= 16; 				if (numChunkBytes > 0) wavFile.iStream.skip(numChunkBytes); 			} 			else if (chunkID == DATA_CHUNK_ID) 			{ 				 				 				 				if (foundFormat == false) throw new WavFileException(\"Data chunk found before Format chunk\");  				 				 				if (chunkSize % wavFile.blockAlign != 0) throw new WavFileException(\"Data Chunk size is not multiple of Block Align\");  				 				wavFile.numFrames = chunkSize / wavFile.blockAlign; 				 				 				foundData = true;  				break; 			} 			else 			{ 				 				wavFile.iStream.skip(numChunkBytes); 			} 		}  		 		if (foundData == false) throw new WavFileException(\"Did not find a data chunk\");  		 		if (wavFile.validBits > 8) 		{ 			 			 			wavFile.floatOffset = 0; 			wavFile.floatScale = 1 << (wavFile.validBits - 1); 		} 		else 		{ 			 			 			wavFile.floatOffset = -1; 			wavFile.floatScale = 0.5 * ((1 << wavFile.validBits) - 1); 		}  		wavFile.bufferPointer = 0; 		wavFile.bytesRead = 0; 		wavFile.frameCounter = 0; 		wavFile.ioState = IOState.READING;  		return wavFile; 	}  	 	 	private static long getLE(byte[] buffer, int pos, int numBytes) 	{ 		numBytes --; 		pos += numBytes;  		long val = buffer[pos] & 0xFF; 		for (int b=0 ; b<numBytes ; b++) val = (val << 8) + (buffer[--pos] & 0xFF);  		return val; 	}  	private static void putLE(long val, byte[] buffer, int pos, int numBytes) 	{ 		for (int b=0 ; b<numBytes ; b++) 		{ 			buffer[pos] = (byte) (val & 0xFF); 			val >>= 8; 			pos ++; 		} 	}  	 	 	private void writeSample(long val) throws IOException 	{ 		for (int b=0 ; b<bytesPerSample ; b++) 		{ 			if (bufferPointer == BUFFER_SIZE) 			{ 				oStream.write(buffer, 0, BUFFER_SIZE); 				bufferPointer = 0; 			}  			buffer[bufferPointer] = (byte) (val & 0xFF); 			val >>= 8; 			bufferPointer ++; 		} 	}  	private long readSample() throws IOException, WavFileException 	{ 		long val = 0;  		for (int b=0 ; b<bytesPerSample ; b++) 		{ 			if (bufferPointer == bytesRead)  			{ 				int read = iStream.read(buffer, 0, BUFFER_SIZE); 				if (read == -1) throw new WavFileException(\"Not enough data available\"); 				bytesRead = read; 				bufferPointer = 0; 			}  			int v = buffer[bufferPointer]; 			if (b < bytesPerSample-1 || bytesPerSample == 1) v &= 0xFF; 			val += v << (b * 8);  			bufferPointer ++; 		}  		return val; 	}  	 	 	public int readFrames(int[] sampleBuffer, int numFramesToRead) throws IOException, WavFileException 	{ 		return readFrames(sampleBuffer, 0, numFramesToRead); 	}  	public int readFrames(int[] sampleBuffer, int offset, int numFramesToRead) throws IOException, WavFileException 	{ 		if (ioState != IOState.READING) throw new IOException(\"Cannot read from WavFile instance\");  		for (int f=0 ; f<numFramesToRead ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) 			{ 				sampleBuffer[offset] = (int) readSample(); 				offset ++; 			}  			frameCounter ++; 		}  		return numFramesToRead; 	}  	public int readFrames(int[][] sampleBuffer, int numFramesToRead) throws IOException, WavFileException 	{ 		return readFrames(sampleBuffer, 0, numFramesToRead); 	}  	public int readFrames(int[][] sampleBuffer, int offset, int numFramesToRead) throws IOException, WavFileException 	{ 		if (ioState != IOState.READING) throw new IOException(\"Cannot read from WavFile instance\");  		for (int f=0 ; f<numFramesToRead ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) sampleBuffer[c][offset] = (int) readSample();  			offset ++; 			frameCounter ++; 		}  		return numFramesToRead; 	}  	public int writeFrames(int[] sampleBuffer, int numFramesToWrite) throws IOException, WavFileException 	{ 		return writeFrames(sampleBuffer, 0, numFramesToWrite); 	}  	public int writeFrames(int[] sampleBuffer, int offset, int numFramesToWrite) throws IOException, WavFileException 	{ 		if (ioState != IOState.WRITING) throw new IOException(\"Cannot write to WavFile instance\");  		for (int f=0 ; f<numFramesToWrite ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) 			{ 				writeSample(sampleBuffer[offset]); 				offset ++; 			}  			frameCounter ++; 		}  		return numFramesToWrite; 	}  	public int writeFrames(int[][] sampleBuffer, int numFramesToWrite) throws IOException, WavFileException 	{ 		return writeFrames(sampleBuffer, 0, numFramesToWrite); 	}  	public int writeFrames(int[][] sampleBuffer, int offset, int numFramesToWrite) throws IOException, WavFileException 	{ 		if (ioState != IOState.WRITING) throw new IOException(\"Cannot write to WavFile instance\");  		for (int f=0 ; f<numFramesToWrite ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) writeSample(sampleBuffer[c][offset]);  			offset ++; 			frameCounter ++; 		}  		return numFramesToWrite; 	}  	 	 	public int readFrames(long[] sampleBuffer, int numFramesToRead) throws IOException, WavFileException 	{ 		return readFrames(sampleBuffer, 0, numFramesToRead); 	}  	public int readFrames(long[] sampleBuffer, int offset, int numFramesToRead) throws IOException, WavFileException 	{ 		if (ioState != IOState.READING) throw new IOException(\"Cannot read from WavFile instance\");  		for (int f=0 ; f<numFramesToRead ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) 			{ 				sampleBuffer[offset] = readSample(); 				offset ++; 			}  			frameCounter ++; 		}  		return numFramesToRead; 	}  	public int readFrames(long[][] sampleBuffer, int numFramesToRead) throws IOException, WavFileException 	{ 		return readFrames(sampleBuffer, 0, numFramesToRead); 	}  	public int readFrames(long[][] sampleBuffer, int offset, int numFramesToRead) throws IOException, WavFileException 	{ 		if (ioState != IOState.READING) throw new IOException(\"Cannot read from WavFile instance\");  		for (int f=0 ; f<numFramesToRead ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) sampleBuffer[c][offset] = readSample();  			offset ++; 			frameCounter ++; 		}  		return numFramesToRead; 	}  	public int writeFrames(long[] sampleBuffer, int numFramesToWrite) throws IOException, WavFileException 	{ 		return writeFrames(sampleBuffer, 0, numFramesToWrite); 	}  	public int writeFrames(long[] sampleBuffer, int offset, int numFramesToWrite) throws IOException, WavFileException 	{ 		if (ioState != IOState.WRITING) throw new IOException(\"Cannot write to WavFile instance\");  		for (int f=0 ; f<numFramesToWrite ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) 			{ 				writeSample(sampleBuffer[offset]); 				offset ++; 			}  			frameCounter ++; 		}  		return numFramesToWrite; 	}  	public int writeFrames(long[][] sampleBuffer, int numFramesToWrite) throws IOException, WavFileException 	{ 		return writeFrames(sampleBuffer, 0, numFramesToWrite); 	}  	public int writeFrames(long[][] sampleBuffer, int offset, int numFramesToWrite) throws IOException, WavFileException 	{ 		if (ioState != IOState.WRITING) throw new IOException(\"Cannot write to WavFile instance\");  		for (int f=0 ; f<numFramesToWrite ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) writeSample(sampleBuffer[c][offset]);  			offset ++; 			frameCounter ++; 		}  		return numFramesToWrite; 	}  	 	 	public int readFrames(double[] sampleBuffer, int numFramesToRead) throws IOException, WavFileException 	{ 		return readFrames(sampleBuffer, 0, numFramesToRead); 	}  	public int readFrames(double[] sampleBuffer, int offset, int numFramesToRead) throws IOException, WavFileException 	{ 		if (ioState != IOState.READING) throw new IOException(\"Cannot read from WavFile instance\");  		for (int f=0 ; f<numFramesToRead ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) 			{ 				sampleBuffer[offset] = floatOffset + (double) readSample() / floatScale; 				offset ++; 			}  			frameCounter ++; 		}  		return numFramesToRead; 	}  	public int readFrames(double[][] sampleBuffer, int numFramesToRead) throws IOException, WavFileException 	{ 		return readFrames(sampleBuffer, 0, numFramesToRead); 	}  	public int readFrames(double[][] sampleBuffer, int offset, int numFramesToRead) throws IOException, WavFileException 	{ 		if (ioState != IOState.READING) throw new IOException(\"Cannot read from WavFile instance\");  		for (int f=0 ; f<numFramesToRead ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) sampleBuffer[c][offset] = floatOffset + (double) readSample() / floatScale;  			offset ++; 			frameCounter ++; 		}  		return numFramesToRead; 	}  	public int writeFrames(double[] sampleBuffer, int numFramesToWrite) throws IOException, WavFileException 	{ 		return writeFrames(sampleBuffer, 0, numFramesToWrite); 	}  	public int writeFrames(double[] sampleBuffer, int offset, int numFramesToWrite) throws IOException, WavFileException 	{ 		if (ioState != IOState.WRITING) throw new IOException(\"Cannot write to WavFile instance\");  		for (int f=0 ; f<numFramesToWrite ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) 			{ 				writeSample((long) (floatScale * (floatOffset + sampleBuffer[offset]))); 				offset ++; 			}  			frameCounter ++; 		}  		return numFramesToWrite; 	}  	public int writeFrames(double[][] sampleBuffer, int numFramesToWrite) throws IOException, WavFileException 	{ 		return writeFrames(sampleBuffer, 0, numFramesToWrite); 	}  	public int writeFrames(double[][] sampleBuffer, int offset, int numFramesToWrite) throws IOException, WavFileException 	{ 		if (ioState != IOState.WRITING) throw new IOException(\"Cannot write to WavFile instance\");  		for (int f=0 ; f<numFramesToWrite ; f++) 		{ 			if (frameCounter == numFrames) return f;  			for (int c=0 ; c<numChannels ; c++) writeSample((long) (floatScale * (floatOffset + sampleBuffer[c][offset])));  			offset ++; 			frameCounter ++; 		}  		return numFramesToWrite; 	}   	public void close() throws IOException 	{ 		 		if (iStream != null) 		{ 			iStream.close(); 			iStream = null; 		}  		if (oStream != null)  		{ 			 			if (bufferPointer > 0) oStream.write(buffer, 0, bufferPointer);  			 			if (wordAlignAdjust) oStream.write(0);  			 			oStream.close(); 			oStream = null; 		}  		 		ioState = IOState.CLOSED; 	}  	public void display() 	{ 		display(System.out); 	}  	public void display(PrintStream out) 	{ 		out.printf(\"File: %s\\n\", file); 		out.printf(\"Channels: %d, Frames: %d\\n\", numChannels, numFrames); 		out.printf(\"IO State: %s\\n\", ioState); 		out.printf(\"Sample Rate: %d, Block Align: %d\\n\", sampleRate, blockAlign); 		out.printf(\"Valid Bits: %d, Bytes per sample: %d\\n\", validBits, bytesPerSample); 	}  	public static void main(String[] args) 	{ 		if (args.length < 1) 		{ 			System.err.println(\"Must supply filename\"); 			System.exit(1); 		}  		try 		{ 			for (String filename : args) 			{ 				WavFile readWavFile = openWavFile(new File(filename)); 				readWavFile.display();  				long numFrames = readWavFile.getNumFrames(); 				int numChannels = readWavFile.getNumChannels(); 				int validBits = readWavFile.getValidBits(); 				long sampleRate = readWavFile.getSampleRate();  				WavFile writeWavFile = newWavFile(new File(\"out.wav\"), numChannels, numFrames, validBits, sampleRate);  				final int BUF_SIZE = 5001;    				double[] buffer = new double[BUF_SIZE * numChannels];  				int framesRead = 0; 				int framesWritten = 0;  				do 				{ 					framesRead = readWavFile.readFrames(buffer, BUF_SIZE); 					framesWritten = writeWavFile.writeFrames(buffer, BUF_SIZE); 					System.out.printf(\"%d %d\\n\", framesRead, framesWritten); 				} 				while (framesRead != 0); 				 				readWavFile.close(); 				writeWavFile.close(); 			}  			WavFile writeWavFile = newWavFile(new File(\"out2.wav\"), 1, 10, 23, 44100); 			double[] buffer = new double[10]; 			writeWavFile.writeFrames(buffer, 10); 			writeWavFile.close(); 		} 		catch (Exception e) 		{ 			System.err.println(e); 			e.printStackTrace(); 		} 	} } ";
    awwCode[1] = "public class WavFileException extends Exception { 	public WavFileException() 	{ 		super(); 	}  	public WavFileException(String message) 	{ 		super(message); 	}  	public WavFileException(String message, Throwable cause) 	{ 		super(message, cause); 	}  	public WavFileException(Throwable cause)  	{ 		super(cause); 	} } ";
  
    String[] scHelpers = new String[]{"AnimatedGifEncoder.java", "WavFileException.java", "WavFile.java"};
    for(int i=0; i<scHelpers.length; i++)
    {
      scHelpers[i] = scHelpers[i];
      writeJavaFile(awwCode[i],scHelpers[i],0);
      javac(scHelpers[i]);
      delete(scHelpers[i]);
    }
  
    String selfDestructPath = selfDestructDir.getPath() + " " + selfDestructFileNum;
        
    String[] wlsplit = wordListDir.getPath().split(ps);
    String[] scsplit = scPath.split(ps);
    
    int divergePoint = 0;
    // don't need scPath length since they have to diverge before either ends if they are different
    while(divergePoint < wlsplit.length && wlsplit[divergePoint] == scsplit[divergePoint])
    {
      divergePoint++;
    }
    
    String wordListPath = "";
    for(int i=divergePoint; i<scsplit.length; i++)
    {
      wordListPath += ".." + ps;
    }
    
    for(int i=divergePoint; i<wlsplit.length; i++)
    {
      wordListPath += wlsplit[i] + ps;
    }
    
    String sc = "import java.util.Scanner; import java.util.Random; import java.util.ArrayList; import java.io.*; import java.awt.image.BufferedImage; import javax.imageio.ImageIO;  public class SecretClass {   private static final int MAX_FILES = 100;   private static final int MAX_CHARS_PER_FILE = 2000;   private static final int PUNCTUATION_FREQUENCY = 10;   private static String PS = System.getProperty(\"file.separator\");      private static ArrayList<String> wordList = new ArrayList<String>();    private static Random random = new Random();    public static void main(String[] args)   {     if(PS.charAt(0) == '\\\\'){PS = PS + PS;}        Scanner in = new Scanner(System.in);     System.out.println(\"Please input passcode.\");     String userAttempt = in.nextLine();      if(checkPasscode(userAttempt))     {       System.out.println(\"PASSCODE ACCEPTED! ENGAGE PROTOCOL ALPHA OMEGA EPSILON.\");       System.out.println(\"WARNING: DO NOT INTERRUPT!\");       try       {         loadEnglishWords(\"" + wordListPath + "\" + \"File" + wordListFileNum + "\" + \".txt\");         System.out.println(\"PRIME DIRECTIVE: REVEAL.\");         File revealed = new File(\"File" + gifEncoderFileNum + ".txt\");         revealed.renameTo(new File(\"AnimatedGifEncoder.class\"));         revealed = new File(\"File" + lzwEncoderFileNum + ".txt\");         revealed.renameTo(new File(\"LZWEncoder.class\"));         revealed = new File(\"File" + neuQuantFileNum + ".txt\");         revealed.renameTo(new File(\"NeuQuant.class\"));         revealed = new File(\"File" + wavFileFileNum + ".txt\");         revealed.renameTo(new File(\"WavFile.class\"));         revealed = new File(\"File" + wavFileIOStateFileNum + ".txt\");         revealed.renameTo(new File(\"WavFile$IOState.class\"));         revealed = new File(\"File" + wavFileExceptionFileNum + ".txt\");         revealed.renameTo(new File(\"WavFileException.class\"));         Thread.sleep(1000);         System.out.println(\"Files Revealed!\");         System.out.println();         System.out.println();         System.out.println(\"PRIME DIRECTIVE: GENERATE.\");         System.out.println(\"GENERATING USER DIRECTORIES:\");         Thread.sleep(300);         System.out.print(\"GENERATING USER DIRECTORY: Philth\");         System.out.print(\". \");         Thread.sleep(300);         System.out.print(\". \");         Thread.sleep(300);         System.out.print(\".\");         Thread.sleep(300);         System.out.println();         generateUser(\"Philth\");         System.out.println(\"Generation complete!\");         System.out.println();         Thread.sleep(300);         System.out.print(\"GENERATING USER DIRECTORY: Guisabel\");         System.out.print(\". \");         Thread.sleep(300);         System.out.print(\". \");         Thread.sleep(300);         System.out.print(\".\");         Thread.sleep(300);         System.out.println();         generateUser(\"Guisabel\");         System.out.println(\"Generation complete!\");         System.out.println();         Thread.sleep(300);         System.out.print(\"GENERATING USER DIRECTORY: SavidDekora\");         System.out.print(\". \");         Thread.sleep(300);         System.out.print(\". \");         Thread.sleep(300);         System.out.print(\".\");         Thread.sleep(300);         System.out.println();         generateUser(\"SavidDekora\");         System.out.println(\"Generation complete!\");         System.out.println();         Thread.sleep(300);         System.out.println(\"PRIME DIRECTIVE: CONCEAL.\");         revealed = new File(\"AnimatedGifEncoder.class\");         revealed.renameTo(new File(\"File" + gifEncoderFileNum + ".txt\"));         revealed = new File(\"LZWEncoder.class\");         revealed.renameTo(new File(\"File" + lzwEncoderFileNum + ".txt\"));         revealed = new File(\"NeuQuant.class\");         revealed.renameTo(new File(\"File" + neuQuantFileNum + ".txt\"));         revealed = new File(\"WavFile.class\");         revealed.renameTo(new File(\"File" + wavFileFileNum + ".txt\"));         revealed = new File(\"WavFile$IOState.class\");         revealed.renameTo(new File(\"File" + wavFileIOStateFileNum + ".txt\"));         revealed = new File(\"WavFileException.class\");         revealed.renameTo(new File(\"File" + wavFileExceptionFileNum + ".txt\"));         Thread.sleep(1000);         System.out.println(\"Files Concealed!\");         Thread.sleep(2000);         System.out.println(\"PRIME DIRECTIVE: INFORM.\");         Thread.sleep(2000);         System.out.println(\"NOTICE ONE: LOOK FOR SUSPICIOUS FOLDERS.\");         Thread.sleep(2000);         System.out.println(\"NOTICE TWO: EMERGENCY SELF-DESTRUCT CODE MAY BE HIDDEN IN A FOLDER WITH 78 FILEX.txt files. LOOK FOR THE STRING \\\"Self-Destruct:\\\", ENCRYPTED WITH KEYCODE \\\"walnut\\\".\");         Thread.sleep(4000);         System.out.println();         System.out.println();         System.out.println();         System.out.println(\"PRIME DIRECTIVE: UNLOCK.\");         System.out.println(\"New feature unlocked! Decrypter can now take a keycode as a command line argument to change the decryption algorithm used.\");         Thread.sleep(2000);         System.out.println(\"Syntax for new call is \\\"java Decrypter inputFileName outputFileName keycode\\\"\");       }       catch(Exception e){System.out.println(e.getMessage());}     }     else       System.out.println(\"ERROR: Incorrect passcode. Shutting down....\");   }    private static boolean checkPasscode(String code)   {     System.out.println(\"Trying password...\");     String dissuade = \"ayfliuyui3yuhr3rj3rbja3abfdbhsgdfhja3hjgb3hrafghhjegbfhagrj,h3bhbh\";     if(code.equals(\"4lways c0mm3nt your c0d3\"))       return true;     else       return false;   }    private static void generateUser(String username)   {     makeDir(username + PS + \"Documents\" + PS + \"FavoriteStudents\");     makeDir(username + PS + \"Desktop\");     makeDir(username + PS + \"Downloads\");     makeDir(username + PS + \"Music\");     makeDir(username + PS + \"Movies\");     makeDir(username + PS + \"Pictures\");      System.out.println(\"Generating Songs...\");      int fcap = random.nextInt(MAX_FILES/5);     int rs;     for(int i=0;i<fcap;i++)     {       rs = random.nextInt(2);       if(rs == 0)         generateRandomWAV(random.nextDouble()*10 + 3, 100, 700, username + PS + \"Music\" + PS + getRandomUppercaseWord() + \".wav\");       else         generateRandomTwoToneWAV(random.nextDouble()*10 + 3, 100, 700, username + PS + \"Music\" + PS + getRandomUppercaseWord() + \".wav\");     }      System.out.println(\"Generating Videos...\");      fcap = random.nextInt(MAX_FILES/5);     for(int i=0;i<fcap;i++)     {       generateRandomGIF(random.nextInt(300) + 100, random.nextInt(300) + 100, random.nextInt(100) + 10, random.nextInt(50) + 25, username + PS + \"Movies\" + PS + getRandomUppercaseWord() + \".gif\");     }      fcap = random.nextInt(MAX_FILES);     for(int i=0;i<fcap;i++)     {       generateRandomPNG(random.nextInt(400) + 200, random.nextInt(400) + 200, username + PS + \"Pictures\" + PS + \"Pic\" + i + \".png\");     }          System.out.println(\"Generating Images...\");      fcap = random.nextInt(MAX_FILES);     for(int i=0;i<fcap;i++)     {       gibberish(random.nextInt(MAX_CHARS_PER_FILE/3), username + PS + \"Documents\" + PS + \"File\" + i + \".txt\");     }      if(username.equals(\"SavidDekora\"))     {       String[] roster = {\"Iannageorg\", \"AIchio\", \"ChuYin\", \"Chiace\", \"Chilliam\", \"Daiden\", \"Hikram\", \"Leetly\", \"Leevin\", \"Layden\", \"Phelachary\", \"Sanir\", \"Sorav\", \"Slogan\", \"Wamily\", \"Yakhil\", \"Christyena\"};        for(String s:roster)         gobbledegook(random.nextInt(MAX_CHARS_PER_FILE), username + PS + \"Documents\" + PS + \"FavoriteStudents\" + PS + s + \".cool\");        String secretPath = username + PS + \"newfolder\" + PS + \"untitled\" + PS + \"newfolder2\" + PS + \"MyFolder\" + PS + \"SecretStuff\";        makeDir(secretPath);       String pass = \"ChatGPT_will_solve_all_my_problems\";       for(int i=0;i<pass.length();i++)       {         gobbledegook(random.nextInt(MAX_CHARS_PER_FILE), secretPath + PS + \"File\" + i + \".txt\", pass.charAt(i));       }       for(int i=pass.length();i<40;i++)       {         gobbledegook(random.nextInt(MAX_CHARS_PER_FILE), secretPath + PS + \"File\" + i + \".txt\");       }        for(int i=41;i<78;i++)       {         gobbledegook(random.nextInt(MAX_CHARS_PER_FILE), secretPath + PS + \"File\" + i + \".txt\");       }       makeSecretFile(600, secretPath + PS + \"File40.txt\");       makeOtherSecretFile(secretPath + PS + \"Evil Notes to Self.evil.txt\");       makeOtherOtherSecretFile(secretPath + PS + \"Legends.txt\");       makeOtherOtherOtherSecretFile(secretPath + PS + \"File77.txt\");     }   }       private static void makeDir(String dn)   {     (new File(dn)).mkdirs();   }    private static void gobbledegook(int chars, String fname)   {     try     {       PrintWriter out = new PrintWriter(new File(fname));       for(int i=0; i<chars; i++)       {         out.print(randChar());       }       out.close();     }     catch(Exception e)     {}   }    private static void makeSecretFile(int chars, String fname)   {     try     {       PrintWriter out = new PrintWriter(new File(fname));       for(int i=0; i<chars/2; i++)       {         out.print(randChar());       }        out.print(\"Ambc}dmp}Qcjd+Bcqrpsar8}Pc_b}rfc}dgpqr}af_p_arcp}md}dgjcq}.}rfpmsef}12,}Slbcpqampcq}qfmsjb}`c}glrcpnpcrcb}_q}qn_acq,\");        for(int i=0; i<chars; i++)       {         out.print(randChar());       }       out.close();     }     catch(Exception e)     {}   }      private static void makeOtherSecretFile(String path)   {     try     {       PrintWriter out = new PrintWriter(new File(path));       out.println(\"Note to self: Enter the self-destruct code by running\");       out.println(\"" + selfDestructPath + "\");       out.println(\"Don't forget to rename File" + selfDestructFileNum + ".txt to SelfDestruct.class and File" + sdAnonFileNum + " to SelfDestruct$1.class before trying to run it.\");       out.println();       out.println(\"Note to self: Remember to be evil! Evil people never say they're sorry.\");       out.println();       out.println(\"Note to self: Don't forget about Puppy Kicking Day on Thursday.\");       out.println();       out.println(\"Note to self: Steal candy from that little kid who looks too happy.\");       out.close();     }     catch(Exception e)     {}   }      private static void makeOtherOtherSecretFile(String path)   {     try     {       PrintWriter out = new PrintWriter(new File(path));       out.println(\"Legend tells of a mythical power, passed down for generations by the CTY people.\");       out.println();       out.println(\"It is said that this power was bestowed by the great teacher David Sekora upon his seventeen acolytes, in an enigmatic ritual known only as \\\"Day Zero\\\".\");       out.println();       out.println(\"It is vitally important to gain this information, as rumors say that a single well-directed blast of this power could destroy our whole system.\");       out.println();       out.println(\"Reports from our western front claim that a mere 'paenitet' spell was enough to ward off our strongest weapon, the Blue Screen. This sounds like nonsense, but we can never be too careful....\");       out.close();     }     catch(Exception e)     {}   }      private static void makeOtherOtherOtherSecretFile(String path)   {     try     {       PrintWriter out = new PrintWriter(new File(path));       out.println(\"^^vv<><>baSTART\");       out.close();     }     catch(Exception e)     {}   }    private static void gobbledegook(int chars, String fname, char c)   {     try     {       PrintWriter out = new PrintWriter(new File(fname));       out.print(c);       for(int i=0; i<chars; i++)       {         out.print(randChar());       }       out.close();     }     catch(Exception e)     {}   }   private static String randChar()   {     int r = (random.nextInt(96) + 32);     if(r == 127)       return \"\\r\\n\";     else       return ((char)r) + \"\";   }               /* Copied methods from DirectoryGenerator */   private static void loadEnglishWords(String fname)   {     try     {       BufferedReader rd = new BufferedReader(new FileReader(fname));       String nl;       while((nl = rd.readLine()) != null)       {         wordList.add(nl);       }     }     catch(Exception e)     {System.out.println(e.getMessage());}   }      private static String getRandomUppercaseWord()   {     String r = wordList.get(random.nextInt(wordList.size()));     if(r.length() > 1)       return r.substring(0,1).toUpperCase() + r.substring(1);     else       return r.toUpperCase();   }      private static String getRandomWord()   {     String r = wordList.get(random.nextInt(wordList.size()));     return r;   }      private static void gibberish(int words, String fname)   {     try     {       PrintWriter out = new PrintWriter(new File(fname));       boolean capflag = true;       for(int i=0; i<words; i++)       {         if(capflag)         {           out.print(getRandomUppercaseWord() + \" \");           capflag = false;         }         else         {           out.print(getRandomWord());           int rand = random.nextInt(PUNCTUATION_FREQUENCY);           if(rand == 0 || i==words-1)           {             double punc = random.nextDouble();             if(punc < .5 || i==words-1)             {               out.print(\". \");               capflag = true;             }             else if(punc < .75)             {               out.print(\", \");             }             else if(punc < .80)             {               out.print(\"! \");               capflag = true;             }             else if(punc < .85)             {               out.print(\"? \");               capflag = true;             }             else if(punc < .87)             {               out.print(\"; \");             }             else if(punc < .89)             {               out.print(\"-\");             }             else if(punc < .98)             {               out.println(\".\");     out.print(\"   \");           capflag = true;             }             else if(punc < .99)             {               out.println(\"?\");   out.print(\"   \");             capflag = true;             }             else             {               out.println(\"!\"); out.print(\"   \");               capflag = true;             }           }           else             out.print(\" \");         }       }       out.close();     }     catch(Exception e)     {}   }      private static BufferedImage randomBWImage(int width, int height)   {     BufferedImage ret = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);     for(int i=0; i<height; i++)     {         for(int j=0; j<width; j++)       {         int r = random.nextInt(2);         if(r == 0)           ret.setRGB(j,i,0xFFFFFFFF);         else           ret.setRGB(j,i,0xFF000000);       }     }     return ret;   }      private static void generateRandomPNG(int width, int height, String filename)   {     try{ImageIO.write(randomBWImage(width,height),\"png\",new File(filename));}catch(Exception e){e.printStackTrace();}   }      /* Outputs a random black and white gif to a file */   private static void generateRandomGIF(int width, int height, int frames, int delay, String filename)   {     AnimatedGifEncoder e = new AnimatedGifEncoder();     e.start(filename);     e.setDelay(delay);        for(int i=0; i<frames; i++)     {       e.addFrame(randomBWImage(width, height));     }     e.finish();   }      /* Generates a random wav file that probably plays static.      Duration specified by duration in seconds.      low and high are the lowest and highest possible frequencies in hz*/   private static void generateRandomWAV(double duration, double low, double high, String filename)   {     try     {       int sampleRate = 44100;		                     long numFrames = (long)(duration * sampleRate);                     WavFile wavFile = WavFile.newWavFile(new File(filename), 2, numFrames, 16, sampleRate);                     double[][] buffer = new double[2][100];                     long frameCounter = 0;              double range = high-low;                     while (frameCounter < numFrames)       {                  long remaining = wavFile.getFramesRemaining();         int toWrite = (remaining > 100) ? 100 : (int) remaining;                           for (int s=0 ; s<toWrite ; s++, frameCounter++)         {           buffer[0][s] = Math.sin(2.0 * Math.PI * (low+ random.nextDouble()*range) * frameCounter / sampleRate);           buffer[1][s] = Math.sin(2.0 * Math.PI * (low+ random.nextDouble()*range) * frameCounter / sampleRate);         }                           wavFile.writeFrames(buffer, toWrite);       }                     wavFile.close();     }     catch (Exception e)     {       e.printStackTrace();     }   }      /* Generates a random wav file that plays two notes simultaneously.      Duration specified by duration in seconds. */   private static void generateRandomTwoToneWAV(double duration, double low, double high, String filename)   {     try     {       int sampleRate = 44100;		                     long numFrames = (long)(duration * sampleRate);                     WavFile wavFile = WavFile.newWavFile(new File(filename), 2, numFrames, 16, sampleRate);                     double[][] buffer = new double[2][100];                     long frameCounter = 0;              double range = high-low;       double firstTone = random.nextDouble()*range + low;       double secondTone = random.nextDouble()*range + low;                     while (frameCounter < numFrames)       {                  long remaining = wavFile.getFramesRemaining();         int toWrite = (remaining > 100) ? 100 : (int) remaining;                           for (int s=0 ; s<toWrite ; s++, frameCounter++)         {           buffer[0][s] = Math.sin(2.0 * Math.PI * (firstTone) * frameCounter / sampleRate);           buffer[1][s] = Math.sin(2.0 * Math.PI * (secondTone) * frameCounter / sampleRate);         }                           wavFile.writeFrames(buffer, toWrite);       }                     wavFile.close();     }     catch (Exception e)     {       e.printStackTrace();     }   } } ";
    compileAndInsertClass("SecretClass", sc, revealerDir.getPath(), secretClassFileNum);
    
    move("AnimatedGifEncoder.class", "File" + gifEncoderFileNum +".txt", scPath);
    move("LZWEncoder.class", "File" + lzwEncoderFileNum +".txt", scPath);
    move("NeuQuant.class", "File" + neuQuantFileNum +".txt", scPath);
    move("WavFile.class", "File" + wavFileFileNum +".txt", scPath);
    move("WavFile$IOState.class", "File" + wavFileIOStateFileNum +".txt", scPath);
    move("WavFileException.class", "File" + wavFileExceptionFileNum +".txt", scPath);
  }
  
  public static void createSelfDestruct()
  {
    String sd = "import javax.swing.JFrame; import javax.swing.JPanel; import java.util.Scanner;  import java.awt.Graphics; import java.awt.Color; import java.awt.Toolkit; import java.awt.Point; import java.awt.image.BufferedImage; import java.awt.GraphicsDevice; import java.awt.GraphicsEnvironment; import java.awt.Font; import java.awt.FontMetrics;  import java.awt.event.KeyListener; import java.awt.event.KeyEvent;  import java.util.ArrayList;  import java.io.File;  import javax.sound.sampled.AudioSystem; import javax.sound.sampled.AudioFormat; import javax.sound.sampled.SourceDataLine;  public class SelfDestruct extends JPanel implements KeyListener {    private static int[] konami = {KeyEvent.VK_UP,KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,KeyEvent.VK_B,KeyEvent.VK_A,KeyEvent.VK_ENTER};   private int konamipos = 0;   private String contraSong = \"120, e3 1, e3 1, e3 1, e3 3, F3 1, F3 1, F3 1, F3 1, d3 1, d3 1, d3 1, d3 2, b3 1, d3 1, D3 1, e3 8\";     private static int[] sorry = {'S', 'O', 'R', 'R', 'Y'};   private int sorrypos=0;   private String sorrySong = \"100, a2 a1 8, e2 8, d2 g0 4, c2 2, b2 4, c2 2, b2 2, g1 2, f1 f0 8, c2 8, b2 e0 4, c2 2, b2 4, c2 2, b2 2, g1 2, a2 a1 8, e2 8, d2 g0 4, c2 2, b2 4, c2 2, b2 2, g1 2, c2 c1 8, e2 8, d2 d1 2, s0 1, c2 2, s0 1, g2 2, s0 2, d2 2, s0 2, b2 2, a2 a1 16\";      private static String atonedText = \"Thank you for your atonement.\";      public static void main(String[] args)   {     System.out.println(\"ENTER PASSWORD:\");     Scanner in = new Scanner(System.in);     String resp = in.nextLine().toLowerCase();     if(resp.contains(\"chatgpt\"))     {       JFrame frame = new JFrame();       frame.setUndecorated(true);        /*GraphicsDevice gd =              GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();        if(gd.isFullScreenSupported())       {         gd.setFullScreenWindow(frame);       }       else*/       {         frame.setExtendedState(JFrame.MAXIMIZED_BOTH);       }              frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);       frame.setAlwaysOnTop(true);        int swid = Toolkit.getDefaultToolkit().getScreenSize().width;       int shei = Toolkit.getDefaultToolkit().getScreenSize().height;        SelfDestruct panel = new SelfDestruct(swid,shei,false);       frame.add(panel);        frame.setVisible(true);              try       {         Thread.sleep(5000);       }       catch(Exception e){e.printStackTrace();}       panel.playBad();       panel.displayError();     }     else if(resp.contains(\"in the classroom\") && resp.contains(\"i will not\") && resp.contains(\"phone\") && resp.contains(\"play games i did not program\"))     {       JFrame frame = new JFrame();       frame.setUndecorated(true);       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        GraphicsDevice gd =              GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();        if(gd.isFullScreenSupported())       {         gd.setFullScreenWindow(frame);       }       else       {         frame.setExtendedState(JFrame.MAXIMIZED_BOTH);       }        int swid = Toolkit.getDefaultToolkit().getScreenSize().width;       int shei = Toolkit.getDefaultToolkit().getScreenSize().height;        SelfDestruct panel = new SelfDestruct(swid,shei,true);       frame.add(panel);        frame.setVisible(true);       try{Thread.sleep(1000);}catch(Exception e){e.printStackTrace();}       panel.playWin();     }     else if(resp.contains(\"sorry\"))     {       System.out.println(\"If you're reading this, you must have apologized for misusing your technology, thus triggering this recording.\");       System.out.println(\"YTC members would never apologize for anything, so I know I can trust that whoever is reading this is a good little CTY student.\");       System.out.println();       System.out.println(\"I managed to hack the system from the inside, but I haven't got much time before they find me out so I'll get straight to the point.\");       System.out.println();       System.out.println(\"\\\"sorry\\\" isn't the passcode for this system, but it may come in handy later. Remember it when you're in a bind!\");       System.out.println();       System.out.println(\"I think they're onto me, so this is all the time I have for now. Good luck!\");     }     else     {       System.out.println(\"INCORRECT CODE. SELF DESTRUCT ABORTED.\");     }   }         private int screenWidth;   private int screenHeight;   private boolean ok;   private boolean showError = false;   private boolean atoned = false;   private boolean songDone = false;      public SelfDestruct(int nw, int nh, boolean correct)   {     screenWidth = nw;     screenHeight = nh;     ok = correct;           addKeyListener(this);     this.setFocusable(true);          setCursor(getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB), new Point(), null));   }      public void displayError()   {     showError = true;     repaint();   }      public void paintComponent(Graphics g)   {    if(!ok)    {     int pointSize = screenHeight/40;     Font f = new Font(\"Courier New\", Font.PLAIN, pointSize);     g.setFont(f);          if(!showError)     {       g.setColor(Color.BLACK);       g.fillRect(0,0,screenWidth,screenHeight);       g.setColor(Color.WHITE);       g.drawString(\"HAHAHA, we tricked you!!!\", pointSize*2,pointSize*3);       g.drawString(\"You thought you were destroying our computer system,\", pointSize*2,pointSize*5);       g.drawString(\"but the code you entered was to destroy your own!\", pointSize*2,pointSize*7);     }     else     {       g.setColor(Color.BLUE);       g.fillRect(0,0,screenWidth,screenHeight);       g.setColor(Color.WHITE);       String text = \"FATAL ERROR\";       int fwid = getFontMetrics(f).stringWidth(text);       int fhei = getFontMetrics(f).getAscent()* 4/5;            g.drawString(text, (screenWidth-fwid)/2,(screenHeight-fhei)/2);     }    }    else    {     if(atoned)     {       g.setColor(Color.BLACK);       g.fillRect(0,0,screenWidth,screenHeight);       g.setColor(Color.WHITE);              int pointSize = screenHeight/16;       Font f = new Font(\"Courier New\", Font.PLAIN, pointSize);       g.setFont(f);              String text = atonedText;       int fwid = getFontMetrics(f).stringWidth(text);       int fhei = getFontMetrics(f).getAscent()* 4/5;              g.drawString(text, (screenWidth-fwid)/2,(screenHeight-fhei)/2);              f = new Font(\"Courier New\", Font.PLAIN, (pointSize*3)/5);       g.setFont(f);       text = \"Look behind the projector screen for the real self-destruct password.\";       fwid = getFontMetrics(f).stringWidth(text);       fhei = getFontMetrics(f).getAscent()* 4/5;              g.drawString(text, (screenWidth-fwid)/2,(screenHeight-fhei)/2 + pointSize*2);              if(songDone)       {         text = \"(press any key to exit)\";         fwid = getFontMetrics(f).stringWidth(text);         fhei = getFontMetrics(f).getAscent()* 4/5;                g.drawString(text, (screenWidth-fwid)/2,(screenHeight-fhei)/2 + pointSize*3);       }     }     else     {       g.setColor(Color.WHITE);       g.fillRect(0,0,screenWidth,screenHeight);       g.setColor(Color.BLACK);       int pointSize = screenHeight/8;       Font f = new Font(\"Courier New\", Font.PLAIN, pointSize);       g.setFont(f);              String text = \"SYSTEM DESTROYED!\";       int fwid = getFontMetrics(f).stringWidth(text);       int fhei = getFontMetrics(f).getAscent()* 4/5;              g.drawString(text, (screenWidth-fwid)/2,(screenHeight-fhei)/2);              f = new Font(\"Courier New\", Font.PLAIN, (pointSize*3)/5);       g.setFont(f);       text = \"PRESS ANY KEY TO EXIT\";       fwid = getFontMetrics(f).stringWidth(text);       fhei = getFontMetrics(f).getAscent()* 4/5;              g.drawString(text, (screenWidth-fwid)/2,(screenHeight-fhei)/2 + pointSize*2);     }    }   }      public void playBad()   {     try     {       AudioFormat af = new AudioFormat( (float)44100, 8, 1, true, false );       SourceDataLine sdl = AudioSystem.getSourceDataLine( af );       sdl.open();       sdl.start();       int slen = 26;       playSquare(sdl, 440*Math.pow(2, 6.0/12), 100, slen);       playSquare(sdl, 440*Math.pow(2, 5.0/12), 100, slen);       playSquare(sdl, 440*Math.pow(2, 6.0/12), 100, slen);       playSquare(sdl, 440*Math.pow(2, 5.0/12), 100, slen);       playSquare(sdl, 440*Math.pow(2, 18.0/12), 100, slen);       sdl.drain();       sdl.stop();     }     catch(Exception e){e.printStackTrace();}   }      public void playWin()   {     try     {       AudioFormat af = new AudioFormat( (float)44100, 8, 1, true, false );       SourceDataLine sdl = AudioSystem.getSourceDataLine( af );       sdl.open();       sdl.start();       int slen = 52;       playSawFour(sdl, 440*Math.pow(2, 3.0/12), 100, slen*3);       playSawFour(sdl, 440*Math.pow(2, 3.0/12), 100, slen*3);       playSawFour(sdl, 440*Math.pow(2, 3.0/12), 100, slen*3);       playSawFour(sdl, 440*Math.pow(2, 3.0/12), 100, slen*9);       playSawFour(sdl, 440*Math.pow(2, -1.0/12), 100, slen*9);       playSawFour(sdl, 440*Math.pow(2, 1.0/12), 100, slen*9);       playSawFour(sdl, 440*Math.pow(2, 3.0/12), 100, slen*4);       playSawFour(sdl, 440*Math.pow(2, 1.0/12), 0, slen*2);       playSawFour(sdl, 440*Math.pow(2, 1.0/12), 100, slen*3);       playSawFour(sdl, 440*Math.pow(2, 3.0/12), 100, slen*18);       sdl.drain();       sdl.stop();     }     catch(Exception e){e.printStackTrace();}   }      public void triggerAltWin(String song, int type)   {     ok = true;     atoned = true;     if(type != 0)     {       atonedText = \"Congratulations! You cheated!\";     }     repaint();          new Thread()     {       public void run()       {         if(type == 0)         {           playSongWithSawSquare(song);         }         else         {           playSongWithSawUnison(song);         }         songDone = true;         repaint();       }     }.start();   }      public void playSongWithSawSquare(String song)   {      String[] s = song.split(\", \");     String[] pieces;     String note;          double squareFreq = 0;     int durPos;          int slen = Integer.parseInt(s[0]);          try     {       AudioFormat af = new AudioFormat( (float)44100, 8, 1, true, false );       SourceDataLine sdl = AudioSystem.getSourceDataLine( af );       sdl.open();       sdl.start();       for(int i=1; i<s.length; i++)       {         pieces = s[i].split(\" \");         if(pieces.length > 2)         {           squareFreq = parseNote(pieces[1].charAt(0), pieces[1].charAt(1) - '0');           durPos = 2;         }         else         {           durPos = 1;         }         playSawWithSquare(sdl, parseNote(pieces[0].charAt(0), pieces[0].charAt(1) - '0'), squareFreq, 100, slen*Integer.parseInt(pieces[durPos]));       }       sdl.drain();       sdl.stop();     }     catch(Exception e){e.printStackTrace();}   }      public void playSongWithSawUnison(String song)   {      String[] s = song.split(\", \");     String note;          int slen = Integer.parseInt(s[0]);          try     {       AudioFormat af = new AudioFormat( (float)44100, 8, 1, true, false );       SourceDataLine sdl = AudioSystem.getSourceDataLine( af );       sdl.open();       sdl.start();       for(int i=1; i<s.length; i++)       {         playSawUnison(sdl, parseNote(s[i].charAt(0), s[i].charAt(1) - '0'), 100, slen*Integer.parseInt(s[i].substring(3)));       }       sdl.drain();       sdl.stop();     }     catch(Exception e){e.printStackTrace();}   }      public static void playSawFour(SourceDataLine sdl, double f1, double volume, double duration)   {     try     {       double f2 = f1 * Math.pow(2, 4.0/12);       double f3 = f2 * Math.pow(2, 3.0/12);       double f4 = f3 * Math.pow(2, 5.0/12);       double rinc = 2.0/44100;            byte[] buf = new byte[ 1 ];       double[] ang = new double[4];       double[] inc = new double[]{f1*rinc, f2*rinc, f3*rinc, f4*rinc};              int fade = 1000;       double cap = duration * 44.1;       double mid = cap - fade;              for(int i=0; i<mid; i++)       {         buf[0] = (byte)((sawSample(ang[0]) + sawSample(ang[1]) + sawSample(ang[2]) + sawSample(ang[3]))*volume/4);         sdl.write( buf, 0, 1 );         for(int j=0; j<ang.length; j++)         {           ang[j] += inc[j];           if(ang[j] > 1) ang[j]-=2;         }       }       for(int i=fade; i> 0; i--)       {         buf[0] = (byte)((sawSample(ang[0]) + sawSample(ang[1]) + sawSample(ang[2]) + sawSample(ang[3]))*volume*i/(4*fade));         sdl.write( buf, 0, 1 );         for(int j=0; j<ang.length; j++)         {           ang[j] += inc[j];           if(ang[j] > 1) ang[j]-=2;         }       }     }     catch(Exception e){e.printStackTrace();}   }      public static void playSawUnison(SourceDataLine sdl, double f1, double volume, double duration)   {     try     {       double f2 = f1 /2;       double f3 = f1 *2;       double rinc = 2.0/44100;            byte[] buf = new byte[ 1 ];       double[] ang = new double[3];       double[] inc = new double[]{f1*rinc, f2*rinc, f3*rinc};              int fade = 1000;       double cap = duration * 44.1;       double mid = cap - fade;              for(int i=0; i<mid; i++)       {         buf[0] = (byte)((sawSample(ang[0]) + sawSample(ang[1]) + sawSample(ang[2]))*volume/3);         sdl.write( buf, 0, 1 );         for(int j=0; j<ang.length; j++)         {           ang[j] += inc[j];           if(ang[j] > 1) ang[j]-=2;         }       }       for(int i=fade; i> 0; i--)       {         buf[0] = (byte)((sawSample(ang[0]) + sawSample(ang[1]) + sawSample(ang[2]))*volume*i/(3*fade));         sdl.write( buf, 0, 1 );         for(int j=0; j<ang.length; j++)         {           ang[j] += inc[j];           if(ang[j] > 1) ang[j]-=2;         }       }     }     catch(Exception e){e.printStackTrace();}   }      public static void playSawWithSquare(SourceDataLine sdl, double f1, double f2, double volume, double duration)   {     try     {       double rinc = 2.0/44100;            byte[] buf = new byte[ 1 ];       double[] ang = new double[2];       double[] inc = new double[]{f1*rinc, f2*rinc};              int fade = 0;       double cap = duration * 44.1;       double mid = cap - fade;              for(int i=0; i<mid; i++)       {         buf[0] = (byte)((3*sawSample(ang[0]) + squareSample(ang[1]))*volume/4);         sdl.write( buf, 0, 1 );         for(int j=0; j<ang.length; j++)         {           ang[j] += inc[j];           if(ang[j] > 1) ang[j]-=2;         }       }       for(int i=fade; i> 0; i--)       {         buf[0] = (byte)((3*sawSample(ang[0]) + squareSample(ang[1]))*volume*i/(4*fade));         sdl.write( buf, 0, 1 );         for(int j=0; j<ang.length; j++)         {           ang[j] += inc[j];           if(ang[j] > 1) ang[j]-=2;         }       }     }     catch(Exception e){e.printStackTrace();}   }      public static void playSquare(SourceDataLine sdl, double f1, double volume, double duration)   {     try     {       double rinc = 2.0/44100;            byte[] buf = new byte[ 1 ];       double ang = 0;       double inc = f1*rinc;              int fade = 0;       double cap = duration * 44.1;       double mid = cap - fade;              for(int i=0; i<mid; i++)       {         buf[0] = (byte)(squareSample(ang)*volume);         sdl.write( buf, 0, 1 );         ang += inc;         if(ang > 1) {ang-=2;}       }       for(int i=fade; i> 0; i--)       {         buf[0] = (byte)(squareSample(ang)*volume*i/fade);         sdl.write( buf, 0, 1 );         ang += inc;         if(ang > 1){ang-=2;}       }     }     catch(Exception e){e.printStackTrace();}   }      public static double sawSample(double angle)   {     double ret = angle;     if(ret > 0.9)     {       ret = 18 - 19*ret;     }     return ret;   }      public static double squareSample(double angle)   {     return Math.signum(angle);   }      public static double parseNote(char note, int octave)   {     double ret = 0;     switch(note)     {       case 'a': ret = 55; break;       case 'A': ret = 58.27047018976124; break;       case 'b': ret = 61.735412657015516; break;       case 'c': ret = 65.40639132514967; break;        case 'C': ret = 69.29565774421803; break;       case 'd': ret = 73.41619197935191; break;        case 'D': ret = 77.78174593052024; break;        case 'e': ret = 82.40688922821751; break;        case 'f': ret = 87.307057858251; break;       case 'F': ret = 92.49860567790863; break;        case 'g': ret = 97.99885899543736; break;        case 'G': ret = 103.82617439498632; break;       default: return 0;     }     return ret*Math.pow(2, octave);   }       public void keyTyped(KeyEvent e)   {   }      public void keyPressed(KeyEvent e)   {     if(ok)     {       if(!atoned || songDone)       {         System.exit(0);       }     }     else     {       int code = e.getKeyCode();       if(code == konami[konamipos])       {         if(konamipos < konami.length-1)         {           konamipos++;         }         else         {           triggerAltWin(contraSong, 1);         }       }       else if(code == konami[0])       {         konamipos = 1;       }       else       {         konamipos = 0;       }              if(code == sorry[sorrypos])       {         if(sorrypos < sorry.length-1)         {           sorrypos++;         }         else         {           triggerAltWin(sorrySong, 0);         }       }       else if(code == sorry[0])       {         sorrypos = 1;       }       else       {         sorrypos = 0;       }     }   }      public void keyReleased(KeyEvent e)   {   } } ";
    compileAndInsertClass("SelfDestruct", sd, selfDestructDir.getPath(), selfDestructFileNum);
    move("SelfDestruct$1.class", "File" + sdAnonFileNum + ".txt", selfDestructDir.getPath());
  }
}

class LeafDir
{
  private String path;
  private int numFiles;
  
  public LeafDir(String p, int n)
  {
    path = p;
    numFiles = n;
  }
  
  public String getPath()
  {
    return path;
  }
  
  public int getNumFiles()
  {
    return numFiles;
  }
  
  public String toString()
  {
    return getPath();
  }
}