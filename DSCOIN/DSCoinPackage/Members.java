package DSCoinPackage;

import java.util.*;

import HelperClasses.CRF;
import HelperClasses.Pair;

 public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public ArrayList<Transaction> in_process_trans; 

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
	  Pair<String, TransactionBlock> temp = mycoins.get(0);
	  mycoins.remove(0);
	  
	  Transaction newNode = new Transaction();
	  Members source = null,destination = null;
	  
	  for(int i =0;i < DSobj.memberlist.length;i++) {
		  if(DSobj.memberlist[i].UID == this.UID)source = DSobj.memberlist[i];
		  else if(DSobj.memberlist[i].UID == destUID)destination = DSobj.memberlist[i];
	  }
	  
	  newNode.coinID = temp.first;
	  newNode.Source = source;
	  newNode.Destination = destination;
	  newNode.coinsrc_block = temp.second;
	  
	  in_process_trans.add(newNode);
	  DSobj.pendingTransactions.AddTransactions(newNode);
  }
  
  public Transaction initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
	  Pair<String, TransactionBlock> temp = mycoins.get(0);
	  mycoins.remove(0);
	  
	  Transaction newNode = new Transaction();
	  Members source = null,destination = null;
	  
	  for(int i =0;i < DSobj.memberlist.length;i++) {
		  if(DSobj.memberlist[i].UID == this.UID)source = DSobj.memberlist[i];
		  else if(DSobj.memberlist[i].UID == destUID)destination = DSobj.memberlist[i];
	  }
	  
	  newNode.coinID = temp.first;
	  newNode.Source = source;
	  newNode.Destination = destination;
	  newNode.coinsrc_block = temp.second;
	  
	  in_process_trans.add(newNode);
	  DSobj.pendingTransactions.AddTransactions(newNode);
	  return newNode;
  }
  
  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction t, DSCoin_Honest DSObj) throws MissingTransactionException {
      
	  TransactionBlock temp = DSObj.bChain.lastBlock;
	  
	  List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
	  
	  int index = -1;
	  
	  while(temp != null){

		  String str;
		  if(temp.previous != null) {
			  str = temp.previous.dgst + "#" + temp.trsummary + "#" + temp.nonce;
		  }
		  else {
			  str = "DSCoin" + "#" + temp.trsummary + "#" + temp.nonce;
		  }
		  
		  list.add(new Pair<>(temp.dgst,str));
		  
		  for(int i = 0;i<temp.trarray.length;i++) {
	    	 if(t.coinID.equals(temp.trarray[i].coinID) && t.Source.UID.equals(temp.trarray[i].Source.UID) && t.Destination.UID.equals(temp.trarray[i].Destination.UID)) {
                 index = i;	    		
	    		 break;
	         }
	      }  
		  
		  if(index >= 0 )break;
		  
		  temp = temp.previous;
	  }
	  
	  
	  if(temp == null)throw new MissingTransactionException();  
	  
	  
	  if(temp.previous != null)list.add(new Pair<>(temp.previous.dgst,null));
	  else list.add(new Pair<>("DSCoin",null));
	  
	  Collections.reverse(list);
	  
	  List<Pair<String, String>> sibling_coupled_path = temp.Tree.getPath(index);
	  
	  t.Destination.mycoins.add(new Pair<>(t.coinID,temp));
	  
	  Collections.sort(t.Destination.mycoins, new Comparator<Pair<String,TransactionBlock>>() {
		@Override
		public int compare(Pair<String, TransactionBlock> o1,Pair<String, TransactionBlock> o2) {
            int a = Integer.parseInt(o1.first);
            int b = Integer.parseInt(o2.first);
			
            if(Integer.compare(a,b) < 0) {
            	return -1;
            }
            else if(Integer.compare(a,b) == 0)return 0;
            else return 1;
		}
      });
	 
	  for(int i = 0;i<t.Source.in_process_trans.size();i++) {
		  if(t.Source.in_process_trans.get(i) == t) {
			  t.Source.in_process_trans.remove(i);
			  break;
		  }
	  }
	  
	  return new Pair<>(sibling_coupled_path,list);
  }
  
  
  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction t, DSCoin_Malicious DSObj) throws MissingTransactionException {
      
	  TransactionBlock temp = DSObj.bChain.FindLongestValidChain();
	  	  
	  List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
	  
	  int index = -1;
	  
	  while(temp != null){

		  String str;
		  if(temp.previous != null) {
			  str = temp.previous.dgst + "#" + temp.trsummary + "#" + temp.nonce;
		  }
		  else {
			  str = "DSCoin" + "#" + temp.trsummary + "#" + temp.nonce;
		  }
		  
		  list.add(new Pair<>(temp.dgst,str));
		  
		  for(int i = 0;i<temp.trarray.length;i++) {
	    	 if(t.coinID.equals(temp.trarray[i].coinID) && t.Source.UID.equals(temp.trarray[i].Source.UID) && t.Destination.UID.equals(temp.trarray[i].Destination.UID)) {
                 index = i;	    		
	    		 break;
	         }
	      }  
		  
		  if(index >= 0 )break;
		  
		  temp = temp.previous;
	  }
	  
	  
	  if(temp == null) {
		  t.Source.mycoins.add(new Pair<>(t.coinID,t.coinsrc_block));
	 
		  Collections.sort(t.Source.mycoins, new Comparator<Pair<String,TransactionBlock>>() {
			@Override
			public int compare(Pair<String, TransactionBlock> o1,Pair<String, TransactionBlock> o2) {
		         int a = Integer.parseInt(o1.first);
		         int b = Integer.parseInt(o2.first);
					
		         if(Integer.compare(a,b) < 0) {
		          return -1;
		         }
		         else if(Integer.compare(a,b) == 0)return 0;
		         else return 1;
			  }
		  });
		  
		  for(int i = 0;i<t.Source.in_process_trans.size();i++) {
			  if(t.Source.in_process_trans.get(i) == t) {
				  t.Source.in_process_trans.remove(i);
				  break;
			  }
		  }
		  
		  throw new MissingTransactionException();
	  }  
	  
	  
	  if(temp.previous != null)list.add(new Pair<>(temp.previous.dgst,null));
	  else list.add(new Pair<>("DSCoin",null));
	  
	  Collections.reverse(list);
	  
	  List<Pair<String, String>> sibling_coupled_path = temp.Tree.getPath(index);
	  
	  t.Destination.mycoins.add(new Pair<>(t.coinID,temp));
	  
	  Collections.sort(t.Destination.mycoins, new Comparator<Pair<String,TransactionBlock>>() {
		@Override
		public int compare(Pair<String, TransactionBlock> o1,Pair<String, TransactionBlock> o2) {
            int a = Integer.parseInt(o1.first);
            int b = Integer.parseInt(o2.first);
			
            if(Integer.compare(a,b) < 0) {
            	return -1;
            }
            else if(Integer.compare(a,b) == 0)return 0;
            else return 1;
		}
      });
	 
	  for(int i = 0;i<t.Source.in_process_trans.size();i++) {
		  if(t.Source.in_process_trans.get(i) == t) {
			  t.Source.in_process_trans.remove(i);
			  break;
		  }
	  }
	  
	  return new Pair<>(sibling_coupled_path,list);
  }
  
  
  public boolean validateTransactionBySeller(Transaction t, DSCoin_Malicious DSObj,Pair<List<Pair<String, String>>, List<Pair<String, String>>>proof) {
	  
	  List<Pair<String, String>> l1 = proof.first;
	  List<Pair<String, String>> l2 = proof.second;
	  
	  TransactionBlock temp = DSObj.bChain.FindLongestValidChain();
	  
      List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
	  
	  int index = -1;
	  
	  while(temp != null){

		  String str;
		  if(temp.previous != null) {
			  str = temp.previous.dgst + "#" + temp.trsummary + "#" + temp.nonce;
		  }
		  else {
			  str = "DSCoin" + "#" + temp.trsummary + "#" + temp.nonce;
		  }
		  
		  list.add(new Pair<>(temp.dgst,str));
		  
		  for(int i = 0;i<temp.trarray.length;i++) {
	    	 if(t.coinID.equals(temp.trarray[i].coinID) && t.Source.UID.equals(temp.trarray[i].Source.UID) && t.Destination.UID.equals(temp.trarray[i].Destination.UID)) {
                 index = i;	    		
	    		 break;
	         }
	      }  
		  
		  if(index >= 0 )break;
		  
		  temp = temp.previous;
	  }
	  
	  if(temp == null)return false;
	  
	  
	  if(temp.previous != null)list.add(new Pair<>(temp.previous.dgst,null));
	  else list.add(new Pair<>("DSCoin",null));
	  
	  Collections.reverse(list);
	  
	  List<Pair<String, String>> sibling_coupled_path = temp.Tree.getPath(index);
	  
	  boolean flag = false;
	  
	  for(int i = 0;i<this.mycoins.size();i++) {
		  Pair<String,TransactionBlock> curr = new Pair<>(t.coinID,temp);
		  if(this.mycoins.get(i).first.equals(t.coinID) && this.mycoins.get(i).second.nonce.equals(temp.nonce)) {
			  flag = true;
			  break;
		  }
	  }
	  
	  
	  if(flag == false)return false;
	  
	  if(l1.size() != sibling_coupled_path.size())return false;
	  if(l2.size() != list.size())return false;
	  flag = true;
        
	  for(int i = 0;i<list.size();i++) {
      	if(!list.get(i).first.equals(l2.get(i).first)){
      		flag = false;
      		break;
      	}
      	if(i != 0 && !list.get(i).second.equals(l2.get(i).second)){
      		flag = false;
      		break;
      	}
      }
       
	  if(flag == false)return false;
	  
	  for(int i = 0;i<sibling_coupled_path.size();i++) {
	     if(!sibling_coupled_path.get(i).first.equals(l1.get(i).first)){
	        flag = false;
	        break;
	     }
	     if(i != sibling_coupled_path.size()-1 && !sibling_coupled_path.get(i).second.equals(l1.get(i).second)){
	      	flag = false;
	      	break;
	     }
	  }
	  
	  
      if(flag == false)return false;
	  return true;
  }
  

  public void MineCoin(DSCoin_Honest DSObj) throws EmptyQueueException {
       int n = 0;
       
       HashMap<String,Integer> map = new HashMap<String,Integer>();
       map.clear();  
     
       Transaction []arr = new Transaction[4];
       
       while(n < (DSObj.bChain.tr_count - 1)) {
    	   Transaction curr;
    	   try {
			  curr = DSObj.pendingTransactions.RemoveTransaction();
		   } 
    	   catch (EmptyQueueException e) {
			  throw new EmptyQueueException();
		   } 
    	   
    	  if(map.containsKey(curr.coinID))continue;
    	  
    	  if(DSObj.bChain.lastBlock.checkTransaction(curr,DSObj.bChain.lastBlock) == false)continue;
    	  
    	  arr[n] = curr;
    	  map.put(curr.coinID, 1);
    	  n++;
       }
       
       Transaction reward = new Transaction();
       
       String coin = Integer.toString(Integer.parseInt(DSObj.latestCoinID) + 1);
       DSObj.latestCoinID = coin;
        
       reward.coinID = coin;
       reward.Source = null;
       reward.Destination = this;
       reward.coinsrc_block = null;
       
       arr[n] = reward;

       TransactionBlock newBlock = new TransactionBlock(arr);
       DSObj.bChain.InsertBlock_Honest(newBlock);
       
       this.mycoins.add(new Pair<>(coin,newBlock));
       
  }
  
  
  public void MineCoin(DSCoin_Malicious DSObj,boolean isHonest) throws EmptyQueueException {
	  int n = 0;
	  
	  TransactionBlock lastBlock = DSObj.bChain.FindLongestValidChain();
      
      HashMap<String,Integer> map = new HashMap<String,Integer>();
      map.clear();  
    
      Transaction []arr = new Transaction[4];
      
      while(n < (DSObj.bChain.tr_count - 1)) {
	   	   Transaction curr;
	   	   try {
			 curr = DSObj.pendingTransactions.RemoveTransaction();
		   } 
	   	   catch (EmptyQueueException e) {
			 throw new EmptyQueueException();
		   } 
	   	   
	   	  if(map.containsKey(curr.coinID))continue;
	   	  
	   	  
	   	  if(isHonest == true && lastBlock.checkTransaction(curr,lastBlock) == false)continue; 
	   
	   	  arr[n] = curr;
	   	  map.put(curr.coinID, 1);
	   	  n++;
      }
      
      Transaction reward = new Transaction();
      
      String coin = Integer.toString(Integer.parseInt(DSObj.latestCoinID) + 1);
      DSObj.latestCoinID = coin;
       
      reward.coinID = coin;
      reward.Source = null;
      reward.Destination = this;
      reward.coinsrc_block = null;
      
      arr[n] = reward;

      TransactionBlock newBlock = new TransactionBlock(arr);
      DSObj.bChain.InsertBlock_Malicious(newBlock);
      
      this.mycoins.add(new Pair<>(coin,newBlock));
  } 
}
