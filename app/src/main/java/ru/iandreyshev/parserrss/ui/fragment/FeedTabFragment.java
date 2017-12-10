package ru.iandreyshev.parserrss.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.IViewRssArticle;
import ru.iandreyshev.parserrss.ui.activity.FeedActivity;
import ru.iandreyshev.parserrss.ui.adapter.ArticlesListAdapter;
import ru.iandreyshev.parserrss.ui.listeners.IOnUpdateRssListener;

public class FeedTabFragment extends Fragment {
    private static final String TAG = FeedTabFragment.class.getName();
    private static final String UPDATING_KEY = "UPDATING_KEY";
    private static final String RSS_KEY = "RSS_KEY";
    private static final String ARTICLES_KEY = "ARTICLES_KEY";

    private IViewRss mRss;
    private ArticlesListAdapter mListAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    public static FeedTabFragment newInstance(final IViewRss rss) {
        final FeedTabFragment fragment = new FeedTabFragment();
        final Bundle fragmentState = new Bundle();
        fragmentState.putSerializable(RSS_KEY, rss);
        fragmentState.putParcelableArrayList(ARTICLES_KEY, rss.getArticles());
        fragment.setArguments(fragmentState);

        return fragment;
    }

    public void startUpdate(boolean isStart) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(isStart);
        }
    }

    public void update(final ArrayList<IViewRssArticle> newArticles) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstantState) {
        mRss = (IViewRss) getArguments().getSerializable(RSS_KEY);
        final View view = inflater.inflate(R.layout.feed_list, viewGroup, false);

        initListAdapter();
        initRefreshLayout(view, savedInstantState);
        initRecyclerView(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(UPDATING_KEY, mRefreshLayout.isRefreshing());
        outState.putSerializable(RSS_KEY, mRss);
        outState.putParcelableArrayList(ARTICLES_KEY, mListAdapter.getArticles());
    }

    private void initListAdapter() {
        mListAdapter = new ArticlesListAdapter(getContext());
        mListAdapter.setArticleClickListener((FeedActivity) getContext());

        if (getArguments() != null) {
            mListAdapter.setArticles(getArguments().getParcelableArrayList(ARTICLES_KEY));
        }
    }

    private void initRefreshLayout(final View fragmentView, final Bundle savedInstantState) {
        mRefreshLayout = fragmentView.findViewById(R.id.feed_refresh_layout);
        mRefreshLayout.setOnRefreshListener(() -> {
            final IOnUpdateRssListener listener = (FeedActivity) getContext();
            if (listener != null) {
                listener.onUpdateRss(mRss);
            }
        });

        if (savedInstantState != null) {
            mRefreshLayout.setRefreshing(savedInstantState.getBoolean(UPDATING_KEY));
        }
    }

    private void initRecyclerView(final View fragmentView) {
        final RecyclerView listView = fragmentView.findViewById(R.id.feed_items_list);
        listView.setAdapter(mListAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }
}
