package HelperClasses;

import DSCoinPackage.Transaction;
import java.util.*;

public class MerkleTree {

  // Check the TreeNode.java file for more details
  public TreeNode rootnode;
  public int numdocs;

  void nodeinit(TreeNode node, TreeNode l, TreeNode r, TreeNode p, String val) {
    node.left = l;
    node.right = r;
    node.parent = p;
    node.val = val;
  }

  public String get_str(Transaction tr) {
    CRF obj = new CRF(64);
    String val = tr.coinID;
    if (tr.Source == null)
      val = val + "#" + "Genesis"; 
    else
      val = val + "#" + tr.Source.UID;

    val = val + "#" + tr.Destination.UID;

    if (tr.coinsrc_block == null)
      val = val + "#" + "Genesis";
    else
      val = val + "#" + tr.coinsrc_block.dgst;

    return obj.Fn(val);
  }

  public String Build(Transaction[] tr) {
    CRF obj = new CRF(64);
    int num_trans = tr.length;
    numdocs = num_trans;
    List<TreeNode> q = new ArrayList<TreeNode>();
    for (int i = 0; i < num_trans; i++) {
      TreeNode nd = new TreeNode();
      String val = get_str(tr[i]);
      nodeinit(nd, null, null, null, val);
      q.add(nd);
    }
    TreeNode l, r;
    while (q.size() > 1) {
      l = q.get(0);
      q.remove(0);
      r = q.get(0);
      q.remove(0);
      TreeNode nd = new TreeNode();
      String l_val = l.val;
      String r_val = r.val;
      String data = obj.Fn(l_val + "#" + r_val);
      nodeinit(nd, l, r, null, data);
      l.parent = nd;
      r.parent = nd;
      q.add(nd);
    }
    rootnode = q.get(0);

    return rootnode.val;
  }
  
  void search(TreeNode root, int start,int end , int index , ArrayList<Pair<String,String>> arr,int currlevel,int level) {
		
		 if(root == null) {
			 return;
		 }
		
		 int mid = start + (end-start)/2;
		 		 
		 if(index > mid) {
			 Pair<String,String> temp = new Pair <String,String> (root.left.val, root.right.val);
			 arr.add(temp);
			 if(currlevel+1 == level) {
				 return;
			 }
			 search(root.right,mid+1,end,index,arr,currlevel+1,level);
		 }
		 else {
			 Pair<String,String> temp = new Pair <String,String> (root.left.val, root.right.val);
			 arr.add(temp);
			 if(currlevel+1 == level) {
				 return;
			 }
			 search(root.left,start,mid,index,arr,currlevel+1,level);
		 }
		
  }
		
  public List<Pair<String,String>> getPath(int doc_idx){
		
		 ArrayList<Pair<String,String>> arr = new ArrayList<Pair<String,String>>(3);
		  
		 Pair<String,String> temp = new Pair <String,String> (rootnode.val, null);
		 
		 arr.add(temp);
		 
		 int level = (int)(Math.log(numdocs)/ Math.log(2)) +1;
		 int currlevel = 1;
		 
		 search(rootnode,1,numdocs,doc_idx,arr,currlevel,level);
		 
		 Collections.reverse(arr);
		 		
		 return arr;
 }
  
}
