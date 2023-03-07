package persistence;

import model.Product;
import org.json.JSONArray;
import org.json.JSONObject;

// Source:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
