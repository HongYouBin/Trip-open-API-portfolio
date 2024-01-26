package com.ssafy.i5i.hotelAPI.domain.hotel.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccommodation is a Querydsl query type for Accommodation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccommodation extends EntityPathBase<Accommodation> {

    private static final long serialVersionUID = 550517531L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccommodation accommodation = new QAccommodation("accommodation");

    public final StringPath accommodationAddr = createString("accommodationAddr");

    public final StringPath accommodationImg = createString("accommodationImg");

    public final NumberPath<java.math.BigDecimal> accommodationLatitude = createNumber("accommodationLatitude", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> accommodationLongitude = createNumber("accommodationLongitude", java.math.BigDecimal.class);

    public final StringPath accommodationName = createString("accommodationName");

    public final StringPath accommodationPrice = createString("accommodationPrice");

    public final NumberPath<Double> accommodationScore = createNumber("accommodationScore", Double.class);

    public final StringPath accommodationType = createString("accommodationType");

    public final QAttraction attraction;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAccommodation(String variable) {
        this(Accommodation.class, forVariable(variable), INITS);
    }

    public QAccommodation(Path<? extends Accommodation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccommodation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccommodation(PathMetadata metadata, PathInits inits) {
        this(Accommodation.class, metadata, inits);
    }

    public QAccommodation(Class<? extends Accommodation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attraction = inits.isInitialized("attraction") ? new QAttraction(forProperty("attraction")) : null;
    }

}

