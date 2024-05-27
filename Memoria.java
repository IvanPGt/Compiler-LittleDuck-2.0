class Memoria {
    public int[] i;
    public float[] f;
    public String[] s;
    public boolean[] b;


    public Memoria(int i_size, int f_size, int s_size, int b_size) {
        this.i = new int[i_size-1];
        this.f = new float[f_size-1];
        this.s = new String[s_size-1];
        this.b = new boolean[b_size-1];
    }
}