package DSCoinPackage;

import java.util.LinkedList;
import java.util.Queue;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions ;
  public Queue<Transaction> q = new LinkedList<>();

  public void AddTransactions (Transaction transaction) {
        q.add(transaction);
        firstTransaction = q.peek();
        lastTransaction = transaction;
        numTransactions = q.size();
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
	
	 if(numTransactions == 0)throw new EmptyQueueException();
	 else {
		numTransactions--; 
		return q.remove(); 
	 }
  }

  public int size() {
    return numTransactions;
  }
}
