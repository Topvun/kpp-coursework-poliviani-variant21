package ua.edu.nupp.kpp.server;

public class RiskCalculator {

    // Розрахунок нормованого фактора F1 на основі ваги (наприклад, база — 10 кг)
    public static double factor1(double weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Вага не може бути від'ємною");
        }
        double value = (weight / 10.0) * 100.0;
        return Math.max(0.0, Math.min(100.0, value));
    }

    public static double calculate(double f1, double f2, double f3) {
        check(f1);
        check(f2);
        check(f3);

        double result = 0.40 * f1 + 0.35 * f2 + 0.25 * f3;
        return Math.round(result * 100.0) / 100.0;
    }

    private static void check(double value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Фактор має бути в діапазоні від 0 до 100");
        }
    }
}