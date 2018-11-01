package com.divercity.app.repository.user;

import com.divercity.app.data.entity.base.DataObject;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.profile.picture.ProfilePictureBody;
import com.divercity.app.data.entity.profile.profile.User;
import com.divercity.app.data.entity.profile.profile.UserProfileBody;
import com.divercity.app.data.networking.services.UserService;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by lucas on 18/10/2018.
 */

public class UserRepositoryImpl implements UserRepository {

    private UserService userService;
    private LoggedUserRepositoryImpl loggedUserRepository;

    @Inject
    public UserRepositoryImpl(UserService userService,
                              LoggedUserRepositoryImpl loggedUserRepository) {
        this.userService = userService;
        this.loggedUserRepository = loggedUserRepository;
    }

    @Override
    public Observable<LoginResponse> fetchRemoteUserData(String userId) {
        return userService.fetchUserData(userId)
                .map(loginResponseDataObject -> {
                    if (loginResponseDataObject != null)
                        loggedUserRepository.saveUserData(loginResponseDataObject);
                    return loginResponseDataObject.getData();
                });
    }

    @Override
    public Observable<Boolean> joinGroup(String idGroup) {
        return userService.joinGroup(idGroup).map(Response::isSuccessful);
    }

    @Override
    public Observable<LoginResponse> uploadUserPhoto(ProfilePictureBody body) {
        return userService.uploadUserPhoto(body).map(loginResponseDataObject -> {
                    if (loginResponseDataObject != null)
                        loggedUserRepository.saveUserData(loginResponseDataObject);
                    return loginResponseDataObject.getData();
                }
        );
    }

    @Override
    public Observable<LoginResponse> updateUserProfile(User user) {
        UserProfileBody userProfileBody = new UserProfileBody();
        userProfileBody.setUser(user);
        return userService.updateUserProfile(loggedUserRepository.getUserId(), userProfileBody)
                .map(response -> {
                            if (response != null) {
                                loggedUserRepository.saveUserData(response);
                            }
                            return response.getData();
                        }
                );
    }

    @Override
    public void setAccessToken(@Nullable String token) {
        loggedUserRepository.setAccessToken(token);
    }

    @Nullable
    @Override
    public String getAccessToken() {
        return loggedUserRepository.getAccessToken();
    }

    @Override
    public void setClient(@Nullable String client) {
        loggedUserRepository.setClient(client);
    }

    @Nullable
    @Override
    public String getClient() {
        return loggedUserRepository.getClient();
    }

    @Override
    public void setUid(@Nullable String uid) {
        loggedUserRepository.setUid(uid);
    }

    @Nullable
    @Override
    public String getUid() {
        return loggedUserRepository.getUid();
    }

    @Override
    public void setAvatarUrl(@Nullable String url) {
        loggedUserRepository.setAvatarUrl(url);
    }

    @Nullable
    @Override
    public String getAvatarUrl() {
        return loggedUserRepository.getAvatarUrl();
    }

    @Override
    public void setUserId(@Nullable String userId) {
        loggedUserRepository.setUserId(userId);
    }

    @Nullable
    @Override
    public String getUserId() {
        return loggedUserRepository.getUserId();
    }

    @Override
    public void setAccountType(@Nullable String accountType) {
        loggedUserRepository.setAccountType(accountType);
    }

    @Nullable
    @Override
    public String getAccountType() {
        return loggedUserRepository.getAccountType();
    }

    @Override
    public boolean isUserLogged() {
        return loggedUserRepository.isUserLogged();
    }

    @Override
    public void clearUserData() {
        loggedUserRepository.clearUserData();
    }

    @Override
    public void saveUserHeaderData(@NotNull Response<DataObject<LoginResponse>> response) {
        loggedUserRepository.saveUserHeaderData(response);
    }

    @Override
    public void saveUserData(@Nullable DataObject<LoginResponse> data) {
        loggedUserRepository.saveUserData(data);
    }
}
