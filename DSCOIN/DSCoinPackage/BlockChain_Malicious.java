package DSCoinPackage;

import java.math.BigInteger;
import java.util.ArrayList;

import HelperClasses.CRF;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public ArrayList<TransactionBlock> lastBlocksList;

  public  boolean checkTransactionBlock(TransactionBlock tB) {
    
	String str;  
	
	CRF obj = new CRF(64);
	  
	if(tB.previous == null) {
		str = obj.Fn(start_string + "#" + tB.trsummary + "#" + tB.nonce);
	}
	else {
		str = obj.Fn(tB.previous.dgst + "#" + tB.trsummary + "#" + tB.nonce);
	}
	
	String temp = tB.dgst.substring(0, 4); 
	
	if(!temp.equals("0000"))return false;
	if(!tB.dgst.equals(str))return false;
	
	str = tB.Tree.Build(tB.trarray);
    
	if(!tB.trsummary.equals(str))return false;
	
	for(int i = 0;i<4;i++) {
		if(tB.checkTransaction(tB.trarray[i], tB.previous) == false)return false;
		
	}

    return true;
  }

  public TransactionBlock FindLongestValidChain () {
	
	int maxLen = 0;
	TransactionBlock lastBlock = null;
	  
	for(int i = 0;i<lastBlocksList.size();i++) {
		TransactionBlock curr = lastBlocksList.get(i);
		TransactionBlock temp = curr;
		int len = 0;
		
		while(curr != null) {
			if(checkTransactionBlock(curr))len++;	
			else {
			   len = 0;
			   temp = curr.previous;
			}
			curr = curr.previous;
		}
		
		if(len > maxLen) {
			maxLen = len;
			lastBlock = temp;
		}
	}
	

	
    return lastBlock;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
      
	   TransactionBlock lastBlock = FindLongestValidChain();
	   
	   String str = "1000000001";
       String temp;
       while(true) {
    	   CRF obj = new CRF(64);
    	   if(lastBlock == null) {
    		   temp = obj.Fn(start_string+ "#" + newBlock.trsummary + "#" + str);  
    	   }
    	   else {
    		   temp = obj.Fn(lastBlock.dgst+ "#" + newBlock.trsummary + "#" + str);
    	   }
    	   
    	   String s = temp.substring(0,4);
    	   
    	  
    	   if(s.equals("0000"))break;
    	   
    	   BigInteger b1 = new BigInteger(str);
    	   b1 = b1.add(new BigInteger("1"));
           str = b1.toString();    
       }
       
       
       newBlock.nonce = str;
       newBlock.dgst = temp;
       newBlock.previous = lastBlock;
            
       int flag = 0;
       
       for(int i = 0;i<lastBlocksList.size();i++) {
    	   if(lastBlocksList.get(i) == lastBlock) {
    		   flag = 1;
    		   lastBlocksList.set(i, newBlock);
    		   break;
    	   }
       }
       
       if(flag == 0) {
    	   lastBlocksList.add(newBlock);
       }
  }
}
