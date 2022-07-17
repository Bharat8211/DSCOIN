package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
     trarray  = t;
     Tree = new MerkleTree();
     trsummary = Tree.Build(t);
     dgst = null;
     previous = null;
     nonce = null;
  }
  
  public boolean checkTransaction(Transaction t,TransactionBlock lastBlock) {
	  
	    TransactionBlock temp = lastBlock;
	    
	    if(t.coinsrc_block == null)return true;
	    
	    while(temp != null && temp != t.coinsrc_block) {
	    	 for(int i = 0;i<temp.trarray.length;i++) {
	    		 if(t.coinID == temp.trarray[i].coinID) {
	    			 return false;
	    		 }
	    	 }
	    
	    	 temp = temp.previous;
	    }
		
	    if(temp == null)return false;
	    
	    boolean flag = false;
	    
	    for(int i = 0;i<temp.trarray.length;i++) {
 		   if(t.coinID == temp.trarray[i].coinID && t.Source == temp.trarray[i].Destination) {
 			   flag = true;
 		   }
 	    }

	    return flag;
  }  
}
