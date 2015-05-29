package util;

import play.libs.F.Tuple;

public class Pair<A, B> extends Tuple<A, B> {

    public Pair(A _1, B _2) {
        super(_1, _2);
    }

    @Override
    public boolean equals(Object o) {
        Pair other = (Pair) o;
        return other._1.equals(_1) && other._2.equals(_2);
    }

    @Override
    public int hashCode() {
        return _1.hashCode() + _2.hashCode();
    }
}
