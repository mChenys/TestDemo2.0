package mchenys.net.csdn.blog.testmoden20.test6.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mChenys on 2017/1/19.
 */
public class Tag implements Parcelable {

    public float ratioX;
    public float ratioY;
    public List<TagDesc> mDescList;

    public static class TagDesc implements Parcelable{
        public String tagName;
        public String tagValue;
        public int position;
        public float angle;

        public TagDesc() {
        }

        public TagDesc(String tagName, String tagValue, int position, float angle) {
            this.tagName = tagName;
            this.tagValue = tagValue;
            this.position = position;
            this.angle = angle;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.tagName);
            dest.writeString(this.tagValue);
            dest.writeInt(this.position);
            dest.writeFloat(this.angle);
        }

        protected TagDesc(Parcel in) {
            this.tagName = in.readString();
            this.tagValue = in.readString();
            this.position = in.readInt();
            this.angle = in.readFloat();
        }

        public static final Creator<TagDesc> CREATOR = new Creator<TagDesc>() {
            @Override
            public TagDesc createFromParcel(Parcel source) {
                return new TagDesc(source);
            }

            @Override
            public TagDesc[] newArray(int size) {
                return new TagDesc[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.ratioX);
        dest.writeFloat(this.ratioY);
        dest.writeList(this.mDescList);
    }

    public Tag() {
    }

    protected Tag(Parcel in) {
        this.ratioX = in.readFloat();
        this.ratioY = in.readFloat();
        this.mDescList = new ArrayList<TagDesc>();
        in.readList(this.mDescList, TagDesc.class.getClassLoader());
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
}
