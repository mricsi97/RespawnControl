package hu.respawncontrol.model.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromResourceIdList(List<Integer> soundResourceIds) {
        if(soundResourceIds == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        String json = gson.toJson(soundResourceIds, type);
        return json;
    }

    @TypeConverter
    public static List<Integer> toResourceIdList(String soundResourceIdsString) {
        if(soundResourceIdsString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> soundResourceIds = gson.fromJson(soundResourceIdsString, type);
        return soundResourceIds;
    }
}
