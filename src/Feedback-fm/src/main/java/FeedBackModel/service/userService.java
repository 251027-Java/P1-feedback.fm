package FeedBackModel.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class userService {

    private final userRepository userRepo;

    @Autowired
    public userService(userRepository userRepo) {
        this.userRepo = userRepo;
    }

    // get user by internal db ID
    public User getUserById(Long ID) {
        return userRepo.findById(ID)
        .orElseThrow(() -> new RuntimeException("User not found" + ID));
    }

    // get user by Spotify ID
    public User getUserBySpotify(String sID) {
        return userRepo.findBySpotifyId(sID)
        .orElseThrow(() -> new RuntimeException("User not found" + sID));
    }

    // if we include email, we can add this method
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found" + email));
    }

    // create new user from spotify profile
    // should be called during OAuth authentication
    public User createUser(String sID, String username, String email, String pfpURL) {
        // check if user already exists
        Optional<User> existingUser = userRepo.findBySpotifyId(sID);
        if (existingUser.isPresent()) {
            return updateUser(existingUser.get(), sID, username, email, pfpURL);
        }

        // if user does not exist
        User u = new User(sID, username, email);
        u.setPFP(pfpURL);
        
        return userRepo.save(u);
    }

    public User updateUser(String sID, String username, String email, String pfpURL) {
        User u = getUserBySpotify(sID);
        u.setUsername(username);
        u.setEmail(email);
        u.setPFP(pfpURL);
        return userRepo.save(u);
    }
    
}
