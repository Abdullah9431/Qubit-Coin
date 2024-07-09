package qubit;

class Block implements Blockinterface{
        int BlockId;
        private Transaction transaction;
        String content;
        byte[] prevHash;
        int nonce = 1;

        

        public Block(Transaction transaction, int BlockId) {
            this.BlockId = BlockId;
            if (BlockId > 1) this.prevHash = null;
            this.transaction = transaction;
            this.content = BlockId + " " + transaction.toString() + " " + nonce;  
        }

        protected void updateNonce() {
            this.nonce++;
            this.content = BlockId + " " + transaction.toString() + " " + nonce;  

        }

        

    public void PrintBlock() {
        // Define the box-drawing characters
        char topLeft = '\u250C';     // ┌
        char topRight = '\u2510';    // ┐
        char horizontal = '\u2500';  // ─
        char vertical = '\u2502';    // │
        char bottomLeft = '\u2514';  // └
        char bottomRight = '\u2518'; // ┘

        // Print the top border of the box
        System.out.print(topLeft);
        int i;
        for (i = 0; i < 24; i++) {
            System.out.print(horizontal);
        }
        i += 1;
        System.out.println(topRight);
        String str1 = vertical + String.format("%16s", "Block ID: ") + this.BlockId;
        str1 += spacer(i - str1.length()) + vertical;
        String str2 = vertical + " Sender ID: " + this.transaction.sender  ;
        str2 += spacer(i - str2.length()) + vertical;
        String str3 = vertical + " Receiver ID: " + this.transaction.receiver;
        str3 += spacer(i - str3.length()) + vertical;
        String str4 = vertical + " Amount: " + this.transaction.amount;
        str4 += spacer(i - str4.length()) + vertical;
        String str5 = vertical + " Nuance: " + nonce;
        str5 += spacer(i - str5.length()) + vertical;  

        // Print the content of the block
        System.out.println(str1);
        System.out.println(str2);
        System.out.println(str3);
        System.out.println(str4);
        System.out.println(str5);

        // Print the bottom border of the box
        System.out.print(bottomLeft);
        for (i = 0; i < 24; i++) {
            System.out.print(horizontal);
        }
        System.out.println(bottomRight);
    }

    public String spacer(int n) {
        String res = "";
        for (int i = 0; i < n; i++) {
            res = res + " ";}
            return res;
    }

}

