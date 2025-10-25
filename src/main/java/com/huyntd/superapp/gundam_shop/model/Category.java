package com.huyntd.superapp.gundam_shop.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "[category]")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    // Không dùng cascade = CascadeType.ALL
    // Vì khi DELETE 1 category thì sẽ không tự động xóa các product của Category đó
    // Ở phía product thì có category_id set là nullable = false
    // 2 cái này conflict nhau và sinh ra exception
    // throw new BusinessLogicException("Cannot delete category. There are products associated with it.");
    List<Product> products;

}
