public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;
        
        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;          
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        //checks for a node with one value and two children nodes
        public boolean isTwoNode() {
            if(this.value1!=0 && this.value2==0 && this.value3==0) 

            {   return true;}
            return false;}
        
        //checks for a node with two value and three children nodes
        public boolean isThreeNode() {
            if(this.value1!=0 && this.value2!=0 && this.value3==0){
                return true;
            }
            return false;
        }

        //checks for a node with three value and four children nodes
        public boolean isFourNode() {
            if(this.value1!=0 && this.value2!=0 && this.value3!=0){
                return true;
            }
            return false;
        }

        //checks if current node is root
        public boolean isRoot() {
            if(root == this) {
                return true;
            }
            return false;
        }

        //creating a two node
        public TwoFourTreeItem(int value1) {
            this.value1 = value1;
        }

         //creating a three node
        public TwoFourTreeItem(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
            this.values = 2;
        }

        //creating a four node
        public TwoFourTreeItem(int value1, int value2, int value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;

            this.values = 3;
        }

        private void printIndents(int indent) {
            for(int i = 0; i < indent; i++) System.out.printf("  ");
        }

        //printing
        public void printInOrder(int indent) {
            if(!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if(isThreeNode()) {
                if(!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if(isFourNode()) {
                if(!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if(!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if(!isLeaf) rightChild.printInOrder(indent + 1);
        }
    }

    TwoFourTreeItem root = null;

    //adding values to 2-4 tree
    public boolean addValue(int value) {
       
        //if value is alredy in tree return
        if(hasValue(value)){
            return true;
        }
        else{
            TwoFourTreeItem adder = root;

            //if node is null create a root
            if(adder == null) {
           
                adder = new TwoFourTreeItem(value);
                root = adder;
                adder.parent = adder;
                return false;
            }
          
            //traverse until the correct leaf is found
            while(!(adder.isLeaf)){

                //if node is a four node split and traverse
                if(adder.isFourNode())
                {
                    adder = preemptiveSplit(adder);
                }
                adder = nextChild(adder, value);
            }

            //double cheack to make sure all four nodes are split
            if(adder.isFourNode()){
                adder = preemptiveSplit(adder);
                adder = nextChild(adder, value);
            }

           //depending on type of node and value comparison, add new value
            if(adder.isTwoNode()){
                if(value < adder.value1){
                    reset(adder, value, adder.value1, adder.value2);
                }
                else{
                    reset(adder, adder.value1, value, adder.value2);}
                }
            else if(adder.isThreeNode()){
                if(value < adder.value1){
                    reset(adder, value, adder.value1, adder.value2);
    
                }
                else if(isBetween(value, adder.value1, adder.value2)){
                    reset(adder, adder.value1, value, adder.value2);
                }
                else{
                    reset(adder, adder.value1, adder.value2, value);
                } 
        
            }

            //increase values counter in node
            adder.values++;
        }       

        //end adding process
            return false;
    }

    public boolean hasValue(int value) {

        //set refernce to root
        TwoFourTreeItem trav = root;

        //traverse thru tree
        while(trav!= null){
            //if value is in current node it has been found
            if(inNode(trav,value)){
                return true;
            }
            else{
                trav = nextChild(trav, value);
            }
        }

        //if null is reached value not found
        return false;
    }

    //deletion process
    public boolean deleteValue(int value) {
        
        // if tree has value continue process
        if(hasValue(value)){

            TwoFourTreeItem dele = root;

            //if root is null return and end process
            if(dele.isRoot() && dele.isLeaf && dele.isTwoNode()){
                dele = null;
                return false;}
         
            // if root is a two node and both children are two nodes fuse
            if(dele.isTwoNode() && dele.leftChild.isTwoNode()&&dele.rightChild.isTwoNode()){
                dele = rootFuse(dele, dele.leftChild, dele.rightChild);}

            //traverse
             while(potentialNode(dele, value)){
               
                //is node is two node and not the root
                if(dele.isTwoNode() && !(dele.isRoot()))
                {
                    //find left sibling
                        TwoFourTreeItem L = leftSiblingNode(dele, dele.parent);
                        //find right sibling
                        TwoFourTreeItem R = rightSiblingNode(dele, dele.parent);
                        
                        
                        //if right is 3-4 rotate
                        if(R != null && ( R.isThreeNode() || R.isFourNode())){
                                
                            dele = rightRotation(dele, R, dele.parent);
                        }
                        else if(L != null && (L.isThreeNode() || L.isFourNode())){
                            
                            // else if left is 3-4 rotate
                            dele = leftRotation(dele, L, dele.parent);}
                        else{
                            //if both are two nodes fuse with one 
                             if(L!=null){
                                dele = leftSiblingMerge(dele, L, dele.parent);}
                            else if(R != null){
                                dele= rightSiblingMerge(dele, R, dele.parent);}
                                 
                            }
                }

                //if node is not leaf but value is inside node
                else if(!(dele.isLeaf) && inNode(dele, value))
                {
                    //get closest left child to the value
                    TwoFourTreeItem leftCh = getleftChild(dele, value);
                     //get closest right child to the value
                    TwoFourTreeItem rightCh = getRightChild(dele, value);
                     
                    //successor and presuccesor node creation
                    TwoFourTreeItem s= null;
                    TwoFourTreeItem pre = null;
                    
                    //assign predecessor node and succesor node depending on node and value location
                    if(dele.isTwoNode())
                    {
                        s = successorNode(dele.rightChild);
                        pre = presuccessorNode(dele.leftChild);
                    }
                    else if(dele.isThreeNode()){
                        if(value == dele.value1){
                                pre = presuccessorNode(dele.leftChild);
                                s = successorNode(dele.centerChild);
                        }
                        else{
                             pre = presuccessorNode(dele.centerChild);
                            s = successorNode(dele.rightChild);
                            }
                    }
                    else if(dele.isFourNode())
                    {
                        if(value == dele.value1){
                            pre = presuccessorNode(dele.leftChild);
                            s = successorNode(dele.centerLeftChild);
                        }
                        else if(value == dele.value2){
                            pre = presuccessorNode(dele.centerLeftChild);
                            s = successorNode(dele.centerRightChild);
                        }
                        else{
                            pre = presuccessorNode(dele.centerRightChild);
                            s = successorNode(dele.rightChild);
                        }
                    }	
	
                        // if left child is 3-4
                    if(leftCh.isThreeNode() || leftCh.isFourNode()){
                        //presuccessor process
                        int pVal = getPredecessorVal(pre);
                        
                        //swap depending on location of both values
                         //if predecessor node is a 2 node continue to the left to delete
                          //if not 2 two node just swap, delete and return
                        if(value == dele.value1){
                           
                            if(pre.isTwoNode())
                            {
                                reset(pre, dele.value1, pre.value2,pre.value3);
                                reset(dele, pVal, dele.value2, dele.value3);
                                dele = leftCh;
                                
                            }
                           
                            else if(pre.isThreeNode()){
                                reset(pre, pre.value1, 0,0);
                                reset(dele, pVal, dele.value2, dele.value3);
                                return false;
                            }
                            else if(pre.isFourNode()){
                                reset(pre, pre.value1, pre.value2, 0);
                                reset(dele, pVal, dele.value2, dele.value3);
                                return false;
                            }
                        }
                        else if(value == dele.value2){
                            if(pre.isTwoNode())
                            {
                                reset(pre, dele.value2, pre.value2,pre.value3);
                                reset(dele, dele.value1, pVal, dele.value3);
                                dele = leftCh;
                            }
                            else if(pre.isThreeNode()){
                                reset(pre, pre.value1, 0,0);
                                reset(dele, dele.value1, pVal, dele.value3);
                                    
                                return false;
                            }
                            else if(pre.isFourNode()){
                                reset(pre, pre.value1, pre.value2, 0);
                                reset(dele, dele.value1, pVal, dele.value3);     
                                return false;
                            }
                        }
                        else if(value == dele.value3){
                            if(pre.isTwoNode())
                            {
                                reset(pre, dele.value3, pre.value2,pre.value3);
                                reset(dele, dele.value1, dele.value2, pVal);

                                dele = leftCh;
                            }
                            else if(pre.isThreeNode()){
                                reset(pre, pre.value1, 0,0);
                                reset(dele, dele.value1, dele.value2, pVal);
                            
                                    return false;
                             }
                            else if(pre.isFourNode()){
                                    reset(pre, pre.value1, pre.value2, 0);
                                    reset(dele, dele.value1, dele.value2, pVal);
                                    return false;
                            }
                        }
                         
                    }		
	                    //get right child according to where the val is at
                        else if(rightCh.isThreeNode() || rightCh.isFourNode()){
                       //swap depending on location of both values
                         //if successor node is a 2 node continue to the left to delete
                          //if not 2 two node just swap, delete and return
                        
                            int sVal = s.value1;
                            
                            if(value == dele.value1){
                                
                                reset(s, dele.value1, s.value2, s.value3);
                                reset(dele, sVal, dele.value2, dele.value3);
                            }
                            else if(value == dele.value2){
                                
                                reset(s, dele.value2, s.value2, s.value3);
                                reset(dele, dele.value1, sVal,dele.value3);
                            }
                            else{
                                reset(s, dele.value3, s.value2, s.value3);
                                reset(dele, dele.value1, dele.value2,sVal);
                                
                            }
                            
                                if(s.isTwoNode()){
                                    dele = rightCh;
                                }
                                else{
                                    //successor wil always be the first value of sucessor node so just adjust
                                    reset(s, s.value2, s.value3, 0);
                                    return false;
                                }
                            
                          
                        }
                        else{ 
                        //if both two node
                        // merge with children

                        dele = subTreeMerge(dele,leftCh, rightCh, value);
                        //revist cases and traverse if need to
                        }
                }

                else if(inNode(dele, value) && dele.isLeaf){
                    //find left sibling
                    if(dele.isTwoNode())
                    {
                        
                        TwoFourTreeItem L = leftSiblingNode(dele, dele.parent);
                        //find right sibling
                        TwoFourTreeItem R = rightSiblingNode(dele, dele.parent);
                        
                        //if right is not null
                       
                                //if right is 3-4 rotate
                                if(R!=null && R.isThreeNode() || R.isFourNode()){
                                
                                    dele = rightRotation(dele, R, dele.parent);
                                }
                                //if right null or two node rotate left
                                else if(L!= null && L.isThreeNode() || L.isFourNode()){
                                
                                    dele = leftRotation(dele, L, dele.parent);
                                }
                                else{
                                    //if both are two node merge with left child
                                        dele = leftSiblingMerge(dele, L, dele.parent);
                                    }
                               
                               
                                }  
                                
                                //depending on location of value delete and readjust node values
                                if(value == dele.value1){
                                reset(dele, dele.value2, dele.value3,0);
                                }
                                else if(value == dele.value2){
                                reset(dele, dele.value1,dele.value3,0);
                                }
                                else{
                                    reset(dele, dele.value1, dele.value2, 0);
                                }

                                //reduce number of values in node
                                dele.values--;
                                return true;
                
                    }
                    
            // continue searching for node until found in a leaf node
            dele = nextChild(dele, value);
           }
        }   
   return false;
}
    

    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }

    public TwoFourTree() {

    }

    public boolean inNode(TwoFourTreeItem n,  int v)
    {
        //if either node values are the value that is being searched return true
        if(v == n.value1 || v == n.value2 || v == n.value3){
            return true;
        }

        return false;
   
    }
    public boolean isBetween(int value, int value1, int value2) 
    // value is inside value1 and value2 return true
    { return (value > value1 && value < value2) ? true:false;}

    private TwoFourTreeItem nextChild(TwoFourTreeItem node, int v){
        //if value is in node return node 
        if(inNode(node, v))
        {return node;}

        //return child depending on the current node type
        //as well as if values is less than in between or greater than current nodes values
        // to ensure correct parent node ordering connect parent nodes as you traverse children
        if(node.isTwoNode())
        {
            if(v < node.value1) 
            {
                if(node.leftChild != null){
                    node.leftChild.parent = node;
                }
                return node.leftChild;}
            else
            {
                if(node.rightChild != null){
                    node.rightChild.parent = node;
                }
                return node.rightChild;}
        }
  
        else if(node.isThreeNode())
        {
            if(v < node.value1) 
            {
            if(node.leftChild != null){
                    node.leftChild.parent = node;
                }
           
                return node.leftChild;}
            else if(isBetween(v, node.value1, node.value2))
            {
            if(node.centerChild != null){
                    node.centerChild.parent = node;
                }
            
                return node.centerChild;}
            else
            {
            
             if(node.rightChild != null){
                    node.rightChild.parent = node;
                }
                return node.rightChild;}
        }
        else if(node.isFourNode()){
          
             if(v < node.value1) 
            {
        
            if(node.leftChild != null){
                    node.leftChild.parent = node;
                }
                return node.leftChild;}

            else if(isBetween(v, node.value1, node.value2))
            {
             if(node.centerLeftChild != null){
                    node.centerLeftChild.parent = node;
                }
                return node.centerLeftChild;}

            else if(isBetween(v, node.value2, node.value3))
            {
            if(node.centerRightChild != null){
                    node.centerRightChild.parent = node;
                }
                return node.centerRightChild;}
            else
            {
             if(node.rightChild != null){
                    node.rightChild.parent = node;
                }
                return node.rightChild;}
        }

        return null;
    }

    private void reset(TwoFourTreeItem node, int v1, int v2, int v3)
    {
        //assign node values according to values place in parameter
        node.value1 = v1;
        node.value2 = v2;
        node.value3 = v3;  
    }

    private TwoFourTreeItem preemptiveSplit (TwoFourTreeItem node){

        // if node is the root create two new nodes one for new parent and new sibling
        if(node.isRoot()){
            TwoFourTreeItem p = new TwoFourTreeItem(node.value2);
           
            TwoFourTreeItem r = new TwoFourTreeItem(node.value3);
    
            //connect appropriate nodes
            p.leftChild = node;
            p.rightChild = r;
            r.leftChild = node.centerRightChild;
            r.rightChild = node.rightChild;
            r.parent =p;

            //change values in current node and make it a child of new parent
            reset(node, node.value1, 0, 0);
            node.values= 1;
            node.rightChild =node.centerLeftChild;
            node.parent =p;
            root = p;
            p.parent = null;
            p.isLeaf = false;
            
            // node is not a leaf readjust parent node values in node children
            if(hasChildren(node) ){
                node.isLeaf =false;}
            if(hasChildren(r) ){
                r.isLeaf =false;}

           if(!(node.isLeaf)){
            r.leftChild.parent = r;
            r.rightChild.parent = r;
          
            }
           
            //reset current node back to two node
            returnToTwo(node);
            return p;
    }
// if node parent is two node make it a three node
    else if(node.parent.isTwoNode())
        {
            if(node == node.parent.leftChild){
                //make value2 in node the new value1 of node parent
            //readjust parent node values
               reset(node.parent, node.value2, node.parent.value1, node.parent.value3);

               //create new center node
                TwoFourTreeItem c = new TwoFourTreeItem(node.value3);
                
            //reset nodes values
            reset(node, node.value1, 0, 0);

            //connect new center node to node parent
                node.parent.centerChild = c;
                c.leftChild = node.centerRightChild;
                c.rightChild = node.rightChild;
                c.parent = node.parent;
                node.value2 = node.value3 = 0;
                node.rightChild =node.centerLeftChild;
                (node.parent).isLeaf = false;

                //if node is not leaf reconnect children of node and new center node
                if(hasChildren(node)){node.isLeaf =false;}
                if(hasChildren(c))
                {
                    c.isLeaf =false;
                    c.leftChild.parent = c;
                    c.rightChild.parent = c;
                }
                if(hasChildren(node.parent.rightChild)){(node.parent).rightChild.isLeaf =false;}

               // reset node to two node turn node parent to three node
                returnToTwo(node);
                returnToThree(node.parent);
                
            }
            else{
               
               
               reset(node.parent, node.parent.value1, node.value2, node.parent.value3);
              node.parent.isLeaf = false;
                TwoFourTreeItem c = new TwoFourTreeItem(node.value1);
                
                
                 reset(node, node.value3, 0, 0);

                node.parent.centerChild = c;
                c.leftChild = node.leftChild;
                c.rightChild = node.centerLeftChild;
                c.parent = node.parent;
                
                node.leftChild = node.centerRightChild;

                if(hasChildren(node) ){node.isLeaf =false;}
                if(hasChildren(c) ){
                    c.leftChild.parent = c;
                    c.rightChild.parent = c;
                    c.isLeaf =false;}
                if(hasChildren(node.parent.leftChild)){node.parent.leftChild.isLeaf =false;}

                returnToTwo(node);
                returnToThree(node.parent);
                
            } 
        
        }

        else if(node.parent.isThreeNode()){
            if(node == node.parent.leftChild){
                node.parent.centerRightChild = node.parent.centerChild;
                
                TwoFourTreeItem cl = new TwoFourTreeItem(node.value3);

                node.parent.value3 = node.parent.value2;
                node.parent.value2 = node.parent.value1;
                node.parent.value1 =node.value2;
                
                node.value2 = node.value3 =0;
                node.parent.centerLeftChild =cl;

                cl.leftChild= node.centerRightChild;
                cl.rightChild = node.rightChild;
                cl.parent = node.parent;

                node.rightChild= node.centerLeftChild;

                if(hasChildren(node)){node.isLeaf =false;}
                if(hasChildren(cl)){
                    cl.isLeaf =false;
                    cl.leftChild.parent = cl;
                    cl.rightChild.parent = cl;
                }
                
                returnToTwo(node);
                makeFour(node.parent);

                 node.parent.isLeaf = false;
            }
            else if(node == node.parent.rightChild)
            {
                node.parent.centerLeftChild = node.parent.centerChild;
                node.parent.value3 = node.value2;
               
                TwoFourTreeItem cr = new TwoFourTreeItem(node.value1);
                node.value1 = node.value3;

                node.parent.centerRightChild = cr;
                cr.leftChild =node.leftChild;
                cr.rightChild= node.centerLeftChild;

                node.leftChild =node.centerRightChild;

                node.value2 =node.value3 = 0;
                cr.parent=node.parent;

                if(hasChildren(node)) {node.isLeaf =false;}
                if(hasChildren(cr) ){
                    cr.isLeaf =false;
                    cr.leftChild.parent = cr;
                    cr.rightChild.parent = cr;
                }
              
                 node.parent.isLeaf = false;
                 returnToTwo(node);
                 makeFour(node.parent);
                
            }
            else{
                reset(node.parent, node.parent.value1, node.value2, node.parent.value2);

                node.parent.centerLeftChild = node;

                TwoFourTreeItem cr = new TwoFourTreeItem(node.value3);
                reset(node, node.value1, 0 ,0);

                cr.leftChild =node.centerRightChild;
                cr.rightChild=node.rightChild;
                node.rightChild = node.centerLeftChild;

                node.parent.centerRightChild =cr;
                
                cr.parent = node.parent;
               
                if(hasChildren(node) ){node.isLeaf =false;}
                if(hasChildren(cr) ){cr.isLeaf =false;}

                 node.parent.isLeaf = false;
                 returnToTwo(node);
                 makeFour(node.parent);
            }
        }
    return node.parent;
}
    private boolean hasChildren(TwoFourTreeItem node){
        if(node.leftChild != null && node.rightChild != null){
            return true;
        }
        else{
            return false;
        }
    }

    private void returnToTwo(TwoFourTreeItem node){
       // two nodes should have these children be null
            node.centerChild = null;
            node.centerRightChild = null;
            node.centerLeftChild = null;

            node.values =1;
        }
    private void returnToThree(TwoFourTreeItem node){
         // three nodes should have these children be null
            node.centerLeftChild = null;
            node.centerRightChild = null;
            node.values =2;
        }
    private void makeFour(TwoFourTreeItem node){

        // four nodes should have this child be null
        node.centerChild = null;
         node.values =3;
    }

    private TwoFourTreeItem rootFuse(TwoFourTreeItem node, TwoFourTreeItem lc, TwoFourTreeItem rc)
    {
        reset(node, lc.value1, node.value1, rc.value1);
      node.leftChild = lc.leftChild;
      node.centerLeftChild = lc.rightChild;
      node.centerRightChild =rc.leftChild;
      node.rightChild =rc.rightChild;

      makeFour(node);
      lc =null;
      rc = null;

      if(hasChildren(node)){
        node.isLeaf = false;
      node.leftChild.parent = node;
      node.centerLeftChild.parent = node;
      node.centerRightChild.parent=node;
      node.rightChild.parent = node;
      }
      else{
        node.isLeaf = true;
      }
      return node;

    }

    private TwoFourTreeItem rightRotation(TwoFourTreeItem node, TwoFourTreeItem R, TwoFourTreeItem p){

        
        if(p.isTwoNode()){
       
        reset(node, node.value1, p.value1,0);
        reset(p, R.value1, 0,0);
          if(R.isThreeNode()){
                
            node.centerChild = node.rightChild;
            node.rightChild = R.leftChild;
            R.leftChild = R.centerChild;

            returnToTwo(R);
            }
      //if R is four node
            else{
                node.centerChild = node.rightChild;
                node.rightChild = R.leftChild;
                R.leftChild = R.centerLeftChild;
                R.centerChild = R.centerRightChild;
                returnToThree(R);
            }

            if(hasChildren(node)){
                node.rightChild.parent = node;
            }
            if(hasChildren(R)){
                R.isLeaf = false;
                node.rightChild.parent = node;
                R.leftChild.parent = R;
               
            }

                returnToThree(node);
            }

        else if(p.isThreeNode()){

        if(R == p.centerChild)
        {
            reset(node, node.value1, p.value1, 0);
            reset(p, R.value1,  p.value2,0);
            if(R.isThreeNode()){
                
                node.centerChild = node.rightChild;
                node.rightChild = R.leftChild;
                R.leftChild = R.centerChild;
                returnToTwo(R);
            }
            else{
                node.centerChild = node.rightChild;
                node.rightChild = R.leftChild;
                R.leftChild = R.centerLeftChild;
                R.centerChild = R.centerRightChild;
                returnToThree(R);
            }

                if(hasChildren(node)){
                    node.isLeaf = false;
                    node.rightChild.parent = node;

                }
                if(hasChildren(R)){
                    R.isLeaf = false;
                    R.leftChild.parent = R;
                    
                }
         
             returnToThree(node);
        }
        else{
      //if R is right child

            reset(node, node.value1, p.value2, 0);
            reset(p, p.value1,  R.value1,0);
            if(R.isThreeNode()){
                
            node.centerChild = node.rightChild;
            node.rightChild = R.leftChild;
            R.leftChild = R.centerChild;
            returnToTwo(R);
            }
            else{
                node.centerChild = node.rightChild;
            node.rightChild = R.leftChild;
            R.leftChild = R.centerLeftChild;
            R.centerChild = R.centerRightChild;
            returnToThree(R);
            }

             if(hasChildren(node)){
                    node.isLeaf = false;
                    node.rightChild.parent = node;

                }
                if(hasChildren(R)){
                    R.isLeaf = false;
                    R.leftChild.parent = R;
                }

            
            returnToThree(node);
    }
        }
        else{

            if(R == p.centerLeftChild){
            reset(node, node.value1, p.value1, 0);
            reset(p, R.value1 , p.value2, p.value3);

                if(R.isThreeNode()){
                    node.centerChild = node.rightChild;
                    node.rightChild = R.leftChild;
                    R.leftChild = R.centerChild;
                    returnToTwo(R);
                }
                else{
                    //r is four node
                    node.centerChild = node.rightChild;
                    node.rightChild = R.leftChild;
                    R.leftChild = R.centerLeftChild;
                    R.centerChild = R.centerRightChild;
                    returnToThree(R);
                }
                   if(hasChildren(node)){
                    node.isLeaf = false;
                    node.rightChild.parent = node;
                }
                if(hasChildren(R)){
                    R.isLeaf = false;
                    R.leftChild.parent = R;
                   
                }
        }
            else if(R == p.centerRightChild){

                reset(node, node.value1, p.value2, 0);
                reset(p, p.value1, R.value1, p.value3);

                if(R.isThreeNode()){
                    node.centerChild = node.rightChild;
                    node.rightChild = R.leftChild;
                    R.leftChild = R.centerChild;
                }
                else{
                    //r is four node
                    node.centerChild = node.rightChild;
                    node.rightChild = R.leftChild;
                    R.leftChild = R.centerLeftChild;
                    R.centerChild = R.centerRightChild;
                }

                

                if(hasChildren(node)){
                    node.isLeaf = false;
                    node.rightChild.parent = node; 
                    R.leftChild.parent = R; 
               
                }
                if(hasChildren(R)){
                                    R.isLeaf = false;
                                    R.leftChild.parent = R; 
                                    
                                }
        }
        else{
            reset(node,node.value1, p.value3, 0);
            reset(p, p.value1, p.value2, R.value1);
            if(R.isThreeNode()){
                node.centerChild = node.rightChild;
                node.rightChild = R.leftChild;
                R.leftChild = R.centerChild;
                returnToTwo(R);
            }
            else{
                //r is four node
                node.centerChild = node.rightChild;
                node.rightChild = R.leftChild;
                R.leftChild = R.centerLeftChild;
                R.centerChild = R.centerRightChild;
                returnToThree(R);
            }
      //R is rightChild

            if(hasChildren(node)){
                node.rightChild.parent = node;
                R.leftChild.parent = R;
            }
            if(hasChildren(R)){
                R.isLeaf = false;
                R.leftChild.parent = R;
                    }
        }
    }   
                   if(hasChildren(node)){
                    node.isLeaf = false;
                    node.rightChild.parent = node;
                }
                if(hasChildren(R)){
                    R.isLeaf = false;
                    R.leftChild.parent = R;
                }
                reset(R, R.value2, R.value3, 0);
                returnToThree(node);
    
        return p;
    }

    private TwoFourTreeItem leftSiblingNode(TwoFourTreeItem node, TwoFourTreeItem p)
    {
 
       
        if(p.isTwoNode()){
            if(node == p.leftChild){
            return null;
        }

            return p.leftChild;
        }

        else if(p.isThreeNode())
        {
            if(node == p.leftChild){
            return null;
            }
            else if(node == p.centerChild){
            return p.leftChild;
            }
    
             return p.centerChild;
        }
  
        else{
        if(node == p.leftChild){
            return null;
            }
            else if(node == p.centerLeftChild){
            return p.leftChild;
            }
            else if(node == p.centerRightChild){
            return p.centerLeftChild;
            }
            
            return p.centerRightChild;
        }

    }

    private TwoFourTreeItem rightSiblingNode(TwoFourTreeItem node, TwoFourTreeItem p){
        if(p.isTwoNode()){
            if(node == p.leftChild){
            return p.rightChild;
            }
            else{
            return null;
            }
            
        }
        else if(p.isThreeNode())
        {
            if(node == p.leftChild){
            return p.centerChild;
            }
            else if(node == p.centerChild){
            return p.rightChild;
            }
            else{
            return null;
            }
        
        }
        else{
        if(node == p.leftChild){
            return p.centerLeftChild;
            }
            else if(node == p.centerLeftChild){
            return p.centerRightChild;
            }
            else if(node == p.centerRightChild){
            return p.rightChild;
            }
            
            return null;
        }
}
    private TwoFourTreeItem rightSiblingMerge(TwoFourTreeItem node, TwoFourTreeItem R, TwoFourTreeItem pops){
       
        if(pops.isThreeNode()){
            if(R == pops.centerChild){
            reset(node, node.value1, pops.value1, R.value1);
                reset(pops, pops.value2, pops.value3, 0);
                reset(R, R.value2, R.value3,  0);

                node.centerLeftChild = node.rightChild;
                node.centerRightChild = R.leftChild;
                node.rightChild = R.rightChild;

              
                if(hasChildren(node)){
                node.isLeaf = false;
                (node.centerRightChild).parent = node;
                (node.rightChild).parent = node;
            }
                makeFour(node);
            }
            else{
            //r is pops right child

                reset(R,node.value1, pops.value2, R.value1);
                reset(pops, pops.value1, pops.value3, 0);
                reset(node, node.value2, pops.value3, 0);
              
                R.centerRightChild = R.leftChild;
                R.centerLeftChild = node.rightChild;
                R.leftChild = node.rightChild;

                if(hasChildren(R)){
                    R.isLeaf = false;
                     R.leftChild.parent = R;
                     R.centerLeftChild.parent = R;
            
                }

              
            makeFour(R);
            }

       returnToTwo(pops);
        
        }
        else if(pops.isFourNode()){
            if(R== pops.centerLeftChild){
                reset(node, node.value1, pops.value1, R.value1);
                reset(pops, pops.value2, pops.value3, 0);
                reset(R, R.value2, R.value3, 0);

                node.centerLeftChild = node.rightChild;
                node.centerRightChild = R.leftChild;
                node.rightChild = R.rightChild;
            
                pops.centerChild = pops.centerRightChild;

                
            }
            else if(R == pops.centerRightChild){
            reset(node, node.value1, pops.value2, R.value1);
                reset(pops, pops.value1, pops.value3, 0);
                reset(R, R.value2, R.value3, 0);

                node.centerLeftChild = node.rightChild;
                node.centerRightChild = R.leftChild;
                node.rightChild = R.rightChild;
            
                pops.centerChild = node;

            }
            else{
            reset(node, node.value1, pops.value3, R.value1);
                reset(pops, pops.value1, pops.value2, 0);
                reset(R, R.value2, R.value3, 0);

                node.centerLeftChild = node.rightChild;
                node.centerRightChild = R.leftChild;
                node.rightChild = R.rightChild;
            
                pops.centerChild = pops.centerLeftChild;
                pops.rightChild = node;
            //right child 

           // R.isLeaf= false;
        }
        if( hasChildren(node)){
            node.isLeaf = false;
            node.leftChild.parent = node;
            node.centerLeftChild.parent = node;
            node.centerRightChild.parent = node;
            node.rightChild.parent = node;
           // R.isLeaf= false;
        }
        
         makeFour(node);
        returnToThree(pops);
            }

        return pops;
    }
       
    
    private TwoFourTreeItem leftRotation(TwoFourTreeItem node, TwoFourTreeItem L, TwoFourTreeItem p){
 
        if(p.isTwoNode()){
            
            
                if(L.isThreeNode()){
                reset(node, p.value1, node.value1,node.value2);
                reset(p, L.value2, 0,0);
                reset(L, L.value1, L.value3 , 0);
            node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerChild;

            
            if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
            }
            returnToTwo(L);
            }
            //if R is four node
            else{
                reset(node, p.value1, node.value1,node.value2);
                reset(p, L.value3, 0,0);
                reset(L, L.value1, L.value2 , 0);
                node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerRightChild;
            L.centerChild = L.centerLeftChild;

            
            if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
                L.centerChild.parent = node;
            }

            returnToThree(L);
            }
            
        }
        else if(p.isThreeNode())
        {
            if(L == p.leftChild){
            if(L.isThreeNode()){
                reset(node, p.value1, node.value1, node.value2);
            reset(p, L.value2, p.value2, p.value3);
            reset(L, L.value1, L.value3, 0);

            node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerChild;
            if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
            }
            returnToTwo(L);
            
            }
            else{
                reset(node, p.value1, node.value1, node.value2);
            reset(p, L.value3, p.value2, p.value3);
            reset(L, L.value1, L.value2, 0);
            node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerRightChild;
            L.centerChild = L.centerLeftChild;
            if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
                L.centerChild.parent = node;
            }
            returnToThree(L);

            }

            
            }
            else if(L == p.centerChild){
            
            if(L.isThreeNode()){
                
                reset(node, p.value2, node.value1, node.value2);
            reset(p, p.value1,L.value2, p.value3);
            reset(L, L.value1, L.value3, 0);
                
            node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerChild;
            if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
            }
            returnToTwo(L);
            
            }
            else{
            reset(node, p.value2, node.value1, node.value2);
            reset(p, p.value1,L.value3, p.value3);
            reset(L, L.value1, L.value2, 0);

            node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerRightChild;
            L.centerChild = L.centerLeftChild;
                if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
                L.centerChild.parent = node;
            }
            returnToThree(L);

            }     
            }
            
        }
        else{
            //p is four node
            if(L == p.leftChild){

            if(L.isThreeNode()){
            reset(node, p.value1, node.value1, node.value2);
            reset(p, L.value2, p.value2, p.value3);
            reset(L, L.value1, L.value3, 0);

            node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerChild;

            if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
            }
            returnToTwo(L);
            }
            else{
            reset(node, p.value1, node.value1, node.value2);
            reset(p, L.value3, p.value2, p.value3);
            reset(L, L.value1, L.value2, 0);
            node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerRightChild;
            L.centerChild = L.centerLeftChild;

            if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
                L.centerChild.parent = node;
            }
            returnToThree(L);
            }

            }
            else if(L== p.centerLeftChild){
            

            if(L.isThreeNode()){
                reset(node, p.value2, node.value1, node.value2);
            reset(p, p.value1,L.value2, p.value3);
            reset(L, L.value1, L.value3, 0);

            node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerChild;

            if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
            }

            returnToTwo(L);
            }
            else{
                //r is four node
                reset(node, p.value2, node.value1, node.value2);
            reset(p, p.value1,L.value3, p.value3);
            reset(L, L.value1, L.value2, 0);

            node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerRightChild;
            L.centerChild = L.centerLeftChild;
            if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
                L.centerChild.parent = node;
            }
            returnToThree(L);

            }
            

            }
            else{
                
            if(L.isThreeNode()){
            reset(node, p.value3, node.value1, node.value2);
            reset(p, p.value1, p.value2,L.value2);
            reset(L, L.value1, L.value3, 0);

            node.centerChild = node.leftChild;
            node.leftChild = L.rightChild;
            L.rightChild = L.centerChild;

            if(hasChildren(L)){
                L.isLeaf = false;
                L.leftChild.parent = node;
            }

            returnToTwo(L);
            }
            else{
                    reset(node, p.value3, node.value1, node.value2);
                reset(p, p.value1,p.value2,L.value3);
                reset(L, L.value1, L.value2, 0);

                node.centerChild = node.leftChild;
                node.leftChild = L.rightChild;
                L.rightChild = L.centerRightChild;
                L.centerChild = L.centerLeftChild;
                
                if(hasChildren(L)){
                L.isLeaf = false;
                L.rightChild.parent = node;
                L.centerChild.parent = node;
            }
                //r is four node
                returnToThree(L);
                }
            //R is rightChild
            
            }
            
                }

                if(hasChildren(node)){
                node.isLeaf = false;
                node.centerChild.parent = node;
            }
            returnToThree(node);
            return node;
    }

    private TwoFourTreeItem leftSiblingMerge(TwoFourTreeItem node, TwoFourTreeItem L, TwoFourTreeItem pops){
        
        if(pops.isThreeNode()){
            //reset values in all three nodes
            //depending on location of left node reconnect children nodes
        if(L == pops.leftChild){

            reset(L, L.value1, pops.value1, node.value1);
            reset(pops, pops.value2, pops.value3, 0);
            reset(node, node.value2, node.value3, 0);

            L.centerLeftChild = L.rightChild;
            L.centerRightChild = node.leftChild;
            L.rightChild = node.rightChild;

            

        }
        else{
            //L is centerChild
            reset(L, L.value1, pops.value2, node.value1);
            reset(pops, pops.value1, pops.value3, 0);
            reset(node, node.value2, node.value3, 0);

            L.centerLeftChild = L.rightChild;
            L.centerRightChild = node.leftChild;
            L.rightChild = node.rightChild;

            pops.rightChild = L;
        }
        returnToTwo(pops);
        }
        else{
        // pops is four node
            //reset values in all three nodes
            //depending on location of left node reconnect children nodes
        if(L == pops.leftChild){
            reset(L, L.value1, pops.value1, node.value1);
            reset(pops, pops.value2, pops.value3, 0);
            reset(node, node.value2, node.value3, 0);

            L.centerLeftChild = L.rightChild;
            L.centerRightChild = node.leftChild;
            L.rightChild = node.rightChild;
            pops.centerChild = pops.centerRightChild;
            
        }
        else if(L == pops.centerLeftChild){
            reset(L, L.value1, pops.value2, node.value1);
            reset(pops, pops.value1, pops.value3, 0);
            reset(node, node.value2, node.value3, 0);

            L.centerLeftChild = L.rightChild;
            L.centerRightChild = node.leftChild;
            L.rightChild = node.rightChild;

            pops.centerChild = L;
        }
        else{
            //L must equal pops centerright

            //reset values in all three nodes
            reset(L, L.value1, pops.value3, node.value1);
            reset(pops, pops.value1, pops.value2, 0);
            reset(node, node.value2, node.value3, 0);

            //reconnect child and nodes to appropriate places
            L.centerLeftChild = L.rightChild;
            L.centerRightChild = node.leftChild;
            L.rightChild = node.rightChild;

            pops.centerChild = pops.centerLeftChild;
            pops.rightChild = L;
           
        
        }
        //reduce parent node to three node
        returnToThree(pops);
    }

    //if new node is not leaf connect  it's children
    if(hasChildren(L)){
        L.isLeaf = false;
            L.leftChild.parent = L;
            L.centerLeftChild.parent = L;
            L.centerRightChild.parent = L;
            L.rightChild.parent = L;
        }
        //returnToThree(L);
       
        return pops;
    }

    public boolean potentialNode(TwoFourTreeItem n, int v){
        if(n == null){return false;}

        //if value could be any of nodes value return true
        if(v != n.value1 || v != n.value2 || v != n.value3){
            return true;
            }

            return false;
    }
    private TwoFourTreeItem presuccessorNode (TwoFourTreeItem nodeChild)
    {   
        //returns node with largest predecessor value
        if(nodeChild.isLeaf)
        {
            return nodeChild;
        }
        return presuccessorNode(nodeChild.rightChild);
    }

    private TwoFourTreeItem successorNode (TwoFourTreeItem nodeChild)
    {   
        //returns node with lowest succesor value
        if(nodeChild.isLeaf)
        {
            return nodeChild;
        }
        return successorNode(nodeChild.leftChild);
    }

    public int getPredecessorVal(TwoFourTreeItem p){
	
        //returns appropriate presuccesor value depending on type of predecessor node
        if(p.isTwoNode())
	    {return p.value1;}
        else if(p.isThreeNode()){
		    return p.value2;
	    }
	    return p.value3;
    }

    private TwoFourTreeItem getleftChild(TwoFourTreeItem node, int val){
        
        //depending on location of value return closest right child
        if(node.isTwoNode()){
            return node.leftChild;
        }
        else if(node.isThreeNode()){
            if(val == node.value1){
                return node.leftChild;
            }
            return node.centerChild;
        }
        else{
        
            //assuming it is a 4 node
            if(val == node.value1){
                return node.leftChild;
            }
            else if(val ==node.value2){
                return node.centerLeftChild;
            }
            else{
            return node.centerRightChild;}
        }
    }

    private TwoFourTreeItem getRightChild(TwoFourTreeItem node, int val)
  {
        //depending on location of value return closest right child
            if(node.isTwoNode()){
            return node.rightChild;

            }
            else if(node.isThreeNode()){
                if(val == node.value1){
                    return node.centerChild;
                }
                return node.rightChild;
            }
            else{
        //assuming node is a 4 node
            if(val == node.value1){
                return node.centerLeftChild;
            }
            else if(val ==node.value2){
                return node.centerRightChild;
            }
            return node.rightChild;
        }
  }

  private TwoFourTreeItem subTreeMerge(TwoFourTreeItem node, TwoFourTreeItem L, TwoFourTreeItem R, int v){
   
   // depending on type of node and exact location of value merge with children
        if(node.isThreeNode()){
    
        if(v == node.value1){
            reset(L, L.value1, node.value1, R.value1);
            reset(node, node.value2, node.value3,0);
            L.centerLeftChild = L.rightChild;
            L.centerRightChild = R.leftChild;
            L.rightChild = R.rightChild;
        }
        else{
            reset(L, L.value1, node.value2, R.value1);
            reset(node, node.value1, node.value3,0);
            L.centerLeftChild = L.rightChild;
            L.centerRightChild = R.leftChild;
            L.rightChild = R.rightChild;
            
            node.rightChild = L;
        }
    
            returnToTwo(node);
        }
        else if(node.isFourNode()){
            if(v == node.value1){
                reset(L, L.value1, node.value1, R.value1);
                reset(node, node.value2, node.value3,0);
                L.centerLeftChild = L.rightChild;
                L.centerRightChild = R.leftChild;
                L.rightChild = R.rightChild;
                
                node.centerChild = node.centerRightChild;
            }
            else if(v == node.value2){
                reset(L, L.value1, node.value2, R.value1);
                reset(node, node.value1, node.value3,0);
                L.centerLeftChild = L.rightChild;
                L.centerRightChild = R.leftChild;
                L.rightChild = R.rightChild;
                
                node.centerChild = L;
            
            }
            else{
                reset(L, L.value1, node.value3, R.value1);
                reset(node, node.value1, node.value2,0);
                L.centerLeftChild = L.rightChild;
                L.centerRightChild = R.leftChild;
                L.rightChild = R.rightChild;
                
                node.rightChild = L;
                node.centerChild = node.centerLeftChild;
            }
        
            returnToThree(node);
        }

        //turn new 4 node into node with appropriate 4 node children
        makeFour(L);
        
        //if new node is not leaf assign new child parent to this node
        if(hasChildren(L)){
            L.isLeaf = false;
            L.centerRightChild.parent = L;
            L.rightChild.parent = L;
        }

        //return node for further conditions and traversing
            return node;
    }

}
