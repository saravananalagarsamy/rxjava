/**
 * Created by m509575 on 23/08/17.
 */
public class Tuple<U,V> {

    private String V;

    @Override
    public String toString() {
        return "Tuple{" +
                "V='" + V + '\'' +
                ", U='" + U + '\'' +
                '}';
    }

    private String U;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;

        if (U != null ? !U.equals(tuple.U) : tuple.U != null) return false;
        return V != null ? V.equals(tuple.V) : tuple.V == null;
    }

    @Override
    public int hashCode() {
        int result = U != null ? U.hashCode() : 0;
        result = 31 * result + (V != null ? V.hashCode() : 0);
        return result;
    }

    public String getU() {
        return U;
    }

    public void setU(String u) {
        U = u;
    }

    public String getV() {
        return V;
    }

    public void setV(String v) {
        V = v;
    }

    public Tuple(String u, String v){
        this.U = u;
        this.V = v;
    }
}
