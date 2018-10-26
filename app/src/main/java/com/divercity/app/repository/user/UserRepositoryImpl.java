package com.divercity.app.repository.user;

import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.profile.User;
import com.divercity.app.data.entity.profile.UserProfileBody;
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
                .map(loginResponseDataObject -> loginResponseDataObject.getData())
                .doOnNext(response -> {
                    if (response != null) {
                        loggedUserRepository.setAvatarUrl(response.getAttributes().getAvatarThumb());
                        loggedUserRepository.setUserId(response.getId());
                        loggedUserRepository.setAccountType(response.getAttributes().getAccountType());
                    }
                });
    }

    @Override
    public Observable<Boolean> joinGroup(String idGroup) {
        return userService.joinGroup(idGroup).map(Response::isSuccessful);
    }

    @Override
    public Observable<LoginResponse> updateUserProfile(User user) {
        UserProfileBody userProfileBody = new UserProfileBody();
        userProfileBody.setUser(user);
        return userService.updateUserProfile(loggedUserRepository.getUserId(), userProfileBody)
                .map(response -> {
                    if (response != null) {
                        loggedUserRepository.setAvatarUrl(response.getData().getAttributes().getAvatarThumb());
                        loggedUserRepository.setUserId(response.getData().getId());
                        loggedUserRepository.setAccountType(response.getData().getAttributes().getAccountType());
                    }
                    return response.getData();
                }
        );
    }

    @Override
    public void setAccessToken(@NotNull String token) {
        loggedUserRepository.setAccessToken(token);
    }

    @Nullable
    @Override
    public String getAccessToken() {
        return loggedUserRepository.getAccessToken();
    }

    @Override
    public void setClient(@NotNull String client) {
        loggedUserRepository.setClient(client);
    }

    @Nullable
    @Override
    public String getClient() {
        return loggedUserRepository.getClient();
    }

    @Override
    public void setUid(@NotNull String uid) {
        loggedUserRepository.setUid(uid);
    }

    @Nullable
    @Override
    public String getUid() {
        return loggedUserRepository.getUid();
    }

    @Override
    public void setAvatarUrl(@NotNull String url) {
        loggedUserRepository.setAvatarUrl(url);
    }

    @Nullable
    @Override
    public String getAvatarUrl() {
        return loggedUserRepository.getAvatarUrl();
    }

    @Override
    public void setUserId(@NotNull String userId) {
        loggedUserRepository.setUserId(userId);
    }

    @Nullable
    @Override
    public String getUserId() {
        return loggedUserRepository.getUserId();
    }

    @Override
    public void setAccountType(@NotNull String accountType) {
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
}
