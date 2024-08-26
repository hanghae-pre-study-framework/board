package com.hanghae.board.utill;

import static org.jeasy.random.FieldPredicates.inClass;
import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.UpdatePostCommand;
import com.hanghae.board.domain.post.entity.Post;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class PostTestFixture {

  public static EasyRandom get(LocalDate firstDate, LocalDate lastDate) {
    var idPredicate = named("id")
        .and(ofType(Long.class))
        .and(inClass(Post.class));

    var param = new EasyRandomParameters()
        .excludeField(idPredicate)
        .dateRange(firstDate, lastDate)
        .randomize(LocalDateTime.class,
            () -> LocalDateTime.now().minusSeconds((long) (Math.random() * 31536000)))
        .stringLengthRange(5, 50)
        .collectionSizeRange(0, 5)
        .randomize(named("title").and(ofType(String.class)),
            () -> "Title: " + java.util.UUID.randomUUID().toString().substring(0, 8))
        .randomize(named("password").and(ofType(String.class)),
            () -> java.util.UUID.randomUUID().toString().substring(0, 12));

    return new EasyRandom(param);
  }

  public static PostCommand nextPostCommand(EasyRandom easyRandom) {
    return new PostCommand(
        easyRandom.nextObject(String.class),
        easyRandom.nextObject(String.class),
        easyRandom.nextObject(String.class),
        easyRandom.nextObject(String.class)
    );
  }

  public static UpdatePostCommand nextUpdatePostCommand(EasyRandom easyRandom) {
    return new UpdatePostCommand(
        easyRandom.nextObject(String.class),
        easyRandom.nextObject(String.class),
        easyRandom.nextObject(String.class),
        easyRandom.nextObject(String.class)
    );
  }
}