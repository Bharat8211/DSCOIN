package DSCoinPackage;

import java.util.ArrayList;
import java.util.List;

import HelperClasses.Pair;

public class DriverCodeMalicious {

	public static void main(String[] args) {
		
        DSCoin_Malicious DSObj = new  DSCoin_Malicious();
        DSObj.pendingTransactions = new TransactionQueue();
        DSObj.bChain = new BlockChain_Malicious();
        DSObj.bChain.lastBlocksList = new ArrayList<TransactionBlock>();
        boolean correct = true;
        DSObj.bChain.tr_count = 4;
        Members m1 = new Members();
        Members m2 = new Members();
        Members m3 = new Members();
        Members m4 = new Members();
        m1.UID = "101";
        m2.UID = "102";
        m3.UID = "103";
        m4.UID = "104";
        m1.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
        m2.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
        m3.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
        m4.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
        m1.in_process_trans = new ArrayList<>();
        m2.in_process_trans = new ArrayList<>();
        m3.in_process_trans = new ArrayList<>();
        m4.in_process_trans = new ArrayList<>();
        DSObj.memberlist = new Members[4];
        DSObj.memberlist[0] = m1;
        DSObj.memberlist[1] = m2;
        DSObj.memberlist[2] = m3;
        DSObj.memberlist[3] = m4;
        
        

        Moderator mod = new Moderator();
        try {
            mod.initializeDSCoin(DSObj, 8);
           
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        
        Transaction t1 = m1.initiateCoinsend("102", DSObj);
        Transaction t2 = m3.initiateCoinsend("102", DSObj);
        Transaction t3 = m3.initiateCoinsend("102", DSObj);
                
        try {
           m2.MineCoin(DSObj,true);      
           Pair<List<Pair<String, String>>,List<Pair<String, String>>> temp = m1.finalizeCoinsend(t1,DSObj);
           correct &= m2.validateTransactionBySeller(t1, DSObj, temp);
           temp = m3.finalizeCoinsend(t2,DSObj);
           correct &= m2.validateTransactionBySeller(t2, DSObj, temp);
           temp = m3.finalizeCoinsend(t3,DSObj);
           correct &= m2.validateTransactionBySeller(t3, DSObj, temp);
        } 
        catch (Exception e) {
           System.out.println(e);	
        }
        
   
        Transaction fake1 = new Transaction();
        fake1.Source = m1;
        fake1.Destination = m4;
        fake1.coinID = "100003";
        fake1.coinsrc_block = DSObj.bChain.lastBlocksList.get(0);
        DSObj.pendingTransactions.AddTransactions(fake1);        

        Transaction t4 = m2.initiateCoinsend("101", DSObj);
        Transaction t5 = m2.initiateCoinsend("101", DSObj);
        
        try {
            m3.MineCoin(DSObj,false);
            Pair<List<Pair<String, String>>,List<Pair<String, String>>> temp = m1.finalizeCoinsend(t4,DSObj);
            temp = m3.finalizeCoinsend(t5,DSObj); 
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        Transaction fake2 = new Transaction();
        fake2.Source = m2;
        fake2.Destination = m4;
        fake2.coinID = "100020";
        fake2.coinsrc_block = DSObj.bChain.lastBlocksList.get(0);
        DSObj.pendingTransactions.AddTransactions(fake2);
        
        Transaction fake3 = new Transaction();
        fake3.Source = m3;
        fake3.Destination = m1;
        fake3.coinID = "100021";
        fake3.coinsrc_block = DSObj.bChain.lastBlocksList.get(0);
        DSObj.pendingTransactions.AddTransactions(fake3);
        
        Transaction fake4 = new Transaction();
        fake4.Source = m4;
        fake4.Destination = m2;
        fake4.coinID = "100013";
        fake4.coinsrc_block = DSObj.bChain.lastBlocksList.get(0);
        DSObj.pendingTransactions.AddTransactions(fake4);
        
        try {
            m3.MineCoin(DSObj,false);
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        Transaction t6 = m4.initiateCoinsend("101", DSObj);
        Transaction t7 = m2.initiateCoinsend("103", DSObj);
        Transaction t8 = m1.initiateCoinsend("104", DSObj);
        
        try {
            m2.MineCoin(DSObj,true);
            Pair<List<Pair<String, String>>,List<Pair<String, String>>> temp = m1.finalizeCoinsend(t6,DSObj);
            correct &= m1.validateTransactionBySeller(t6, DSObj, temp);
            temp = m3.finalizeCoinsend(t7,DSObj);
            correct &= m3.validateTransactionBySeller(t7, DSObj, temp);
            temp = m3.finalizeCoinsend(t8,DSObj);
            correct &= m4.validateTransactionBySeller(t8, DSObj, temp);
         } 
        catch (Exception e) {
            System.out.println(e);	
        }
            
        
        Transaction fake5 = new Transaction();
        fake5.Source = m1;
        fake5.Destination = m3;
        fake5.coinID = "100017";
        fake5.coinsrc_block = DSObj.bChain.lastBlocksList.get(0);
        DSObj.pendingTransactions.AddTransactions(fake5);
        
        Transaction fake6 = new Transaction();
        fake6.Source = m3;
        fake6.Destination = m4;
        fake6.coinID = "100022";
        fake6.coinsrc_block = DSObj.bChain.lastBlocksList.get(0);
        DSObj.pendingTransactions.AddTransactions(fake6);
        
        Transaction fake7 = new Transaction();
        fake7.Source = m4;
        fake7.Destination = m2;
        fake7.coinID = "100027";
        fake7.coinsrc_block = DSObj.bChain.lastBlocksList.get(0);
        DSObj.pendingTransactions.AddTransactions(fake7);
        
        
        try {
            m3.MineCoin(DSObj,false);
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        Transaction t9 = m4.initiateCoinsend("103", DSObj);
        Transaction t10 = m2.initiateCoinsend("101", DSObj);
        Transaction t11 = m2.initiateCoinsend("104", DSObj);
        
        try {
            m2.MineCoin(DSObj,true);
            Pair<List<Pair<String, String>>,List<Pair<String, String>>> temp = m1.finalizeCoinsend(t9,DSObj);    
            correct &= m3.validateTransactionBySeller(t9, DSObj, temp);            
            temp = m3.finalizeCoinsend(t10,DSObj);
            correct &= m1.validateTransactionBySeller(t10, DSObj, temp);
            temp = m3.finalizeCoinsend(t11,DSObj);
            correct &= m4.validateTransactionBySeller(t11, DSObj, temp);
            
             
            if(correct == true) {
            	System.out.println("All the test Cases Passed");
            }
            else {
            	System.out.println("Some of the test Cases Failed!");
            }
        } 
        catch (Exception e) {
            System.out.println(e);	
        }
        
	}

}
