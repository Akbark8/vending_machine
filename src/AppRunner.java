import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();
    private final CoinAcceptor coinAcceptor;
    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        println("В автомате доступны:");
        showProducts(products);

        println("Монет на сумму: " + coinAcceptor.getAmount());

        UniversalArray<Product> allowedProducts = getAllowedProducts();
        showActions(allowedProducts);
        chooseAction(allowedProducts);
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            println(product.getActionLetter() + " - " + product.getName() + " (Цена: " + product.getPrice() + ")");
        }
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowedProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowedProducts.add(products.get(i));
            }
        }
        return allowedProducts;
    }

    private void showActions(UniversalArray<Product> products) {
        println("Доступные действия:");
        println(" a - Пополнить баланс (на 10)");
        println(" c - Оплатить картой");

        if (products.size() > 0) {
            println(" Доступные для покупки товары:");
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                println("  " + product.getActionLetter() + " - Купить " + product.getName() + " (Цена: " + product.getPrice() + ")");
            }
        } else {
            println("  Нет доступных товаров для покупки.");
        }
        println(" h - Выйти");
    }

    private void chooseAction(UniversalArray<Product> products) {
        String action = fromConsole().substring(0, 1);

        if ("a".equalsIgnoreCase(action)) {
            coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
            println("Вы пополнили баланс на 10");
            return;
        }

        if ("c".equalsIgnoreCase(action)) {
            double totalCost = 0;
            for (int i = 0; i < products.size(); i++) {
                totalCost += products.get(i).getPrice();
            }
            CardAcceptor cardAcceptor = new CardAcceptor();
            if (cardAcceptor.processPayment(totalCost)) {
                println("Вы успешно оплатили " + totalCost + " с карты.");
                for (int i = 0; i < products.size(); i++) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                    println("Вы купили " + products.get(i).getName());
                }
            } else {
                println("Ошибка при оплате картой.");
            }
            return;
        }

        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    Product selectedProduct = products.get(i);
                    if (coinAcceptor.getAmount() >= selectedProduct.getPrice()) {
                        coinAcceptor.setAmount(coinAcceptor.getAmount() - selectedProduct.getPrice());
                        println("Вы купили " + selectedProduct.getName());
                    } else {
                        println("Недостаточно средств для покупки " + selectedProduct.getName());
                    }
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            } else {
                println("Недопустимая буква. Попробуйте еще раз.");
                chooseAction(products);
            }
        }
    }

    private String fromConsole() {
        println("Выберите действие: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void println(String msg) {
        System.out.println(msg);
    }
}
