import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
public class Survey
{
  public static void main(String[] args)
  {
    Scanner in = new Scanner(System.in);
    
    System.out.println("Hello there!");
    System.out.println();
    System.out.println("Please enter your name.");
    String name = in.nextLine();
    
    System.out.println("Hello " + name + ". Welcome to the Final Project Retrospective!");
    System.out.println();
    System.out.println("What will follow is a series of questions.");
    System.out.println("Please answer these questions briefly and honestly.");
    System.out.println("Only one line of text will be accepted for each question except where specified, so make it count!");
    System.out.println();
    
    try
    {
      PrintWriter out = new PrintWriter(new File(name + " Final Project Retrospective.txt"));
      
      if(!askAndWriteYNQuestion(in, out, "Did you accomplish everything you wanted to on the project?"))
      {
        askAndWriteQuestion(in, out, "Give a rough estimate (as a percentage) of how much you completed compared to your original vision.");
        if(askAndWriteYNQuestion(in, out, "Were your goals harder than anticipated?"))
        {
          askAndWriteQuestion(in, out, "Explain.");
        }
        if(!askAndWriteYNQuestion(in, out, "Did you make good use of the time you had available?"))
        {
          askAndWriteQuestion(in, out, "Explain.");
        }
        if(askAndWriteYNQuestion(in, out, "Did you run into major conceptual misunderstandings?"))
        {
          askAndWriteQuestion(in, out, "Explain.");
        }
        if(askAndWriteYNQuestion(in, out, "Did we not go over something in class that was needed?"))
        {
          askAndWriteQuestion(in, out, "Explain.");
        }
      }
      
      if(askAndWriteYNQuestion(in, out, "Did you accomplish any stretch goals? (These are goals that you wanted to achieve, but knew right away that they would be difficult.)"))
      {
        askAndWriteQuestion(in, out, "Which ones?");
      }
      
      if(!askAndWriteYNQuestion(in, out, "Do you feel you put an appropriate amount of time and effort in?"))
      {
        askAndWriteQuestion(in, out, "What prevented you from putting in an appropriate amount of time and effort?");
      }
      if(askAndWriteYNQuestion(in, out, "Did you learn something new about Java programming in the course of completing the project?"))
      {
        askAndWriteQuestion(in, out, "What did you learn?");
      }
      
      if(askAndWriteYNQuestion(in, out, "Did you learn something new about computers or programming in general in the course of completing the project, other than what you answered in the previous two sections?"))
      {
        askAndWriteQuestion(in, out, "What did you learn?");
      }
      
      if(!askAndWriteYNQuestion(in, out, "Are you satisfied with the work you personally did on the project?"))
      {
        askAndWriteQuestion(in, out, "Explain.");
      }
      if(!askAndWriteYNQuestion(in, out, "Are you satisfied with the final result of the project?"))
      {
        askAndWriteQuestion(in, out, "Explain.");
      }
      askAndWriteQuestionLongAnswer(in, out, "If you had an extra week to work on the project, what would you work on?");
      askAndWriteQuestionLongAnswer(in, out, "If you were sent back in time and had to do the project all over again (including the proposal), what would you do differently?");
      
      if(askAndWriteYNQuestion(in, out, "Did you have teammates?"))
      {
        System.out.println("Now, you will evaluate your teammates.");
        evaluateTeammate(1, in, out);
        int i = 2;
        while(askAndWriteYNQuestion(in, out, "Did you have another teammate?"))
        {
          evaluateTeammate(i++, in, out);
        }
      }
      System.out.println("Evaluation of group members complete.");
      System.out.println();
      
      if(!askAndWriteYNQuestion(in, out, "Did David and Isabel help you enough with your project?"))
      {
        askAndWriteQuestion(in, out, "Explain.");
      }
      else if(askAndWriteYNQuestion(in, out, "Did David and Isabel help you too much with your project?"))
      {
        askAndWriteQuestion(in, out, "Explain.");
      }
      
      askAndWriteQuestion(in, out, "Did you enjoy the course?");
      
      askAndWriteQuestion(in, out, "Did you learn a lot in the course?");
      
      askAndWriteQuestionLongAnswer(in, out, "Do you feel that you were well served by your instructor (David)? Provide any positive or negative feedback you have for him in this space.");
      
      askAndWriteQuestionLongAnswer(in, out, "Do you feel that you were well served by your TA (Isabel)? Provide any positive or negative feedback you have for her in this space.");
      
      askAndWriteQuestionLongAnswer(in, out, "Provide any suggestions you have for your instructional team that they can implement in future courses:");
      
      askAndWriteQuestionLongAnswer(in, out, "Anything else to say? Do so here:");
      out.close();
    }
    catch(IOException e)
    {
    }
    
    System.out.println("Thank you for taking Fundamentals of Computer Science! Your output file will have the name " + name + " Final Project Retrospective.txt");
  }
  
  public static String evaluateTeammate(int num, Scanner in, PrintWriter out)
  {
    String teammate = askAndWriteQuestion(in, out, "Enter name of teammate " + num + ":");
    if(askAndWriteYNQuestion(in, out, "Did " + teammate +" help you fix your code for the project?"))
    {
      askAndWriteQuestion(in, out, "Explain.");
    }
    if(askAndWriteYNQuestion(in, out, "During the project, did " + teammate + " help you gain understanding of course concepts you didn't understand before?"))
    {
      askAndWriteQuestion(in, out, "What concepts?");
    }
    if(!askAndWriteYNQuestion(in, out, "Did " + teammate + " contribute their fair share of the project?"))
    {
      askAndWriteQuestionLongAnswer(in, out, "Explain.");
    }
    askAndWriteQuestion(in,out,"If you had to give this group member a grade from 0-100 based on their total contributions to the project, what would you give them?");
    askAndWriteQuestionLongAnswer(in, out, "Write any other comments you have about " + teammate + ".");
    return teammate;
  }
  
  public static String askAndWriteQuestion(Scanner in, PrintWriter out, String question)
  {
    System.out.println(question);
    out.println(question + ":");
    String ret = in.nextLine();
    out.println(ret);
    out.println();
    return ret;
  }
  
  public static void askAndWriteQuestionLongAnswer(Scanner in, PrintWriter out, String question)
  {
    System.out.println(question);
    System.out.println("You may enter multiple lines for this question.");
    System.out.println("Type \"DONE.\" (no quotes) when you are done to move on to the next question.");
    out.println(question + ":");
    String ret = in.nextLine();
    
    while(!(ret.startsWith("DONE")))
    {
      out.println(ret);
      ret = in.nextLine();
    }
    out.println();
  }
  
  public static boolean askAndWriteYNQuestion(Scanner in, PrintWriter out, String question)
  {
    boolean resp = askYNQuestion(in, question);
    out.println(question + ":");
    out.println((resp ? "yes" : "no"));
    out.println();
    return resp;
  }
  
  public static boolean askYNQuestion(Scanner in, String question)
  {
    System.out.println("Answer Yes or No: " + question);
    String resp = in.nextLine().toLowerCase();
    
    return ((resp.startsWith("ye") && !resp.startsWith("yesterday")) || resp.equals("y") || resp.equals("y.") || resp.contains("affirmative") || resp.equals("aye"));
  }
}