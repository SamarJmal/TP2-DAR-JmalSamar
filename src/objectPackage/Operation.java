package objectPackage;

import java.io.Serializable;

public class Operation implements Serializable {
    public double oper1;
    public char opr;
    public double oper2;

    public Operation(double operande1, char operateur, double operande2) {
        this.oper1 = operande1;
        this.opr = operateur;
        this.oper2 = operande2;
    }


}
