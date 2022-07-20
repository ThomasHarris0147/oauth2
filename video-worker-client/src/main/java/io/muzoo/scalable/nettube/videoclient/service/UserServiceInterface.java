package io.muzoo.scalable.nettube.videoclient.service;

import io.muzoo.scalable.nettube.videoclient.entity.User;
import io.muzoo.scalable.nettube.videoclient.entity.VerificationToken;
import io.muzoo.scalable.nettube.videoclient.model.UserModel;

import java.util.Optional;


public interface UserServiceInterface {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(User user, String oldPassword);
}