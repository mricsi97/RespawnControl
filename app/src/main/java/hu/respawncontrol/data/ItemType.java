package hu.respawncontrol.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ItemType implements Parcelable {
    private String name;
    private List<Item> items;

    public ItemType(String name, List<Item> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    ////////////////
    // Parcelable //
    ////////////////
    protected ItemType(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0x01) {
            items = new ArrayList<Item>();
            in.readList(items, Item.class.getClassLoader());
        } else {
            items = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (items == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(items);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ItemType> CREATOR = new Parcelable.Creator<ItemType>() {
        @Override
        public ItemType createFromParcel(Parcel in) {
            return new ItemType(in);
        }

        @Override
        public ItemType[] newArray(int size) {
            return new ItemType[size];
        }
    };
}

//public class ItemType implements Parcelable {
//    private String name;
//    private List<Item> items;
//
//    public ItemType(String name, List<Item> items) {
//        this.name = name;
//        this.items = items;
//    }
//
//    protected ItemType(Parcel in) {
//        name = in.readString();
//        items = in.createTypedArrayList(Item.CREATOR);
//    }
//
//    public String getName() {
//        return name;
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
//        dest.writeTypedList(items);
//    }
//
//    public static final Creator<ItemType> CREATOR = new Creator<ItemType>() {
//        @Override
//        public ItemType createFromParcel(Parcel in) {
//            return new ItemType(in);
//        }
//
//        @Override
//        public ItemType[] newArray(int size) {
//            return new ItemType[size];
//        }
//    };
//}
