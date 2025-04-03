package exp7.src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private Connection conn;

    public ProductManager(Connection conn) {
        this.conn = conn;
        initializeTable();
    }

    private void initializeTable() {
        try {
            Statement stmt = conn.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS Product " +
                               "(ProductID INT PRIMARY KEY, " +
                               "ProductName VARCHAR(100), " +
                               "Price DOUBLE, " +
                               "Quantity INT)";
            stmt.execute(createTable);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public void createProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Product (ProductID, ProductName, Price, Quantity) VALUES (?, ?, ?, ?)";
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product.getProductId());
            pstmt.setString(2, product.getProductName());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getQuantity());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public Product readProduct(int productId) throws SQLException {
        String sql = "SELECT * FROM Product WHERE ProductID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, productId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return new Product(
                rs.getInt("ProductID"),
                rs.getString("ProductName"),
                rs.getDouble("Price"),
                rs.getInt("Quantity")
            );
        }
        return null;
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Product");
        
        while (rs.next()) {
            products.add(new Product(
                rs.getInt("ProductID"),
                rs.getString("ProductName"),
                rs.getDouble("Price"),
                rs.getInt("Quantity")
            ));
        }
        return products;
    }

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?";
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getProductName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getQuantity());
            pstmt.setInt(4, product.getProductId());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM Product WHERE ProductID=?";
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
