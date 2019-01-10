package com.divercity.app.features.home.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.questions.QuestionResponse
import com.divercity.app.data.entity.storiesfeatured.StoriesFeaturedResponse
import com.divercity.app.features.home.home.feed.questions.QuestionsPaginatedRepositoryImpl
import com.divercity.app.features.home.home.usecase.GetStoriesFeatured
import com.divercity.app.repository.chat.ChatRepositoryImpl
import com.divercity.app.repository.user.LoggedUserRepositoryImpl
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject
constructor(
    private val questionsRepository: QuestionsPaginatedRepositoryImpl,
    private val getStoriesFeatured: GetStoriesFeatured,
    private val loggedUserRepository: LoggedUserRepositoryImpl,
    private val chatRepositoryImpl: ChatRepositoryImpl
) : BaseViewModel() {

    var featuredList = MutableLiveData<Resource<List<StoriesFeaturedResponse>>>()
    var questionList: LiveData<PagedList<QuestionResponse>>

    private var questionListing: Listing<QuestionResponse> = questionsRepository.fetchData()

    val networkState: LiveData<NetworkState> = questionListing.networkState

    val refreshState: LiveData<NetworkState> = questionListing.refreshState

    init {
        questionList = questionListing.pagedList
    }

    fun retry() {
        questionsRepository.retry()
    }

    fun refresh() {
        questionsRepository.refresh()
    }

    fun getFeatured() {
        val disposable = object : DisposableObserver<List<StoriesFeaturedResponse>>() {
            override fun onNext(storiesFeaturedResponses: List<StoriesFeaturedResponse>) {
                featuredList.postValue(Resource.success(storiesFeaturedResponses))
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
        compositeDisposable.add(disposable)
        getStoriesFeatured.execute(disposable, null)
    }

    override fun onCleared() {
        super.onCleared()
        questionsRepository.clear()
    }

    fun clearUserData() {
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            chatRepositoryImpl.deleteChatDB()
            chatRepositoryImpl.deleteChatMessagesDB()
        }
        loggedUserRepository.clearUserData()
    }
}
