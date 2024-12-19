import java.util.Scanner;

public class CardAcceptor {
    public boolean processPayment(double amount) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите номер карты: ");
        String cardNumber = scanner.nextLine();
        System.out.println("Введите одноразовый пароль: ");
        String otp = scanner.nextLine();

        if (cardNumber.isEmpty() && !otp.isEmpty()) {
            System.out.println("Платеж принят с карты: " + cardNumber);
            return true;
        }
        return false;
    }
}
