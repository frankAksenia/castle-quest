package server.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import server.model.Coordinate;
import server.model.MapField;

@Service
public class CombiningHalfmapsService {

	public Map<Coordinate, MapField> combineHalfmaps(Map<Coordinate, MapField> firstMap, Map<Coordinate, MapField> secondMap) {
		return new HashMap<Coordinate, MapField>();
	}
}
