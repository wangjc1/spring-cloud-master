package boot.custom.enableAutoConfigureAnno;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;

public class MyEnableAutoConfigurationImport2 extends AutoConfigurationImportSelector {

    protected Class<?> getAnnotationClass() {
        return MyEnableAutoConfiguration.class;
    }
}