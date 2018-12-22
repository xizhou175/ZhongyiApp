package com.hfad.zhongyi;

public class Config {
    private static Config config = null;
    public String server_url;
    private Config() {
        // use "http://10.0.2.2:8080/registration" for emulator
        server_url = "http://10.0.0.9:8080";
        // server_url = "http://3.16.147.178:8080"; // ec2 server
    }
    public static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }
}
