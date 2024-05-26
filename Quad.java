class Quad {
    private final String oper;
    private final int opdo1;
    private final int opdo2;
    private final int res;

    public Quad(String oper, int opdo1, int opdo2, int res) {
        this.oper = oper;
        this.opdo1 = opdo1;
        this.opdo2 = opdo2;
        this.res = res;
    }

    @Override
    public String toString() {
        return  "( "+ oper + ", " + opdo1 + ", " + opdo2 + ", " + res + ")";
    }

}