package ru.iandreyshev.parserrss.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.List;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.presentation.presenter.FeedTabPresenter;
import ru.iandreyshev.parserrss.presentation.presenter.ImagesLoadPresenter;
import ru.iandreyshev.parserrss.presentation.view.IFeedTabView;
import ru.iandreyshev.parserrss.presentation.view.IImageView;
import ru.iandreyshev.parserrss.ui.adapter.FeedListAdapter;
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener;

public class FeedPageFragment extends BaseFragment implements IFeedTabView, IImageView {
    @InjectPresenter(type = PresenterType.GLOBAL, tag = "ImageLoadPresenter")
    ImagesLoadPresenter mImageLoadPresenter;
    @InjectPresenter
    FeedTabPresenter mPresenter;

    private IViewRss mRss;
    private FeedListAdapter mListAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    public static FeedPageFragment newInstance(final IViewRss rss) {
        final FeedPageFragment fragment = new FeedPageFragment();
        fragment.mRss = rss;

        return fragment;
    }

    public IViewRss getRss() {
        return mRss;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.init(mRss);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstantState) {
        final View view = inflater.inflate(R.layout.feed_list, viewGroup, false);

        initListAdapter(view);
        initRefreshLayout(view);
        initRecyclerView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListAdapter.updateImages(true);
    }

    @Override
    public void startUpdate(boolean isStart) {
        mRefreshLayout.setRefreshing(isStart);
    }

    @Override
    public void setArticles(final List<IViewArticle> newArticles) {
        mListAdapter.setArticles(newArticles);
    }

    private void initListAdapter(final View fragmentView) {
        mListAdapter = new FeedListAdapter(getContext(), fragmentView.findViewById(R.id.feed_items_list));
        mListAdapter.setArticleClickListener((IOnArticleClickListener) getContext());
        mListAdapter.setImageRequestListener(mImageLoadPresenter);
    }

    private void initRefreshLayout(final View fragmentView) {
        mRefreshLayout = fragmentView.findViewById(R.id.feed_refresh_layout);
        mRefreshLayout.setOnRefreshListener(() -> mPresenter.onUpdate());
    }

    private void initRecyclerView(final View fragmentView) {
        final RecyclerView listView = fragmentView.findViewById(R.id.feed_items_list);
        listView.setAdapter(mListAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void insertImage(@NonNull byte[] imageBytes, @NonNull Bitmap bitmap) {
    }
}