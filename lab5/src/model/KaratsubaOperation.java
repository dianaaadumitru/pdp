package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KaratsubaOperation {
    public Polynomial sequentialForm(Polynomial x, Polynomial y) {
        System.out.println("hello");
        if (x.getDegree() < 2 || y.getDegree() < 2) {
            RegularOnOperation regularOnOperation = new RegularOnOperation();
            return regularOnOperation.sequentialForm(x, y);
        }

        // divide polynomials
        int len = Math.max(x.getDegree(), y.getDegree()) / 2;
        Polynomial firstHalfX = new Polynomial(x.getCoefficients().subList(0, len));
        Polynomial secondHalfX = new Polynomial(x.getCoefficients().subList(len, x.getLength()));
        Polynomial firstHalfY = new Polynomial(y.getCoefficients().subList(0, len));
        Polynomial secondHalfY = new Polynomial(y.getCoefficients().subList(len, y.getLength()));

        // step1 (a*c)
        Polynomial z1 = sequentialForm(firstHalfX, firstHalfY);
        //step2 (b*d)
        Polynomial z2 = sequentialForm(secondHalfX, secondHalfY);
        //step3 (a+b)(c+d)
        Polynomial z3 = sequentialForm(addPolynomials(firstHalfX, secondHalfX), addPolynomials(firstHalfY, secondHalfY));

        //calculate the final result
        Polynomial r1 = addZeros(z2, 2 * len);
        Polynomial r2 = addZeros(subtractPolynomials(subtractPolynomials(z3, z2), z1), len);

        return addPolynomials(addPolynomials(r1, r2), z1);
    }

    public Polynomial parallelizedForm(Polynomial x, Polynomial y) {
        // TODO implementation
        if (x.getDegree() < 2 || y.getDegree() < 2) {
            return sequentialForm(x, y);
        }
        return new Polynomial(0);
    }

    public Polynomial addPolynomials(Polynomial x, Polynomial y) {
        int minDegree = Math.min(x.getDegree(), y.getDegree());
        int maxDegree = Math.max(x.getDegree(), y.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(x.getCoefficients().get(i) + y.getCoefficients().get(i));
        }

        addRemainingCoefficients(x, y, minDegree, maxDegree, coefficients);

        return new Polynomial(coefficients);
    }

    public Polynomial subtractPolynomials(Polynomial x, Polynomial y) {
        int minDegree = Math.min(x.getDegree(), y.getDegree());
        int maxDegree = Math.max(x.getDegree(), y.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(x.getCoefficients().get(i) - y.getCoefficients().get(i));
        }

        addRemainingCoefficients(x, y, minDegree, maxDegree, coefficients);

        int i = coefficients.size() - 1;
        while (i > 0 && coefficients.get(i) == 0) {
            coefficients.remove(i);
            i--;
        }

        return new Polynomial(coefficients);
    }

    private void addRemainingCoefficients(Polynomial x, Polynomial y, int minDegree, int maxDegree, List<Integer> coefficients) {
        if (minDegree != maxDegree) {
            if (maxDegree == x.getDegree()) {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(x.getCoefficients().get(i));
                }
            } else {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(y.getCoefficients().get(i));
                }
            }
        }
    }

    public Polynomial addZeros(Polynomial x, int offset) {
        List<Integer> coefficients = IntStream.range(0, offset).mapToObj(i -> 0).collect(Collectors.toList());
        coefficients.addAll(x.getCoefficients());
        return new Polynomial(coefficients);
    }
}
