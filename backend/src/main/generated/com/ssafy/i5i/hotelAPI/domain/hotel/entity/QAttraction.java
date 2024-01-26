package com.ssafy.i5i.hotelAPI.domain.hotel.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAttraction is a Querydsl query type for Attraction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttraction extends EntityPathBase<Attraction> {

    private static final long serialVersionUID = 1265156414L;

    public static final QAttraction attraction = new QAttraction("attraction");

    public final StringPath addr1 = createString("addr1");

    public final StringPath addr2 = createString("addr2");

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final NumberPath<Integer> contentTypeId = createNumber("contentTypeId", Integer.class);

    public final StringPath firstImage = createString("firstImage");

    public final StringPath firstImage2 = createString("firstImage2");

    public final NumberPath<Integer> gugunCode = createNumber("gugunCode", Integer.class);

    public final NumberPath<java.math.BigDecimal> latitude = createNumber("latitude", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> longitude = createNumber("longitude", java.math.BigDecimal.class);

    public final StringPath mlevel = createString("mlevel");

    public final NumberPath<Integer> readCount = createNumber("readCount", Integer.class);

    public final NumberPath<Integer> sidoCode = createNumber("sidoCode", Integer.class);

    public final StringPath tel = createString("tel");

    public final StringPath title = createString("title");

    public final StringPath zipcode = createString("zipcode");

    public QAttraction(String variable) {
        super(Attraction.class, forVariable(variable));
    }

    public QAttraction(Path<? extends Attraction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAttraction(PathMetadata metadata) {
        super(Attraction.class, metadata);
    }

}

