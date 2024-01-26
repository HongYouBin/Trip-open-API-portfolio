package com.ssafy.i5i.hotelAPI.domain.food.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFood is a Querydsl query type for Food
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFood extends EntityPathBase<Food> {

    private static final long serialVersionUID = -866241453L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFood food = new QFood("food");

    public final com.ssafy.i5i.hotelAPI.domain.hotel.entity.QAttraction attraction;

    public final NumberPath<Integer> foodJjim = createNumber("foodJjim", Integer.class);

    public final NumberPath<java.math.BigDecimal> foodLatitude = createNumber("foodLatitude", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> foodLongtitude = createNumber("foodLongtitude", java.math.BigDecimal.class);

    public final StringPath foodName = createString("foodName");

    public final NumberPath<Integer> foodScore = createNumber("foodScore", Integer.class);

    public final NumberPath<Double> foodStar = createNumber("foodStar", Double.class);

    public final NumberPath<Integer> foodStarUser = createNumber("foodStarUser", Integer.class);

    public final StringPath foodType = createString("foodType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QFood(String variable) {
        this(Food.class, forVariable(variable), INITS);
    }

    public QFood(Path<? extends Food> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFood(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFood(PathMetadata metadata, PathInits inits) {
        this(Food.class, metadata, inits);
    }

    public QFood(Class<? extends Food> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attraction = inits.isInitialized("attraction") ? new com.ssafy.i5i.hotelAPI.domain.hotel.entity.QAttraction(forProperty("attraction")) : null;
    }

}

