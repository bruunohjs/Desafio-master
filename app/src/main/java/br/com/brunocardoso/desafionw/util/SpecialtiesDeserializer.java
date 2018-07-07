package br.com.brunocardoso.desafionw.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.brunocardoso.desafionw.model.Specialty;

public class SpecialtiesDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement specialties = json.getAsJsonObject();

        if ( specialties.getAsJsonObject().get("specialties") != null){
            specialties = json.getAsJsonObject().get("specialties");
        }

        return (new Gson().fromJson(specialties, Specialty.class));
    }
}
