package com.ankush.cleancity.Features.Feature;

import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Feature {
    @Getter
    String name;

    private final Path2D path;

    public Feature(JSONObject object) {
        List<Point2D> points = new ArrayList<>();
        name = (String) ((JSONObject) object.get("properties")).get("name-mr");
        if(name==null){
            name = (String) ((JSONObject) object.get("properties")).get("Zone_Name");
        }
        JSONArray array = (JSONArray) (((JSONObject) object.get("geometry")).get("coordinates"));
        if (array.size() > 1) {
            throw new UnsupportedOperationException("CURRENTLY ONLY SUPPORT One geometry per feature");
        }
        array = (JSONArray) array.get(0);
        for (Object o : array) {
            JSONArray obj = (JSONArray) o;
            double x = (double) obj.get(0);
            double y = (double) obj.get(1);
            points.add(new Point2D.Double(x, y));
        }

        path = new Path2D.Double();
        path.moveTo(points.get(0).getX(), points.get(0).getY());
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).getX(), points.get(i).getY());
        }
        path.closePath();
    }

    public boolean intersects(Point2D point) {

        return path.contains(point);
    }

    public Rectangle2D getBounds() {
        return path.getBounds2D();
    }

    public boolean inBounds(Point2D point) {
        return getBounds().contains(point);
    }

    @Override
    public String toString() {
        return String.format("[%s]", name);
    }
}
