package listener;

import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class OnlineUserCounter implements HttpSessionListener, HttpSessionAttributeListener {
    private static final AtomicInteger counter = new AtomicInteger(0);
    public static volatile int count = 0;
    private static final String KEY = "currentUser";

    public static int getCount() {
        return counter.get();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Object v = se.getSession().getAttribute(KEY);
        if (v != null) {
            count = counter.decrementAndGet();
        }
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (KEY.equals(event.getName()) && event.getValue() != null) {
            count = counter.incrementAndGet();
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (KEY.equals(event.getName()) && event.getValue() != null) {
            count = counter.decrementAndGet();
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        if (KEY.equals(event.getName())) {
            Object newVal = event.getSession().getAttribute(KEY);
            Object oldVal = event.getValue();
            if (oldVal == null && newVal != null) {
                count = counter.incrementAndGet();
            } else if (oldVal != null && newVal == null) {
                count = counter.decrementAndGet();
            }
        }
    }
}