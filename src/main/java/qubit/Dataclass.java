package qubit;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Dataclass{
    final int Id;
    final PublicKey pubkey;
    final PrivateKey privkey;
    double balance;

    public Dataclass(int Id, PublicKey pubkey, PrivateKey privkey){
        this.Id = Id;
        this.pubkey = pubkey;
        this.privkey = privkey;
        this.balance = 1000.0;
    }

    public String toString(){
        return "ID: " + this.Id + " Balance: " + this.balance;
    }
}