package com.ankush.cleancity.Features.Feature;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
    public static JSONObject getJSON(File f) throws IOException, ParseException {
        StringBuilder sb = new StringBuilder();
        Files.lines(Path.of(f.getPath())).forEach(sb::append);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(sb.toString());
    }

    public static JSONArray getPointList(JSONObject o) {
        return (JSONArray) o.get("features");
    }

    public static List<Feature> loadFeatures(JSONObject features) {
        List<Feature> list = new ArrayList<Feature>();

        getPointList(features).forEach(x -> list.add(new Feature((JSONObject) x)));
        return list;
    }
}
