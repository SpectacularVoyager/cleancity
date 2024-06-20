package com.ankush.cleancity.Features.Feature;

import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class City {
    private final List<Feature> features;
    @Getter
    private JSONObject json;

    public List<Feature> getFeatures() {
        return features;
    }

    public City(File file) throws IOException, ParseException {

        JSONObject obj = FileParser.getJSON(file);
        features = FileParser.loadFeatures(obj);
        json = obj;
    }

    public List<Feature> intersectFeatures(Point2D p) {
        return features.stream().filter(x -> x.inBounds(p)).filter(x -> x.intersects(p)).collect(Collectors.toList());
    }

    public Rectangle2D getBounds() {
        return features.stream().map(Feature::getBounds).reduce(this::merge).get();
    }

    public Rectangle2D merge(Rectangle2D r1, Rectangle2D r2) {
        double x1 = Math.min(r1.getX(), r2.getX());
        double x2 = Math.max(r1.getX(), r2.getX());
        double y1 = Math.min(r1.getX(), r2.getX());
        double y2 = Math.max(r1.getY(), r2.getY());
        return new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
    }
}
