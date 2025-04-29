package live;

import live.di.Component;
import live.di.ComponentScanner;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Class<?>> scannedComponents = ComponentScanner.scanAllComponents();

    }
}