public class MyLinkedList
{
  class Node // Wrapper class for the data
  {
    // Properties of a single node
    int data;  // actual information being stored
    Node next; // pointer to the next node in the list
    
    // constructor
    public Node( int dataToStore, Node nextNode )
    {
      data = dataToStore;
      next = nextNode;
    }
  }
  
  // Properties of a linked list
  Node head;
  Node tail;
  int length;

  /* NOTE: tail is not currently used. Your goal is to modify
     this class to properly maintain and utilize the tail
     reference, resulting in better efficiency for certain
     use cases. */
  
  // Initialize an empty list
  public MyLinkedList()
  {
    head = null; // empty list, nowhere for head to point
    length = 0;
  }
  
  public void addToHead(int value)
  {
    head = new Node(value, head);
    length++;
  }
  
  public int getHead()
  {
    return head.data;
  }
  
  // Move the head reference to the next node.
  // This means we have no references to the old head, so it
  // will be deleted by garbage collection
  public void removeHead()
  {
    head = head.next;
    length--;
  }
  
  public int size()
  {
    return length;
  }

  public void addToTail(int value)
  {
    // Step 0: Special case if list is empty
    if(head == null)
    {
      addToHead(value);
      return; // end method early so that we don't add twice
    }
  
    // Step 1: Locate the tail
    Node current = head;
    while(current.next != null)
    {
      current = current.next;
    }
    
    // Step 2: Add the new node after the tail
    current.next = new Node(value, null);
    
    // Step 3: Update the length
    length++;
  }
  
  public int getTail()
  {
    // Step 0: Deal with empty list
    // We don't have to do anything, an error will be thrown
    
    // Step 1: Locate the tail
    Node current = head;
    while(current.next != null)
    {
      current = current.next;
    }
    
    // Step 2: Return the data
    return current.data;
  }
  
  public void removeTail()
  {
    // Step 0: Deal with special case where list has one element
    if(head.next == null)
    {
      removeHead();
      return;
    }
  
    // Step 1: Locate the node just BEFORE the tail
    Node current = head;
    while(current.next.next != null)
    {
      current = current.next;
    }
    
    // Step 2: Remove the tail
    current.next = null;
    
    // Step 3: Update length
    length--;
  }
}