package blockchain;

interface Blockinterface {
    int BlockId = 0;
    Transaction transaction = null;
    String content = null;
    byte[] prevHash = null;
    final int n = 1;

    public void PrintBlock();
}