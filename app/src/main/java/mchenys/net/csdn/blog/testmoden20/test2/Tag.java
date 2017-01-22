package mchenys.net.csdn.blog.testmoden20.test2;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by mChenys on 2017/1/15.
 */
public class Tag implements Parcelable {
    public int type = -1;//类型
    public float ratioX;//按下时相对父控件的位置百分比0-1
    public float ratioY;//按下时相对父控件的位置百分比0-1

    public String tagName1;
    public String tagValue1;
    public String tagName2;
    public String tagValue2;
    public String tagName3;
    public String tagValue3;


    public void setType(int type) {
        this.type = type;
    }

    public void setRatioX(float touchXP) {
        this.ratioX = touchXP;
    }

    public void setRatioY(float touchYP) {
        this.ratioY = touchYP;
    }

    public void setTag1(String tagName1, String tagValue1) {
        this.tagName1 = tagName1;
        this.tagValue1 = tagValue1;
    }

    public void setTag2(String tagName2, String tagValue2) {
        this.tagName2 = tagName2;
        this.tagValue2 = tagValue2;
    }

    public void setTag3(String tagName3, String tagValue3) {
        this.tagName3 = tagName3;
        this.tagValue3 = tagValue3;
    }

    public int getTagValueCount() {
        int count = 0;
        if (!TextUtils.isEmpty(tagValue1) || !TextUtils.isEmpty(tagName1)) count++;
        if (!TextUtils.isEmpty(tagValue2) || !TextUtils.isEmpty(tagName2)) count++;
        if (!TextUtils.isEmpty(tagValue3) || !TextUtils.isEmpty(tagName3)) count++;
        return count;
    }

    /**
     * 根据最终的个数调整变量
     */
    public void adjustTagByCount() {
        String[] tempName = new String[3];
        String[] tempValue = new String[3];
        int index = 0;
        String[] nameArray = new String[]{tagName1, tagName2, tagName3};
        String[] valueArray = new String[]{tagValue1, tagValue2, tagValue3};
        for (String name : nameArray) {
            if (!TextUtils.isEmpty(name)) {
                tempName[index] = name;
                index++;
            }
        }
        index = 0;
        for (String value : valueArray) {
            if (!TextUtils.isEmpty(value)) {
                tempValue[index] = value;
                index++;
            }
        }
        tagName1 = tempName[0];
        tagName2 = tempName[1];
        tagName3 = tempName[2];

        tagValue1 = tempValue[0];
        tagValue2 = tempValue[1];
        tagValue3 = tempValue[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeFloat(this.ratioX);
        dest.writeFloat(this.ratioY);
        dest.writeString(this.tagName1);
        dest.writeString(this.tagValue1);
        dest.writeString(this.tagName2);
        dest.writeString(this.tagValue2);
        dest.writeString(this.tagName3);
        dest.writeString(this.tagValue3);
    }

    public Tag() {
    }

    protected Tag(Parcel in) {
        this.type = in.readInt();
        this.ratioX = in.readFloat();
        this.ratioY = in.readFloat();
        this.tagName1 = in.readString();
        this.tagValue1 = in.readString();
        this.tagName2 = in.readString();
        this.tagValue2 = in.readString();
        this.tagName3 = in.readString();
        this.tagValue3 = in.readString();
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
