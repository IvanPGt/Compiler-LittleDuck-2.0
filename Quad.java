class Quad {
    private final int oper;
    private final int opdo1;
    private final int opdo2;
    private int res;

    public Quad(int oper, int opdo1, int opdo2, int res) {
        this.oper = oper;
        this.opdo1 = opdo1;
        this.opdo2 = opdo2;
        this.res = res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getOper () {
        return oper;
    }

    public int getRes() {
        return res;
    }

    public int getOpdo1 () {
        return opdo1;
    }

    public int getOpdo2 () {
        return opdo2;
    }



    @Override
    public String toString() {
        return  "( "+ oper + ", " + opdo1 + ", " + opdo2 + ", " + res + ")";
    }

}