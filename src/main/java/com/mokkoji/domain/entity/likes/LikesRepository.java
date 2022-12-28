package com.mokkoji.domain.entity.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<LikesEntity, Integer> {

    @Query( value = "select * from likes where bno=:bno and mno=:mno" , nativeQuery = true)
    Optional<LikesEntity> findByRecords(int bno , int mno );

    @Query( value = "select count(*) from likes where like_info=:likeinfo and bno=:bno" , nativeQuery = true )
    Optional<LikesEntity> fineByLikes( boolean likeinfo , int bno );

}
