module com.ritolaaudio.jfcdpp {
    exports com.ritolaaudio.jfcdpp;
    exports com.ritolaaudio.jfcdpp.javahidapi;
    exports com.ritolaaudio.jfcdpp.autoselect;
    exports com.ritolaaudio.jfcdpp.usb4java;
    exports com.ritolaaudio.jfcdpp.jnahidapi;

    requires hidapi;
    requires transitive java.logging;
    requires jna;
    requires usb.api;
}