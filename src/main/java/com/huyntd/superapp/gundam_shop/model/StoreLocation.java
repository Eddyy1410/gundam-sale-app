package com.huyntd.superapp.gundam_shop.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "[store_location]")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreLocation {

    @Id
    @GeneratedValue
    @Column(name = "id")
    int id;

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
    BigDecimal longitude;

    @Column(name = "address", nullable = false, length = 200)
    String address;

}
