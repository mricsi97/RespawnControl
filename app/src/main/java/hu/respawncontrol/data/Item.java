package hu.respawncontrol.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Item implements Parcelable {
    private String name;
    private Integer imageResourceId;
    private List<Integer> soundResourceIds;
    private Integer respawnTimeInSeconds;

    public Item(String name, Integer imageResourceId, List<Integer> soundResourceIds, Integer respawnTimeInSeconds) {
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.soundResourceIds = soundResourceIds;
        this.respawnTimeInSeconds = respawnTimeInSeconds;
    }

    public String getName() {
        return name;
    }

    public Integer getImageResourceId() {
        return imageResourceId;
    }

    public List<Integer> getSoundResourceIds() {
        return soundResourceIds;
    }

    public Integer getRespawnTimeInSeconds() {
        return respawnTimeInSeconds;
    }

    ////////////////
    // Parcelable //
    ////////////////

    protected Item(Parcel in) {
        name = in.readString();
        imageResourceId = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            soundResourceIds = new ArrayList<Integer>();
            in.readList(soundResourceIds, Integer.class.getClassLoader());
        } else {
            soundResourceIds = null;
        }
        respawnTimeInSeconds = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (imageResourceId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(imageResourceId);
        }
        if (soundResourceIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(soundResourceIds);
        }
        if (respawnTimeInSeconds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(respawnTimeInSeconds);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}

//public class Item implements Parcelable {
//    private String name;
//    private Integer respawnTimeInSeconds;
//    private Integer imageResourceId;
//
//    public Item(String name, Integer respawnTimeInSeconds, Integer image) {
//        this.name = name;
//        this.respawnTimeInSeconds = respawnTimeInSeconds;
//        this.imageResourceId = image;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Integer getRespawnTimeInSeconds() {
//        return respawnTimeInSeconds;
//    }
//
//    public Integer getImage() {
//        return imageResourceId;
//    }
//
//    ////////////////
//    // Parcelable //
//    ////////////////
//    protected Item(Parcel in) {
//        name = in.readString();
//        imageResourceId = in.readByte() == 0x00 ? null : in.readInt();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(name);
//        if (imageResourceId == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeInt(imageResourceId);
//        }
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
//        @Override
//        public Item createFromParcel(Parcel in) {
//            return new Item(in);
//        }
//
//        @Override
//        public Item[] newArray(int size) {
//            return new Item[size];
//        }
//    };
//}