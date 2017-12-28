package ru.iandreyshev.parserrss.models.rss;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.Date;

import javax.annotation.Nullable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;
import ru.iandreyshev.parserrss.app.IBuilder;

@Entity
public final class RssArticle extends ViewRssArticle {
    private static final String TAG = RssArticle.class.getName();
    private static final Long NULL_DATE = -1L;

    @Id
    long mId;
    long mRssId;

    String mTitle;
    @Nullable
    String mOriginUrl;
    @Nullable
    String mDescription;
    @Nullable
    String mImageUrl;
    @Nullable
    Long mPostDate;

    @Transient
    private Bitmap mImage;

    private RssArticle() {
    }

    private RssArticle(Parcel in) {
        mTitle = in.readString();
        mOriginUrl = in.readString();
        mDescription = in.readString();
        mImage = in.readParcelable(Bitmap.class.getClassLoader());
        mImageUrl = in.readString();

        final Long postDate = in.readLong();
        mPostDate = (postDate.equals(NULL_DATE)) ? null : postDate;
    }

    public static final Creator<RssArticle> CREATOR = new Creator<RssArticle>() {
        @Override
        public RssArticle createFromParcel(Parcel in) {
            return new RssArticle(in);
        }

        @Override
        public RssArticle[] newArray(int size) {
            return new RssArticle[size];
        }
    };

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getOriginUrl() {
        return mOriginUrl;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public Long getPostDate() {
        return mPostDate;
    }

    @Override
    public Bitmap getImage() {
        return mImage;
    }

    @Override
    public String getImageUrl() {
        return mImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mOriginUrl);
        dest.writeString(mDescription);
        dest.writeParcelable(mImage, flags);
        dest.writeString(mImageUrl);
        dest.writeLong(mPostDate == null ? NULL_DATE : mPostDate);
    }

    void bindRss(final Rss rss) {
        mRssId = rss.getId();
    }

    static class Builder implements IBuilder<RssArticle> {
        private final RssArticle mArticle = new RssArticle();

        Builder(final String title) {
            mArticle.mTitle = title;
        }

        Builder setTitle(final String title) {
            mArticle.mTitle = title;

            return this;
        }

        Builder setDescription(final String text) {
            mArticle.mDescription = text;

            return this;
        }

        Builder setImage(final Bitmap image) {
            mArticle.mImage = image;

            return this;
        }

        Builder setDate(final Date date) {
            mArticle.mPostDate = date.getTime();

            return this;
        }

        Builder setImageUrl(final String url) {
            mArticle.mImageUrl = url;

            return this;
        }

        Builder setOrigin(final String origin) {
            mArticle.mOriginUrl = origin;

            return this;
        }

        @NonNull
        @Override
        public RssArticle build() {
            return mArticle;
        }
    }
}
