package model;

import java.util.ArrayList;
import java.util.List;

public class RegularOnOperation {
    public static Polynomial sequentialForm(Object o, Object p, int begin, int end) {
        Polynomial x = (Polynomial) o;
        Polynomial y = (Polynomial) p;
        Polynomial result = Polynomial.buildEmptyPolynomial(x.getDegree() * 2 + 1);
        for (int i = begin; i < end; i++) {
            for (int j = 0; j < y.getCoefficients().size(); j++) {
                result.getCoefficients().set(i + j, result.getCoefficients().get(i + j) + x.getCoefficients().get(i) * y.getCoefficients().get(j));
            }
        }
        return result;
    }

    public Polynomial sequentialFormClassic(Polynomial x, Polynomial y) {
        int sizeOfResult = x.getDegree() + y.getDegree() + 1;
        List<Integer> coefficients = new ArrayList<>();

        for (int i = 0; i < sizeOfResult; i++) {
            coefficients.add(0);
        }

        for (int i = 0; i < x.getCoefficients().size(); i++) {
            for (int j = 0; j < y.getCoefficients().size(); j++) {
                int index = i + j;
                int value = x.getCoefficients().get(i) * y.getCoefficients().get(j);
                coefficients.set(index, coefficients.get(index) + value);
            }
        }
        return new Polynomial(coefficients);
    }
}
