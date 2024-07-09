package qubit;

class Transaction {
    protected int sender;
    protected int receiver;
    protected double amount;

    public Transaction(int sender, int receiver, double amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public String toString() {
        return this.sender + " " + this.receiver + " " + this.amount;
    }
}
