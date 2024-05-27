class Cte {
    private Object value;
    private int dir;


    public Cte(Object value, int dir) {
        this.value = value;
        this.dir = dir;
    }

    public int getDir() {
        return dir;
    }

    public Object getValue() {
        return value;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}