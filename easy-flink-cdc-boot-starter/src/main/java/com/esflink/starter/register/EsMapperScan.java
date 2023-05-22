package com.esflink.starter.register;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MapperScannerRegister.class)
public @interface EsMapperScan {
    String value();
}
