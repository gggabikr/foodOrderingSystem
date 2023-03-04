package com.menuit.menuitreplica.service;

import com.menuit.menuitreplica.domain.Rating;
import com.menuit.menuitreplica.domain.Store;
import com.menuit.menuitreplica.domain.User;
import com.menuit.menuitreplica.repository.RatingRepository;
import com.menuit.menuitreplica.repository.StoreRepository;
import com.menuit.menuitreplica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public Long save(Long userId, Long storeId, int score, @Nullable String comment) throws Exception {
        User user = userRepository.findOne(userId);
        Store store = storeRepository.findOne(storeId);
        if(score>5 || score<1){
            throw new IllegalArgumentException("Score can only be 1~5");
        }
        if(ratingRepository.findByUserAndStore(user, store).size()>0){
            throw new IllegalArgumentException(
                    "The user left a rating for the store already. " +
                    "Please delete or deactivate the previous rating and try again.");
        }

        Rating rating;
        if(comment != null && comment.length()>0){
            rating = new Rating(user, store, score, comment);
        } else{
            rating = new Rating(user, store, score);
        }
        return ratingRepository.save(rating);
    }

    public Rating findById(Long ratingId){
        return ratingRepository.findById(ratingId);
    }

    public List<Rating> findByUser(Long userId){
        User user = userRepository.findOne(userId);
        return ratingRepository.findByUser(user);
    }

    public List<Rating> findByStore(Long storeId){
        Store store = storeRepository.findOne(storeId);
        return ratingRepository.findByStore(store);
    }

    public List<Rating> findByString(String str){
        return ratingRepository.findByString(str);
    }

    public List<Rating> findByScore(int score){
        return ratingRepository.findByScore(score);
    }

    public void toggleRating(Long ratingId){
        Rating rating = ratingRepository.findById(ratingId);
        rating.toggleStatus();
        //위에 토글 외에 다른 작업 필요없음
    }
}
