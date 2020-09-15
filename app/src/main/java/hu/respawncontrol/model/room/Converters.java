package hu.respawncontrol.model.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromResourceIdList(List<String> soundResourceEntryNames) {
        if(soundResourceEntryNames == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(soundResourceEntryNames, type);
        return json;
    }

    @TypeConverter
    public static List<String> toResourceIdList(String soundResourceEntryNameString) {
        if(soundResourceEntryNameString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> soundResourceEntryNames = gson.fromJson(soundResourceEntryNameString, type);
        return soundResourceEntryNames;
    }
}
