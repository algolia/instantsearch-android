package dx;

import com.algolia.instantsearch.core.connection.ConnectionHandler;
import com.algolia.instantsearch.core.connection.ConnectionImpl;

import org.junit.Test;


public class JavaDX {

    @Test
    public final void testConnection() {
        ConnectionHandler handler = new ConnectionHandler();
        handler.add(new ConnectionImpl());
        handler.disconnect();
    }
}