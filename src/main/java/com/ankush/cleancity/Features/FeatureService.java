package com.ankush.cleancity.Features;

import com.ankush.cleancity.Features.Feature.City;
import com.ankush.cleancity.Features.Feature.Feature;
import lombok.Getter;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Component
public class FeatureService {
    @Getter
    City city;
    private final HashMap<String, City> map = new HashMap<>();

    public FeatureService() throws IOException, ParseException {
        map.put("pune", new City(new File("res/pune.geojson")));
        map.put("chennai", new City(new File("res/chennai.geojson")));
    }

    public Optional<Feature> getFromCoords(String name, double lat, double lon) {
        return getFromCoords(this.getCity(name), lat, lon);
    }

    public Optional<Feature> getFromCoords(City city, double lat, double lon) {
        return city.intersectFeatures(new Point2D.Double(lat, lon)).stream().findFirst();
    }


    public Optional<Feature> getFromCoords(double lat, double lon) {
        Point2D p = new Point2D.Double(lat, lon);
        Optional<City> city = this.map.values().stream().filter(x -> x.getBounds().contains(p)).findFirst();
        if (city.isEmpty()) return Optional.empty();
        return getFromCoords(city.get(), lat, lon);
    }

    public City getCity(String s) {
        if (!map.containsKey(s)) throw new RuntimeException(String.format("CITY %s NOT FOUND", s));
        return map.get(s);
    }
}
