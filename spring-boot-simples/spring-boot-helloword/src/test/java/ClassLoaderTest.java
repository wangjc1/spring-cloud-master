import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class ClassLoaderTest {

    @Test
    public void test() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
        Enumeration<URL> urls =  classLoader.getResources(FACTORIES_RESOURCE_LOCATION);
        while (urls.hasMoreElements()){
            System.out.println(urls.nextElement());
        }

    }
}
