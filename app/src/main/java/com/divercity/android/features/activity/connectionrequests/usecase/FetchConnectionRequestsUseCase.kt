package com.divercity.android.features.activity.connectionrequests.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.repository.group.GroupRepository
import com.divercity.android.repository.session.SessionRepository
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Function3
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchConnectionRequestsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: GroupRepository,
            private val userRepository: UserRepository,
            private val sessionRepository: SessionRepository
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

        val fetchConnectionRequests = userRepository.fetchConnectionRequests(
            sessionRepository.getUserId(),
            params.page,
            params.size,
            null
        )

        return Observable.zip(
            fetchGroupInvitations,
            fetchGroupJoinRequests,
            fetchConnectionRequests,
            Function3 {groupInvitations, groupJoinRequests, connectionRequests ->

                val shuffledGroupInvitationsAndJoinRequests = ArrayList<ConnectionItem>()
                var startLimitGroupInvitations = 0
                var startLimitJoinRequests = 0
                var startLimitConnectionRequest = 0

                while(groupInvitations.isNotEmpty() && startLimitGroupInvitations < groupInvitations.size ||
                    groupJoinRequests.isNotEmpty() && startLimitJoinRequests < groupJoinRequests.size ||
                    connectionRequests.isNotEmpty() && startLimitConnectionRequest < connectionRequests.size){

                    if(startLimitConnectionRequest < connectionRequests.size){
                        val randConnections = (1..2).random()
                        var endLimitConnectionRequests = startLimitConnectionRequest + randConnections
                        if(endLimitConnectionRequests > connectionRequests.size) endLimitConnectionRequests = connectionRequests.size
                        for(i in startLimitConnectionRequest..(endLimitConnectionRequests - 1))
                            shuffledGroupInvitationsAndJoinRequests.add(connectionRequests[i])
                        startLimitConnectionRequest += randConnections
                    }

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

                return@Function3 shuffledGroupInvitationsAndJoinRequests
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
