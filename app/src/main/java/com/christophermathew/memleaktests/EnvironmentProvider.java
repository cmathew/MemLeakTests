package com.christophermathew.memleaktests;

public class EnvironmentProvider {
    public EnvironmentProvider() {

    }

    public boolean isProduction() {
        return true;
    }

    public boolean isDevelopment() {
        return false;
    }
}
