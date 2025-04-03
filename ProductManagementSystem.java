package exp7.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Scanner;

public class ProductManagementSystem {
    private static ProductManager productManager;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Database connection
            String url = "jdbc:mysql://localhost:3306/productdb";
            String user = "root";
            String password = "Manjula@27";  // MySQL Workbench password
            
            System.out.println("Connecting to database...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected!");
            
            productManager = new ProductManager(conn);

            while (true) {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addProduct();
                        break;
                    case 2:
                        viewProduct();
                        break;
                    case 3:
                        viewAllProducts();
                        break;
                    case 4:
                        updateProduct();
                        break;
                    case 5:
                        deleteProduct();
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        conn.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== Product Management System ===");
        System.out.println("1. Add New Product");
        System.out.println("2. View Product");
        System.out.println("3. View All Products");
        System.out.println("4. Update Product");
        System.out.println("5. Delete Product");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addProduct() {
        try {
            System.out.print("Enter Product ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            System.out.print("Enter Product Name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();
            
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            Product product = new Product(id, name, price, quantity);
            productManager.createProduct(product);
            System.out.println("Product added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewProduct() {
        try {
            System.out.print("Enter Product ID: ");
            int id = scanner.nextInt();
            
            Product product = productManager.readProduct(id);
            if (product != null) {
                System.out.println(product);
            } else {
                System.out.println("Product not found!");
            }
        } catch (Exception e) {
            System.out.println("Error viewing product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewAllProducts() {
        try {
            List<Product> products = productManager.getAllProducts();
            if (products.isEmpty()) {
                System.out.println("No products found!");
                return;
            }
            
            System.out.println("\nAll Products:");
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (Exception e) {
            System.out.println("Error viewing products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void updateProduct() {
        try {
            System.out.print("Enter Product ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Product existingProduct = productManager.readProduct(id);
            if (existingProduct == null) {
                System.out.println("Product not found!");
                return;
            }

            System.out.print("Enter new Product Name (or press Enter to skip): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                existingProduct.setProductName(name);
            }

            System.out.print("Enter new Price (or -1 to skip): ");
            double price = scanner.nextDouble();
            if (price != -1) {
                existingProduct.setPrice(price);
            }

            System.out.print("Enter new Quantity (or -1 to skip): ");
            int quantity = scanner.nextInt();
            if (quantity != -1) {
                existingProduct.setQuantity(quantity);
            }

            productManager.updateProduct(existingProduct);
            System.out.println("Product updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void deleteProduct() {
        try {
            System.out.print("Enter Product ID to delete: ");
            int id = scanner.nextInt();
            
            Product existingProduct = productManager.readProduct(id);
            if (existingProduct == null) {
                System.out.println("Product not found!");
                return;
            }

            productManager.deleteProduct(id);
            System.out.println("Product deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
