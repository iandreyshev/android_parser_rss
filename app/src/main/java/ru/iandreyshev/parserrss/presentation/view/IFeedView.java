package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.IViewRssArticle;

public interface IFeedView extends IBaseView {
    @StateStrategyType(AddToEndStrategy.class)
    void insertRss(final IViewRss rss);

    @StateStrategyType(AddToEndStrategy.class)
    void updateArticles(final IViewRss rss);

    @StateStrategyType(AddToEndStrategy.class)
    void removeRss(final IViewRss rss);

    @StateStrategyType(SkipStrategy.class)
    void openArticle(final IViewRssArticle article);

    @StateStrategyType(SkipStrategy.class)
    void openAddingRssDialog();

    @StateStrategyType(AddToEndStrategy.class)
    void startProgressBar(boolean isStart);

    @StateStrategyType(AddToEndStrategy.class)
    void startUpdate(final IViewRss rss, boolean isStart);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void openRssInfo(final IViewRss rss);
}
