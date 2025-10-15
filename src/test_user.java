//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public class test_user {
//    // 定义连接数据库URL常量
//    static final String DB_URL = "jdbc:mysql://localhost:3306/test_user?useSSL=false&serverTimezone=UTC";
//
//    // 定义数据库用户名
//    static String user = "root";
//
//    // 定义对应的登录密码
//    static String password = "123456";
//
//    public static void main(String[] args) {
//        Connection connection = null;
//        Statement statement = null;
//
//        // 定义一条向数据表 user 插入数据的 SQL 语句
//        // String sql_insert = "INSERT INTO user VALUES(1,'admin_01','admin',1314)";
//
//        String sql_insert = "INSERT INTO user (username, role, code) VALUES ('admin_01', 'admin', 1314)";
//
//        try {
//            // 建立数据库连接
//            connection = DriverManager.getConnection(DB_URL, user, password);
//
//            // 获取执行 SQL 语句的对象
//            statement = connection.createStatement();
//
//            // 执行插入 SQL 并获取结果
//            int result = statement.executeUpdate(sql_insert);
//
//            // 控制台输出结果
//            System.out.println("执行结果：" + result + "（1 表示成功）");
//
//        } catch (SQLException e) {
//            System.out.println("数据库连接或执行失败！");
//            e.printStackTrace();
//        } finally {
//            // 释放资源
//            if (statement != null) {
//                try {
//                    statement.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
