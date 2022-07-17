package DSCoinPackage;

import java.math.BigInteger;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count = 4;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
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
       lastBlock = newBlock;
  }
}
