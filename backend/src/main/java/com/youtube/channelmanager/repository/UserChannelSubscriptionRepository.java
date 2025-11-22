package com.youtube.channelmanager.repository;

import com.youtube.channelmanager.entity.UserChannelSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserChannelSubscriptionRepository extends JpaRepository<UserChannelSubscription, Long> {

    List<UserChannelSubscription> findByUserId(Long userId);

    List<UserChannelSubscription> findByUserIdAndCategoryId(Long userId, Long categoryId);

    Optional<UserChannelSubscription> findByUserIdAndChannelId(Long userId, Long channelId);

    void deleteByUserIdAndChannelId(Long userId, Long channelId);

    @Query("SELECT ucs FROM UserChannelSubscription ucs " +
           "JOIN FETCH ucs.channel " +
           "WHERE ucs.userId = :userId")
    List<UserChannelSubscription> findByUserIdWithChannel(@Param("userId") Long userId);

    @Query("SELECT ucs FROM UserChannelSubscription ucs " +
           "JOIN FETCH ucs.channel " +
           "LEFT JOIN FETCH ucs.category " +
           "WHERE ucs.userId = :userId")
    List<UserChannelSubscription> findByUserIdWithChannelAndCategory(@Param("userId") Long userId);
}
