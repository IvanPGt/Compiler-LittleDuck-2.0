import java.util.*;

class Variable {
    private String id;
    private String tipo;
    private int dir;

    public Variable(String id, String tipo, int dir) {
        this.id = id;
        this.tipo = tipo;
        this.dir = dir;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getDir() {
        return dir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable tuple = (Variable) o;
        return id.equals(tuple.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    
}