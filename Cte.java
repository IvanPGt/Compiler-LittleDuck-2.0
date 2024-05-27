class Cte {
    private int type;
    private Object value;
    private int dir;


    public Cte(int type, Object value, int dir) {
        this.type = type;
        this.value = value;
        this.dir = dir;
    }

    public int getDir() {
        return dir;
    }

    public Object getValue() {
        return value;
    }
    
    public int getType() {
        return type;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setType(int type) {
        this.type = type;
    }

}