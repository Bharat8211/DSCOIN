package DSCoinPackage;

import HelperClasses.Pair;

public class Moderator
 { 
	public Members moderator ;
	
  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) throws EmptyQueueException {
	  
	  moderator = new Members();
	  moderator.UID = "Moderator";
      
	  String coinId = "100000";  
	  int i = 0,n = 0;
	  
	  while(n < coinCount) {
		  Transaction newNode = new Transaction();
		  newNode.coinID = coinId;
		  newNode.Source = moderator;
		  newNode.Destination = DSObj.memberlist[i];
		  newNode.coinsrc_block = null;
		  
		  DSObj.latestCoinID = coinId;
		  DSObj.pendingTransactions.AddTransactions(newNode);
		  
		  coinId = Integer.toString(Integer.parseInt(coinId)+1);
		  i = (i+1)%DSObj.memberlist.length;
		  n++;
		  
	  }
	  
	  
	  n = coinCount/(DSObj.memberlist.length);
	  
	  for(i = 0;i<n;i++) {
		  int k = 0;
		  Transaction []arr = new Transaction[4];
		  
		  while(k < DSObj.memberlist.length) {
			  arr[k] = DSObj.pendingTransactions.RemoveTransaction();
			  k++;
		  }
		  
		  
		  TransactionBlock newBlock = new TransactionBlock(arr);
		
		  DSObj.bChain.InsertBlock_Honest(newBlock);
		   
		  
		  for(int j = 0;j<4;j++) {
			  String coin = newBlock.trarray[j].coinID;
			  newBlock.trarray[j].Destination.mycoins.add(new Pair<>(coin,newBlock));
		  }
	 } 
  }



public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) throws EmptyQueueException {
	  moderator = new Members();
	  moderator.UID = "Moderator";
    
	  String coinId = "100000";  
	  int i = 0,n = 0;
	  
	  while(n < coinCount) {
		  Transaction newNode = new Transaction();
		  newNode.coinID = coinId;
		  newNode.Source = moderator;
		  newNode.Destination = DSObj.memberlist[i];
		  newNode.coinsrc_block = null;
		  
		  DSObj.latestCoinID = coinId;
		  DSObj.pendingTransactions.AddTransactions(newNode);
		  
		  coinId = Integer.toString(Integer.parseInt(coinId)+1);
		  i = (i+1)%DSObj.memberlist.length;
		  n++;
		  
	  }
	  
	  
	  n = coinCount/(DSObj.memberlist.length);
	  
	  for(i = 0;i<n;i++) {
		  int k = 0;
		  Transaction []arr = new Transaction[4];
		  
		  while(k < DSObj.memberlist.length) {
			  arr[k] = DSObj.pendingTransactions.RemoveTransaction();
			  k++;
		  }
		  
		  
		  TransactionBlock newBlock = new TransactionBlock(arr);
		
		  DSObj.bChain.InsertBlock_Malicious(newBlock);
		   
		  
		  for(int j = 0;j<4;j++) {
			  String coin = newBlock.trarray[j].coinID;
			  newBlock.trarray[j].Destination.mycoins.add(new Pair<>(coin,newBlock));
		  }
	  } 
  }
}
