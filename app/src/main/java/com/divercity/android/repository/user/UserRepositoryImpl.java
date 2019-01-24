package com.divercity.android.repository.user;

import com.divercity.android.data.entity.base.DataObject;
import com.divercity.android.data.entity.device.body.DeviceBody;
import com.divercity.android.data.entity.device.response.DeviceResponse;
import com.divercity.android.data.entity.industry.body.FollowIndustryBody;
import com.divercity.android.data.entity.interests.body.FollowInterestsBody;
import com.divercity.android.data.entity.occupationofinterests.body.FollowOOIBody;
import com.divercity.android.data.entity.profile.picture.ProfilePictureBody;
import com.divercity.android.data.entity.profile.profile.User;
import com.divercity.android.data.entity.profile.profile.UserProfileBody;
import com.divercity.android.data.entity.user.followuser.FollowUserResponse;
import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.data.networking.services.UserService;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.HttpException;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;

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

    private void checkResponse(Response response) {
        if (!response.isSuccessful())
            throw new HttpException(response);
    }

    @Override
    public Observable<UserResponse> fetchRemoteUserData(String userId) {
        return userService.fetchUserData(userId)
                .map(response -> {
                    checkResponse(response);
                    loggedUserRepository.saveUserData(response.body());
                    return response.body().getData();
                });
    }

    @Override
    public Observable<Boolean> joinGroup(String idGroup) {
        return userService.joinGroup(idGroup).map(response -> {
            checkResponse(response);
            return true;
        });
    }

    @Override
    public Observable<UserResponse> uploadUserPhoto(ProfilePictureBody body) {
        return userService.uploadUserPhoto(body).map(response -> {
                    checkResponse(response);
                    loggedUserRepository.saveUserData(response.body());
                    return response.body().getData();
                }
        );
    }

    @Override
    public Observable<UserResponse> updateUserProfile(User user) {
        UserProfileBody userProfileBody = new UserProfileBody();
        userProfileBody.setUser(user);
        return userService.updateUserProfile(loggedUserRepository.getUserId(), userProfileBody)
                .map(response -> {
                            checkResponse(response);
                            loggedUserRepository.saveUserData(response.body());
                            return response.body().getData();
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
    public void setAvatarThumbUrl(@Nullable String url) {
        loggedUserRepository.setAvatarThumbUrl(url);
    }

    @Nullable
    @Override
    public String getAvatarThumbUrl() {
        return loggedUserRepository.getAvatarThumbUrl();
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
    public void saveUserHeaderData(@NotNull Response<DataObject<UserResponse>> response) {
        loggedUserRepository.saveUserHeaderData(response);
    }

    @Override
    public void saveUserData(@Nullable DataObject<UserResponse> data) {
        loggedUserRepository.saveUserData(data);
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
    public void setFullName(@Nullable String userName) {
        loggedUserRepository.setFullName(userName);
    }

    @Nullable
    @Override
    public String getFullName() {
        return loggedUserRepository.getFullName();
    }

    @NotNull
    @Override
    public Observable<UserResponse> followOccupationOfInterests(@NotNull List<String> occupationIds) {
        return userService.followOccupationOfInterests(new FollowOOIBody(occupationIds)).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @Override
    public boolean isLoggedUserJobSeeker() {
        return loggedUserRepository.isLoggedUserJobSeeker();
    }

    @NotNull
    @Override
    public Observable<UserResponse> followInterests(@NotNull List<String> interestsIds) {
        return userService.followInterests(new FollowInterestsBody(interestsIds)).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @NotNull
    @Override
    public Observable<UserResponse> followIndustries(@NotNull List<String> industriesIds) {
        return userService.followIndustries(new FollowIndustryBody(industriesIds)).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @NotNull
    @Override
    public Observable<List<UserResponse>> fetchFollowersByUser(@NotNull String userId, int pageNumber, int size, @Nullable String query) {
        return userService.fetchFollowersByUser(userId, pageNumber, size, query).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @Nullable
    @Override
    public String getEthnicity() {
        return loggedUserRepository.getEthnicity();
    }

    @Override
    public void setEthnicity(@Nullable String ethnicity) {
        loggedUserRepository.setEthnicity(ethnicity);
    }

    @Nullable
    @Override
    public String getGender() {
        return loggedUserRepository.getGender();
    }

    @Override
    public void setGender(@Nullable String gender) {
        loggedUserRepository.setGender(gender);
    }

    @Nullable
    @Override
    public String getIndustry() {
        return loggedUserRepository.getIndustry();
    }

    @Override
    public void setIndustry(@Nullable String industry) {
        loggedUserRepository.setIndustry(industry);
    }

    @NotNull
    @Override
    public Observable<List<UserResponse>> fetchFollowingByUser(@NotNull String userId, int pageNumber, int size, @Nullable String query) {
        return userService.fetchFollowingByUser(userId, pageNumber, size, query).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @Nullable
    @Override
    public String getLocation() {
        return loggedUserRepository.getLocation();
    }

    @Override
    public void setLocation(@Nullable String location) {
        loggedUserRepository.setLocation(location);
    }

    @Nullable
    @Override
    public String getAgeRange() {
        return loggedUserRepository.getAgeRange();
    }

    @Override
    public void setAgeRange(@Nullable String ageRange) {
        loggedUserRepository.setAgeRange(ageRange);
    }

    @NotNull
    @Override
    public Observable<List<UserResponse>> fetchUsers(int pageNumber, int size, @Nullable String query) {
        return userService.fetchUsers(pageNumber, size, query).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @NotNull
    @Override
    public Observable<FollowUserResponse> followUser(@NotNull String userId) {
        RequestBody partUserId = RequestBody.create(MediaType.parse("text/plain"), userId);
        return userService.followUser(partUserId).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @NotNull
    @Override
    public Observable<Void> unfollowUser(@NotNull String userId) {
        RequestBody partUserId = RequestBody.create(MediaType.parse("text/plain"), userId);
        return userService.unfollowUser(partUserId).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @NotNull
    @Override
    public Observable<DeviceResponse> saveDevice(@NotNull DeviceBody body) {
        return userService.saveDevice(body).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @NotNull
    @Override
    public Observable<Void> updateDevice(@NotNull String deviceId, @NotNull DeviceBody body) {
        return userService.updateDevice(deviceId, body).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Nullable
    @Override
    public String getDeviceId() {
        return loggedUserRepository.getDeviceId();
    }

    @Override
    public void setDeviceId(@Nullable String deviceId) {
        loggedUserRepository.setDeviceId(deviceId);
    }

    @Nullable
    @Override
    public String getFCMToken() {
        return loggedUserRepository.getFCMToken();
    }

    @Override
    public void setFCMToken(@Nullable String token) {
        loggedUserRepository.setFCMToken(token);
    }
}
