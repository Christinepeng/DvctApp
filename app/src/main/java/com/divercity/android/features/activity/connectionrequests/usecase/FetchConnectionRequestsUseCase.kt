package com.divercity.android.features.activity.connectionrequests.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchConnectionRequestsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: GroupRepository
) : UseCase<@JvmSuppressWildcards List<ConnectionItem>, FetchConnectionRequestsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<ConnectionItem>> {
        val fetchGroupInvitations = repository.fetchGroupInvitations(
            params.page,
            params.size
        )

        val fetchGroupJoinRequests = repository.fetchGroupJoinRequests(
            params.page,
            params.size
        )

        return Observable.zip(
            fetchGroupInvitations,
            fetchGroupJoinRequests,
            BiFunction {groupInvitations, groupJoinRequests ->

                val shuffledGroupInvitationsAndJoinRequests = ArrayList<ConnectionItem>()
                var startLimitGroupInvitations = 0
                var startLimitJoinRequests = 0

                while(groupInvitations.isNotEmpty() && startLimitGroupInvitations < groupInvitations.size ||
                    groupJoinRequests.isNotEmpty() && startLimitJoinRequests < groupJoinRequests.size){

                    if(startLimitGroupInvitations < groupInvitations.size){
                        val randJobs = (1..2).random()
                        var endLimitGroupInvitations = startLimitGroupInvitations + randJobs
                        if(endLimitGroupInvitations > groupInvitations.size) endLimitGroupInvitations = groupInvitations.size
                        for(i in startLimitGroupInvitations..(endLimitGroupInvitations - 1))
                            shuffledGroupInvitationsAndJoinRequests.add(groupInvitations[i])
                        startLimitGroupInvitations += randJobs
                    }

                    if(startLimitJoinRequests < groupJoinRequests.size){
                        val randQuestions = (1..2).random()
                        var endLimitJoinRequests = randQuestions + startLimitJoinRequests
                        if(endLimitJoinRequests > groupJoinRequests.size) endLimitJoinRequests = groupJoinRequests.size
                        for(i in startLimitJoinRequests..(endLimitJoinRequests - 1))
                            shuffledGroupInvitationsAndJoinRequests.add(groupJoinRequests[i])
                        startLimitJoinRequests += randQuestions
                    }
                }

                return@BiFunction shuffledGroupInvitationsAndJoinRequests
            }
        )
    }

    class Params private constructor(val page: Int, val size: Int) {

        companion object {

            fun forConnRequest(page: Int, size: Int): Params {
                return Params(page, size)
            }
        }
    }
}
