package com.scratchgame;

import com.google.gson.Gson;


import java.io.FileReader;

public class ConfigLoader {
    public static Config load(String path) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(new FileReader(path), Config.class);
    }
}
