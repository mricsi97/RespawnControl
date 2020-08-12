package hu.respawncontrol;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String name;
    private Integer imageResourceId;
    private Integer respawnTimeInSeconds;

    public Item(String name, Integer imageResourceId, Integer respawnTimeInSeconds) {
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.respawnTimeInSeconds = respawnTimeInSeconds;

    }

    public String getName() {
        return name;
    }

    public Integer getRespawnTimeInSeconds() {
        return respawnTimeInSeconds;
    }

    public Integer getImageResourceId() {
        return imageResourceId;
    }

    ////////////////
    // Parcelable //
    ////////////////

    protected Item(Parcel in) {
        name = in.readString();
        respawnTimeInSeconds = in.readByte() == 0x00 ? null : in.readInt();
        imageResourceId = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (respawnTimeInSeconds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(respawnTimeInSeconds);
        }
        if (imageResourceId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(imageResourceId);
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