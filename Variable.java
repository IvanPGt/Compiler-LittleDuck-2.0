import java.util.*;

class Variable {
    private String id;
    private String tipo;

    public Variable(String id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public String getTipo() {
        return tipo;
    }
}