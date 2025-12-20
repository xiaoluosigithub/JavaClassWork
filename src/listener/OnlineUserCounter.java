package listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

// 在线用户计数器（监听会话创建/销毁事件）
@WebListener
public class OnlineUserCounter implements HttpSessionListener, HttpSessionAttributeListener {
    // 在线用户计数器（静态整数，非线程安全）
    public static int count = 0;
    // 当前用户会话属性键名 
    private static final String KEY = "currentUser";
    // 获取当前在线用户数量（非线程安全）
    public static int getCount() {
        return count;
    }
    // 会话销毁时，若会话属性中包含当前用户，则将在线用户数量减一
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Object v = se.getSession().getAttribute(KEY);
        if (v != null) {
            count--;
        }
    }
    // 会话属性添加时，若属性键为当前用户且值非空，则将在线用户数量加一
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (KEY.equals(event.getName()) && event.getValue() != null) {
            count++;
        }
    }
    // 会话属性移除时，若属性键为当前用户且值非空，则将在线用户数量减一
    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (KEY.equals(event.getName()) && event.getValue() != null) {
            count--;
        }
    }
    // 会话属性替换时，根据旧值和新值判断是否改变在线用户数量
    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        // 若属性键为当前用户
        if (KEY.equals(event.getName())) {
            // 获取新值和旧值
            Object newVal = event.getSession().getAttribute(KEY);
            Object oldVal = event.getValue();
            // 若旧值为空且新值非空，则将在线用户数量加一
            if (oldVal == null && newVal != null) {
                count++;
            }
            // 若旧值非空且新值为空，则将在线用户数量减一
            else if (oldVal != null && newVal == null) {    
                count--;
            }
        }
    }
}
