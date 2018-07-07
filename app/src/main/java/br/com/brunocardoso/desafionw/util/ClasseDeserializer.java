package br.com.brunocardoso.desafionw.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.brunocardoso.desafionw.model.Classe;

public class ClasseDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement classe = json.getAsJsonObject();

        if ( classe.getAsJsonObject().get("classe") != null){
            classe = json.getAsJsonObject().get("classe");
        }

        return (new Gson().fromJson(classe, Classe.class));
    }
}
