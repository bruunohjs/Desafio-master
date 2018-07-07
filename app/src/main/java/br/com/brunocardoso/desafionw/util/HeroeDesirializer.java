package br.com.brunocardoso.desafionw.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.brunocardoso.desafionw.model.Hero;

public class HeroeDesirializer implements JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement hero = json.getAsJsonObject();

        if ( hero.getAsJsonObject().get("heroe") != null){
            hero = json.getAsJsonObject().get("heroe");
        }

        return (new Gson().fromJson(hero, Hero.class));
    }
}
