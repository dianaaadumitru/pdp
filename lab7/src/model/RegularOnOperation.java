package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RegularOnOperation {
    public static Polynomial multiplySimple(Object o, Object o1, int begin, int end) {
        Polynomial p = (Polynomial) o;
        Polynomial q = (Polynomial) o1;
        Polynomial result = Polynomial.buildEmptyPolynomial(p.getDegree()*2 + 1);
        for (int i = begin; i < end; i++) {
            for (int j = 0; j < q.getCoefficients().size(); j++) {
                result.getCoefficients().set(i + j, result.getCoefficients().get(i + j) + p.getCoefficients().get(i) * q.getCoefficients().get(j));
            }
        }
        return result;
    }

    public static Polynomial buildResult(Object[] results) {
        int degree = ((Polynomial) results[0]).getDegree();
        Polynomial result = Polynomial.buildEmptyPolynomial(degree+1);
        for (int i = 0; i < result.getCoefficients().size(); i++) {
            for (Object o : results) {
                result.getCoefficients().set(i, result.getCoefficients().get(i) + ((Polynomial) o).getCoefficients().get(i));
            }
        }
        return result;
    }

    public Polynomial sequentialForm(Polynomial x, Polynomial y) {
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
