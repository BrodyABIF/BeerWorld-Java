package at.spengergasse.vaadin.exception;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class BeerException
        extends RuntimeException {

    public enum Type {ERROR, WARNING, INFO, DEBUG}

    ;
    private Notification notification = new Notification();

    public BeerException(String message, Type type) {
        switch (type) {
            case ERROR:
                notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
                break;
            case WARNING:
                notification.show(message).addThemeVariants(NotificationVariant.LUMO_WARNING);
                break;
            default:
                notification.show(message);

        }
    }


    public BeerException(String message) {
        super(message);
    }

    public BeerException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeerException(Throwable cause) {
        super(cause);
    }

    public BeerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
