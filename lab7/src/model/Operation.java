package model;

public class Operation {
    public static Polynomial buildResult(Object[] results) {
        int degree = ((Polynomial) results[0]).getDegree();
        Polynomial result = Polynomial.buildEmptyPolynomial(degree + 1);
        for (int i = 0; i < result.getCoefficients().size(); i++) {
            for (Object o : results) {
                result.getCoefficients().set(i, result.getCoefficients().get(i) + ((Polynomial) o).getCoefficients().get(i));
            }
        }
        return result;
    }
}
