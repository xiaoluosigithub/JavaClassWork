package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    // 定义静态变量保存配置信息
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String DRIVER;

    static {
        try {
            // 加载 db.properties 文件
            Properties props = new Properties();
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            if (in == null) {
                throw new RuntimeException("❌ 未找到 db.properties 文件，请确认放在 src 根目录！");
            }
            props.load(in);

            // 读取配置
            DRIVER = props.getProperty("jdbc.driver");
            URL = props.getProperty("jdbc.url");
            USER = props.getProperty("jdbc.username");
            PASSWORD = props.getProperty("jdbc.password");

            // 注册数据库驱动
            Class.forName(DRIVER);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化数据库连接失败：" + e.getMessage());
        }
    }

    // 获取连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 关闭连接
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
