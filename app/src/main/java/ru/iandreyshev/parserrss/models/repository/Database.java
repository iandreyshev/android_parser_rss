package ru.iandreyshev.parserrss.models.repository;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class Database {
    private BoxStore mBoxStore;
    private Box<Rss> mRssBox;
    private Box<Article> mArticleBox;

    public Database(@NonNull final BoxStore store) {
        mBoxStore = store;
        mRssBox = mBoxStore.boxFor(Rss.class);
        mArticleBox = mBoxStore.boxFor(Article.class);
    }

    @Nullable
    public Rss getRssById(long id) {
        final Rss rss = mRssBox.get(id);

        if (rss == null) {
            return null;
        }

        rss.setArticles(getArticlesByRssId(rss.getId()));

        return rss;
    }

    @Nullable
    public Article getArticleById(long id) {
        return mArticleBox.get(id);
    }

    public boolean isRssWithUrlExist(final String url) {
        return !mRssBox.find(Rss_.mUrl, url).isEmpty();
    }

    public void updateArticleImage(long id, byte[] image) {
        final Article article = mArticleBox.get(id);
        article.setImage(image);
        mArticleBox.put(article);
    }

    @NonNull
    public List<Rss> getAllRss() throws Exception {
        return mBoxStore.callInTx(() -> {
            final ArrayList<Rss> result = new ArrayList<>();

            for (final Rss rss : mRssBox.getAll()) {
                result.add(getRssById(rss.getId()));
            }

            return result;
        });
    }

    public boolean putRssIfSameUrlNotExist(final Rss newRss) throws Exception {
        return mBoxStore.callInTx(() -> {
            final Rss rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.mUrl, newRss.getUrl())
                    .build()
                    .findFirst();

            if (rssWithSameUrl != null) {
                return false;
            }

            mRssBox.put(newRss);
            putArticles(newRss);

            return true;
        });
    }

    public boolean updateRssWithSameUrl(final Rss newRss) throws Exception {
        return mBoxStore.callInTx(() -> {
            final Rss rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.mUrl, newRss.getUrl())
                    .build()
                    .findFirst();

            if (rssWithSameUrl == null) {
                return false;
            }

            newRss.setId(rssWithSameUrl.getId());
            mRssBox.put(newRss);
            putArticles(newRss);

            return true;
        });
    }

    public void removeRssById(long id) {
        mBoxStore.runInTx(() -> {
            mRssBox.remove(id);
            mArticleBox.query()
                    .equal(Article_.mRssId, id)
                    .build()
                    .remove();
        });
    }

    private void putArticles(final Rss rss) {
        bindArticles(rss);

        final HashSet<Article> newArticles = new HashSet<>(rss.getArticles());
        final List<Article> currentArticles = getArticlesByRssId(rss.getId());

        for (final Article article : currentArticles) {
            if (!newArticles.remove(article)) {
                mArticleBox.remove(article);
            }
        }

        mArticleBox.put(newArticles);
        rss.setArticles(getArticlesByRssId(rss.getId()));
    }

    private void bindArticles(final Rss rss) {
        for (final Article article : rss.getArticles()) {
            article.setRssId(rss.getId());
        }
    }

    @NonNull
    private List<Article> getArticlesByRssId(long id) {
        return mArticleBox.query()
                .equal(Article_.mRssId, id)
                .build()
                .find();
    }
}
