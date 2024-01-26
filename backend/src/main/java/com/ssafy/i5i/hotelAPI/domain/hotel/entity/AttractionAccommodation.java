package com.ssafy.i5i.hotelAPI.domain.hotel.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(name = "attraction_accommodation")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttractionAccommodation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attraction_accommodation_id")
    private Long attractionAccommodationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "attraction_id", referencedColumnName = "content_id")
    private Attraction attraction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="accommodation_id", referencedColumnName = "accommodation_id")
    private Accommodation accommodation;
}
