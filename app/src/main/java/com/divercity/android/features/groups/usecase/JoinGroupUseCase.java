package com.divercity.android.features.groups.usecase;

import com.divercity.android.core.base.UseCase;
import com.divercity.android.data.entity.group.GroupResponse;
import com.divercity.android.repository.user.UserRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class JoinGroupUseCase extends UseCase<Boolean, JoinGroupUseCase.Params> {

    private UserRepository userRepository;

    @Inject
    public JoinGroupUseCase(@Named("executor_thread") Scheduler executorThread,
                            @Named("ui_thread") Scheduler uiThread,
                            UserRepository userRepository) {
        super(executorThread, uiThread);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<Boolean> createObservableUseCase(JoinGroupUseCase.Params params) {
        return userRepository.joinGroup(params.group.getId());
    }

    public static final class Params {

        private final GroupResponse group;

        private Params(GroupResponse group) {
            this.group = group;
        }

        public static JoinGroupUseCase.Params forJoin(GroupResponse group) {
            return new JoinGroupUseCase.Params(group);
        }
    }
}
