package com.divercity.app.features.home.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.questions.QuestionResponse;
import com.divercity.app.data.entity.storiesfeatured.StoriesFeaturedResponse;
import com.divercity.app.features.home.home.feed.questions.QuestionsPaginatedRepositoryImpl;
import com.divercity.app.features.home.home.usecase.GetStoriesFeatured;
import com.divercity.app.repository.user.LoggedUserRepositoryImpl;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class HomeViewModel extends BaseViewModel {

    public MutableLiveData<Resource<List<StoriesFeaturedResponse>>> featuredList = new MutableLiveData<>();
    public LiveData<PagedList<QuestionResponse>> questionList;

    private Listing<QuestionResponse> questionListing;
    private QuestionsPaginatedRepositoryImpl questionsRepository;
    private GetStoriesFeatured getStoriesFeatured;
    private LoggedUserRepositoryImpl loggedUserRepository;

    @Inject
    public HomeViewModel(QuestionsPaginatedRepositoryImpl questionsRepository,
                         GetStoriesFeatured getStoriesFeatured,
                         LoggedUserRepositoryImpl loggedUserRepository) {
        this.questionsRepository = questionsRepository;
        questionListing = questionsRepository.fetchData();
        questionList = questionListing.getPagedList();
        this.getStoriesFeatured = getStoriesFeatured;
        this.loggedUserRepository = loggedUserRepository;
    }

    public void retry() {
        questionsRepository.retry();
    }

    public void refresh() {
        questionsRepository.refresh();
    }

    public LiveData<NetworkState> getNetworkState() {
        return questionListing.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return questionListing.getRefreshState();
    }

    public void getFeatured(){
        DisposableObserver<List<StoriesFeaturedResponse>> disposable = new DisposableObserver<List<StoriesFeaturedResponse>>() {
            @Override
            public void onNext(List<StoriesFeaturedResponse> storiesFeaturedResponses) {
                featuredList.postValue(Resource.Companion.success(storiesFeaturedResponses));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        getCompositeDisposable().add(disposable);
        getStoriesFeatured.execute(disposable, null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        questionsRepository.clear();
    }

    public void clearUserData(){
        loggedUserRepository.clearUserData();
    }
}
