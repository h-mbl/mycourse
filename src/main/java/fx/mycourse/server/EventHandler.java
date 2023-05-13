package fx.mycourse.server;

import java.io.IOException;

@FunctionalInterface
public interface EventHandler {
    void handle(String cmd, Object object) throws IOException;
}
