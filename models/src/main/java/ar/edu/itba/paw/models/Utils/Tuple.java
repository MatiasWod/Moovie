package ar.edu.itba.paw.models.Utils;

public class Tuple<A,B>
{
    public final A first;
    public final B second;

    public Tuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
