package com.divercity.app.repository.user;

import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.profile.User;

import io.reactivex.Observable;

/**
 * Created by lucas on 18/10/2018.
 */

public interface UserRepository extends LoggedUserRepository{

    Observable<LoginResponse> fetchRemoteUserData(String userId);

    Observable<LoginResponse> updateUserProfile(User user);

    Observable<Boolean> joinGroup(String idGroup);
}
