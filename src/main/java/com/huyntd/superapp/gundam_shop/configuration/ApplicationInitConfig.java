package com.huyntd.superapp.gundam_shop.configuration;

import com.huyntd.superapp.gundam_shop.model.Category;
import com.huyntd.superapp.gundam_shop.model.Product;
import com.huyntd.superapp.gundam_shop.model.ProductImage;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import com.huyntd.superapp.gundam_shop.repository.CategoryRepository;
import com.huyntd.superapp.gundam_shop.repository.ProductImageRepository;
import com.huyntd.superapp.gundam_shop.repository.ProductRepository;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    UserRepository userRepository;

    ProductRepository productRepository;

    ProductImageRepository productImageRepository;

    CategoryRepository categoryRepository;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    ApplicationRunner applicationRunner() {
        // Tiêm passwordEncoder() phía trên vào applicationRunner
        PasswordEncoder passwordEncoder = passwordEncoder();

        return args -> {
            if(userRepository.findByEmail("admin@huyntd.com").isEmpty()) {
                userRepository.save(User.builder()
                        .email("admin@huyntd.com")
                        .role(UserRole.ADMIN)
                        .passwordHash(passwordEncoder.encode("admin"))
                        .build()
                );
                log.warn("admin@huyntd.com has been created with default password: admin, please change it!");
            }
            if(categoryRepository.findAll().isEmpty()) {
                categoryRepository.saveAll(List.of(
                        Category.builder().name("SD - Super Deformed").build(),
                        Category.builder().name("HG - High Grade 1/144").build(),
                        Category.builder().name("RG - Real Grade 1/144").build(),
                        Category.builder().name("Entry Grade").build(),
                        Category.builder().name("MG - Master Grade 1/100").build(),
                        Category.builder().name("Full Mechanics - RE/100").build(),
                        Category.builder().name("P-Bandai").build(),
                        Category.builder().name("PG - Perfect Grade 1/60").build(),
                        Category.builder().name("Megasize 1/48").build(),
                        Category.builder().name("Haropla").build(),
                        Category.builder().name("HiRm - Hi Resolution Model").build(),
                        Category.builder().name("30MM - 30MS - 30MF").build(),
                        Category.builder().name("Weapon + Support Unit").build()
                ));
            }

            if (productRepository.findAll().isEmpty()) {
                log.warn(productRepository.findAll().toString());
                productRepository.saveAll(List.of(
                        Product.builder()
                                .name("SDCS RX-78-2 Gundam")
                                .briefDescription("Mẫu Gundam Chibi đáng yêu, dễ lắp ráp.")
                                .fullDescription("Mẫu SDCS RX-78-2 Gundam với khung xương Cross Silhouette, cho phép tùy chỉnh giữa 2 dạng: SD và cao hơn. Đi kèm beam rifle, shield và beam saber.")
                                .technicalSpecification("Tỷ lệ: Non-scale\nChiều cao: ~10cm\nPhụ kiện: Súng, Khiên, Kiếm Beam.")
                                .price(BigDecimal.valueOf(250000))
                                .quantity(85)
                                .category(categoryRepository.findById(1).orElse(null))
                                .build(),

                        Product.builder()
                                .name("SD EX-Standard Sazabi")
                                .briefDescription("Mobile Suit Sazabi phiên bản SD cực ngầu.")
                                .fullDescription("Sazabi từ 'Char's Counterattack' được tái hiện dưới dạng SD với nhiều chi tiết và vũ khí đặc trưng như beam shot rifle, tomahawk và funnels.")
                                .technicalSpecification("Tỷ lệ: Non-scale\nChiều cao: ~9cm\nPhụ kiện: Súng, Rìu Beam, Phễu.")
                                .price(BigDecimal.valueOf(200000))
                                .quantity(110)
                                .category(categoryRepository.findById(1).orElse(null))
                                .build(),

                        Product.builder()
                                .name("HG 1/144 Gundam Aerial")
                                .briefDescription("Gundam Aerial từ The Witch from Mercury.")
                                .fullDescription("HG 1/144 Gundam Aerial là mẫu mobile suit chủ đạo trong series 'Gundam: The Witch from Mercury'. Mô hình có độ chi tiết cao, biên độ cử động linh hoạt.")
                                .technicalSpecification("Tỷ lệ: 1/144\nChiều cao: ~13cm\nPhụ kiện: Beam Rifle, Escutcheon (Khiên).")
                                .price(BigDecimal.valueOf(450000))
                                .quantity(150)
                                .category(categoryRepository.findById(2).orElse(null))
                                .build(),

                        Product.builder()
                                .name("HGUC 1/144 Char's Zaku II")
                                .briefDescription("Mẫu Zaku II kinh điển của Zeon.")
                                .fullDescription("Tái hiện lại mẫu Zaku II của Char Aznable với màu đỏ đặc trưng. Đây là một trong những kit HG bán chạy nhất mọi thời đại.")
                                .technicalSpecification("Tỷ lệ: 1/144\nChiều cao: ~12.5cm\nPhụ kiện: Zaku Machine Gun, Heat Hawk.")
                                .price(BigDecimal.valueOf(350000))
                                .quantity(200)
                                .category(categoryRepository.findById(2).orElse(null))
                                .build(),

                        Product.builder()
                                .name("HG 1/144 Gundam Barbatos Lupus Rex")
                                .briefDescription("Gundam Barbatos Lupus Rex đầy uy lực.")
                                .fullDescription("Phiên bản cuối cùng của Gundam Barbatos từ 'Iron-Blooded Orphans'. Kit nổi bật với vũ khí Ultra Large Mace khổng lồ và cánh tay được kéo dài.")
                                .technicalSpecification("Tỷ lệ: 1/144\nChiều cao: ~13cm\nPhụ kiện: Chùy siêu lớn, Móng vuốt.")
                                .price(BigDecimal.valueOf(480000))
                                .quantity(90)
                                .category(categoryRepository.findById(2).orElse(null))
                                .build(),

                        Product.builder()
                                .name("RG 1/144 RX-93 Nu Gundam")
                                .briefDescription("Nu Gundam với độ chi tiết đáng kinh ngạc.")
                                .fullDescription("RG Nu Gundam được xem là một trong những kit Real Grade tốt nhất. Tái hiện lại mobile suit của Amuro Ray với khung xương chi tiết và bộ Fin Funnels.")
                                .technicalSpecification("Tỷ lệ: 1/144\nChiều cao: ~15cm\nPhụ kiện: Fin Funnels, Beam Rifle, Khiên.")
                                .price(BigDecimal.valueOf(1150000))
                                .quantity(60)
                                .category(categoryRepository.findById(3).orElse(null))
                                .build(),

                        Product.builder()
                                .name("RG 1/144 God Gundam")
                                .briefDescription("God Gundam với khả năng tạo dáng võ thuật.")
                                .fullDescription("RG God Gundam từ series 'G Gundam' có biên độ cử động cực kỳ linh hoạt, cho phép tái tạo các tư thế võ thuật phức tạp và đòn thế Burning Finger.")
                                .technicalSpecification("Tỷ lệ: 1/144\nChiều cao: ~14cm\nPhụ kiện: Các bàn tay thay thế, God Finger.")
                                .price(BigDecimal.valueOf(950000))
                                .quantity(70)
                                .category(categoryRepository.findById(3).orElse(null))
                                .build(),

                        Product.builder()
                                .name("Entry Grade 1/144 RX-78-2 Gundam")
                                .briefDescription("Hoàn hảo cho người mới bắt đầu.")
                                .fullDescription("Entry Grade RX-78-2 Gundam được thiết kế để lắp ráp dễ dàng mà không cần kìm, keo hay sơn. Màu sắc và chi tiết vẫn rất ấn tượng.")
                                .technicalSpecification("Tỷ lệ: 1/144\nChiều cao: ~13cm\nPhụ kiện: Beam Rifle, Khiên.")
                                .price(BigDecimal.valueOf(200000))
                                .quantity(300)
                                .category(categoryRepository.findById(4).orElse(null))
                                .build(),

                        Product.builder()
                                .name("Entry Grade 1/144 Strike Gundam")
                                .briefDescription("Strike Gundam dễ lắp, giá phải chăng.")
                                .fullDescription("Mẫu Entry Grade Strike Gundam tái hiện mobile suit nổi tiếng từ Gundam SEED. Dễ lắp nhưng vẫn có biên độ cử động tốt.")
                                .technicalSpecification("Tỷ lệ: 1/144\nChiều cao: ~13cm\nPhụ kiện: Beam Rifle, Khiên.")
                                .price(BigDecimal.valueOf(220000))
                                .quantity(250)
                                .category(categoryRepository.findById(4).orElse(null))
                                .build(),

                        Product.builder()
                                .name("MG 1/100 Freedom Gundam Ver. 2.0")
                                .briefDescription("Kiệt tác kỹ thuật của dòng Master Grade.")
                                .fullDescription("Freedom Gundam 2.0 là phiên bản nâng cấp toàn diện, với khung xương mới, tỷ lệ đẹp hơn và các khớp cử động linh hoạt, cho phép tạo dáng dang cánh cực đẹp.")
                                .technicalSpecification("Tỷ lệ: 1/100\nChiều cao: ~18cm\nPhụ kiện: Lupus Beam Rifle, Cánh plasma cannon.")
                                .price(BigDecimal.valueOf(1200000))
                                .quantity(65)
                                .category(categoryRepository.findById(5).orElse(null))
                                .build(),

                        Product.builder()
                                .name("MG 1/100 Sinanju Stein (Narrative Ver.)")
                                .briefDescription("Gundam 'khủng long' từ series Unicorn.")
                                .fullDescription("MG Sinanju Stein (Narrative Ver.) nổi bật với các hoa văn chạm khắc tinh xảo trên tay và ngực. Đi kèm vũ khí Beam Rifle và Bazooka có thể kết hợp.")
                                .technicalSpecification("Tỷ lệ: 1/100\nChiều cao: ~20cm\nPhụ kiện: High Beam Rifle, Bazooka, Khiên.")
                                .price(BigDecimal.valueOf(2100000))
                                .quantity(40)
                                .category(categoryRepository.findById(5).orElse(null))
                                .build(),

                        Product.builder()
                                .name("Full Mechanics 1/100 Calamity Gundam")
                                .briefDescription("Calamity Gundam với chi tiết ấn tượng.")
                                .fullDescription("Dòng Full Mechanics tập trung vào độ chi tiết bên ngoài. Calamity Gundam có bộ vũ khí 'hầm hố' gồm pháo plasma và pháo năng lượng trên vai.")
                                .technicalSpecification("Tỷ lệ: 1/100\nChiều cao: ~18cm\nPhụ kiện: \"Todesblock\" plasma-sabot cannon, \"Schlag\" beam cannon.")
                                .price(BigDecimal.valueOf(1350000))
                                .quantity(45)
                                .category(categoryRepository.findById(6).orElse(null))
                                .build(),

                        Product.builder()
                                .name("P-Bandai MG 1/100 Astray Red Frame Flight Unit")
                                .briefDescription("Phiên bản giới hạn của Astray Red Frame.")
                                .fullDescription("P-Bandai là dòng sản phẩm sản xuất giới hạn. Kit này là Astray Red Frame với bộ trang bị Flight Unit và thanh kiếm Gerbera Straight được mạ chrome.")
                                .technicalSpecification("Tỷ lệ: 1/100\nChiều cao: ~18cm\nPhụ kiện: Gerbera Straight (mạ), Flight Unit.")
                                .price(BigDecimal.valueOf(2400000))
                                .quantity(25)
                                .category(categoryRepository.findById(7).orElse(null))
                                .build(),

                        Product.builder()
                                .name("P-Bandai RG 1/144 Tallgeese III")
                                .briefDescription("Tallgeese III phiên bản đặc biệt.")
                                .fullDescription("Mẫu Tallgeese III từ Gundam Wing: Endless Waltz với màu sắc và decal nước độc quyền của P-Bandai.")
                                .technicalSpecification("Tỷ lệ: 1/144\nChiều cao: ~14cm\nPhụ kiện: Mega Cannon, Heat Rod, Beam Saber.")
                                .price(BigDecimal.valueOf(1300000))
                                .quantity(30)
                                .category(categoryRepository.findById(7).orElse(null))
                                .build(),

                        Product.builder()
                                .name("PG Unleashed 1/60 RX-78-2 Gundam")
                                .briefDescription("Đỉnh cao của mô hình Gunpla.")
                                .fullDescription("Perfect Grade Unleashed RX-78-2 Gundam là một cuộc cách mạng về kỹ thuật mô hình, với 5 giai đoạn lắp ráp từ khung xương đến giáp ngoài, tích hợp đèn LED.")
                                .technicalSpecification("Tỷ lệ: 1/60\nChiều cao: ~30cm\nPhụ kiện: Hệ thống LED, Beam Rifle, Khiên, Core Fighter.")
                                .price(BigDecimal.valueOf(7500000))
                                .quantity(15)
                                .category(categoryRepository.findById(8).orElse(null))
                                .build(),

                        Product.builder()
                                .name("Mega Size 1/48 Unicorn Gundam (Destroy Mode)")
                                .briefDescription("Gundam Unicorn khổng lồ.")
                                .fullDescription("Mẫu Mega Size có kích thước ấn tượng, cao đến 45cm. Dù lớn, việc lắp ráp khá đơn giản, phù hợp để trưng bày.")
                                .technicalSpecification("Tỷ lệ: 1/48\nChiều cao: ~45.2cm\nPhụ kiện: Beam Magnum, Khiên.")
                                .price(BigDecimal.valueOf(2800000))
                                .quantity(20)
                                .category(categoryRepository.findById(9).orElse(null))
                                .build(),

                        Product.builder()
                                .name("Haropla Haro Basic Green")
                                .briefDescription("Robot hình cầu đáng yêu.")
                                .fullDescription("Haropla là dòng mô hình lắp ráp đơn giản của chú robot Haro. Mẫu Basic Green là màu sắc kinh điển nhất.")
                                .technicalSpecification("Tỷ lệ: Non-scale\nChiều cao: ~5cm\nPhụ kiện: Chân đế trưng bày.")
                                .price(BigDecimal.valueOf(150000))
                                .quantity(150)
                                .category(categoryRepository.findById(10).orElse(null))
                                .build(),

                        Product.builder()
                                .name("HiRM 1/100 Wing Gundam Zero EW")
                                .briefDescription("Wing Gundam Zero với chi tiết kim loại.")
                                .fullDescription("Dòng HiRM kết hợp giữa nhựa và kim loại đúc sẵn (die-cast) ở phần khung xương, tạo cảm giác nặng và chắc chắn. Wing Zero EW có đôi cánh thiên thần cực đẹp.")
                                .technicalSpecification("Tỷ lệ: 1/100\nChiều cao: ~18cm\nPhụ kiện: Khung xương kim loại, Twin Buster Rifle.")
                                .price(BigDecimal.valueOf(3800000))
                                .quantity(18)
                                .category(categoryRepository.findById(11).orElse(null))
                                .build(),

                        Product.builder()
                                .name("30MM 1/144 eEXM-17 Alto (White)")
                                .briefDescription("Robot tùy biến theo phong cách quân sự.")
                                .fullDescription("30 Minutes Missions (30MM) là dòng mô hình cho phép tùy biến và kết hợp không giới hạn. Alto là một trong những mẫu cơ bản với nhiều lỗ cắm trang bị.")
                                .technicalSpecification("Tỷ lệ: 1/144\nChiều cao: ~13cm\nPhụ kiện: Submachine gun, Knuckle guard.")
                                .price(BigDecimal.valueOf(380000))
                                .quantity(95)
                                .category(categoryRepository.findById(12).orElse(null))
                                .build(),

                        Product.builder()
                                .name("Builders Parts HD MS Weapon 001")
                                .briefDescription("Bộ vũ khí nâng cấp cho Gunpla.")
                                .fullDescription("Bộ vũ khí đa năng có thể sử dụng cho nhiều mẫu HG 1/144 khác nhau, bao gồm nhiều loại súng trường, tên lửa và lưỡi kiếm.")
                                .technicalSpecification("Tỷ lệ: 1/144\nPhụ kiện: Zaku Machine Gun, Fedayeen Rifle, G-Bouncer Rifle.")
                                .price(BigDecimal.valueOf(250000))
                                .quantity(120)
                                .category(categoryRepository.findById(13).orElse(null))
                                .build()
                ));
            }

            if (productImageRepository.findAll().isEmpty()) {
                productImageRepository.saveAll(List.of(
                        ProductImage.builder().imageUrl("https://img.lazcdn.com/g/p/d002e09a71901375e5d078d68c7f92db.jpg_720x720q80.jpg").product(productRepository.findById(1).get()).build(),
                        ProductImage.builder().imageUrl("http://www.xmodeltoys.com/photo/1595-4.jpg").product(productRepository.findById(1).get()).build(),
                        ProductImage.builder().imageUrl("https://cdn.media.amplience.net/s/hottopic/14248475_hi?$productMainDesktop$&fmt=auto").product(productRepository.findById(2).get()).build(),
                        ProductImage.builder().imageUrl("https://www.kikatek.com/MMRes/ResourcesMultimediaProducts/Piper-G600/1154/K1414585_m115154.jpg").product(productRepository.findById(2).get()).build(),
                        ProductImage.builder().imageUrl("https://bandai-hobby.net/images/193_5238_s_x79to5rahv280kcd71nm2e660avx.jpg").product(productRepository.findById(3).get()).build(),
                        ProductImage.builder().imageUrl("https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEgzU6SIn9TQXR4FhyKhtpFqjBp6AIwGTahfINJPBcfW8ckn-ggwBud4AQzZNSzDZTEqVx_2rqcvvWDUFsY5yEbEb58eT6WNmpUePT27W-tvokWSJrNeevt6PcE-jPMeVk2jtwDXCCxIDQKaocj8ylRSi-hW_aoavM5WXbkUIPFuASFQW8uXzZQrW6k/s1500/hg%20gundam%20aerial%20rebuild%20(1).jpg").product(productRepository.findById(3).get()).build(),
                        ProductImage.builder().imageUrl("https://www.japanzon.com/78305-product_hd/bandai-mobile-suit-gundam-high-grade-hguc-char-s-zaku-ii-model-kit-figure.jpg").product(productRepository.findById(4).get()).build(),
                        ProductImage.builder().imageUrl("https://www.hlj.com/productimages/ban/bans60453_14.jpg").product(productRepository.findById(4).get()).build(),
                        ProductImage.builder().imageUrl("https://product.hstatic.net/200000326537/product/hgibo21-gundam_barbatos_lupus-boxart_9507da59285f4b94818fb850ef499c13_master.jpg").product(productRepository.findById(5).get()).build(),
                        ProductImage.builder().imageUrl("http://www.gundam.co.nz/wp-content/uploads/2016/09/Untitled-88.png").product(productRepository.findById(5).get()).build(),
                        ProductImage.builder().imageUrl("https://down-th.img.susercontent.com/file/th-11134207-7rasf-m101aeu9gi08cf").product(productRepository.findById(6).get()).build(),
                        ProductImage.builder().imageUrl("https://cdn.shopify.com/s/files/1/0643/2148/7095/products/RG-Nu-Gundam-8.webp?v=1671556536").product(productRepository.findById(6).get()).build(),
                        ProductImage.builder().imageUrl("https://www.fumetto-online.it/ew/ew_albi/images/BANDAI/82254%20RG%2037%20GUNDAM%20GOD.jpg").product(productRepository.findById(7).get()).build(),
                        ProductImage.builder().imageUrl("https://cn.lnwfile.com/_/cn/_raw/8c/d2/lu.jpg").product(productRepository.findById(7).get()).build(),
                        ProductImage.builder().imageUrl("https://www.static-src.com/wcsstore/Indraprastha/images/catalog/full/catalog-image/MTA-72702893/bandai_bandai_gundam_entry_grade_1-144_rx_78-2__gundam_mainan_anak_full01_jzaxotkw.jpeg").product(productRepository.findById(8).get()).build(),
                        ProductImage.builder().imageUrl("http://www.xmodeltoys.com/photo/1675-12.jpg").product(productRepository.findById(8).get()).build(),
                        ProductImage.builder().imageUrl("https://www.gundam.my/detailimage/big/6838/image_6838.jpg").product(productRepository.findById(9).get()).build(),
                        ProductImage.builder().imageUrl("https://www.gundam.my/images/sell_products/interactive/6838/7.jpg").product(productRepository.findById(9).get()).build(),
                        ProductImage.builder().imageUrl("https://down-th.img.susercontent.com/file/cn-11134207-7r98o-lvbi8xtglho1b4").product(productRepository.findById(10).get()).build(),
                        ProductImage.builder().imageUrl("https://www.gundam.co.nz/wp-content/uploads/2016/06/mg-zgmf-x10a-freedom-gundam-ver-2-0-06_1.jpg").product(productRepository.findById(10).get()).build(),
                        ProductImage.builder().imageUrl("https://down-ph.img.susercontent.com/file/ph-11134201-23030-lauig56uciov4d").product(productRepository.findById(11).get()).build(),
                        ProductImage.builder().imageUrl("https://www.rioxteir.com/wp-content/uploads/2018/10/MG-Sinanju-Stein-NARRATIVE-Version-02.jpg").product(productRepository.findById(11).get()).build(),
                        ProductImage.builder().imageUrl("https://down-th.img.susercontent.com/file/b3f8e7691906de4ebf4ca63bcf834ee2").product(productRepository.findById(12).get()).build(),
                        ProductImage.builder().imageUrl("https://cdn.ecommercedns.uk/files/4/254074/1/29203611/image.png").product(productRepository.findById(12).get()).build(),
                        ProductImage.builder().imageUrl("https://www.picclickimg.com/cAIAAOSw9yhnlOBG/Bandai-MG-Gundam-Kai-Model-Kit-1-100-Scale.webp").product(productRepository.findById(13).get()).build(),
                        ProductImage.builder().imageUrl("https://www.gundam.my/images/sell_products/interactive/5701/1.jpg").product(productRepository.findById(13).get()).build(),
                        ProductImage.builder().imageUrl("https://www.gunjap.net/site/wp-content/uploads/2019/07/FAFA1AE6-D026-4FD3-8AF6-C6779B022EB2.jpeg").product(productRepository.findById(14).get()).build(),
                        ProductImage.builder().imageUrl("https://3.bp.blogspot.com/-V16hL8afzCQ/XNIO5WC6kZI/AAAAAAAHJTI/80JjYhfN_KQJQQq_Iihe4qBIt25tgcMHACLcBGAs/s1600/rg-tallgeese-III%2B%25282%2529.jpg").product(productRepository.findById(14).get()).build(),
                        ProductImage.builder().imageUrl("https://media.karousell.com/media/photos/products/2022/9/20/pgu_rx782_perfect_grade_unleas_1663691958_8b3bed0b_progressive.jpg").product(productRepository.findById(15).get()).build(),
                        ProductImage.builder().imageUrl("https://www.gundam.co.nz/wp-content/uploads/2020/10/PG-Unleashed-RX-78-2-Gundam_2.jpg").product(productRepository.findById(15).get()).build(),
                        ProductImage.builder().imageUrl("https://down-my.img.susercontent.com/file/289d3ba74111d05832decea1e75aa99a").product(productRepository.findById(16).get()).build(),
                        ProductImage.builder().imageUrl("https://hobbygundamusa.com/cdn/shop/files/mega-size-1-48-rx-0-unicorn-gundam-destroy-mode-3.webp?v=1731177060&width=1445").product(productRepository.findById(16).get()).build(),
                        ProductImage.builder().imageUrl("https://media.karousell.com/media/photos/products/2022/8/21/gundam_world_haropla_haro_basi_1661105328_12655a83_progressive").product(productRepository.findById(17).get()).build(),
                        ProductImage.builder().imageUrl("https://www.gundam.my/images/sell_products/interactive/5248/10.jpg").product(productRepository.findById(17).get()).build(),
                        ProductImage.builder().imageUrl("https://media.karousell.com/media/photos/products/2023/5/22/hirm_1100_wing_gundam_zero_ew__1684719991_cb4e1b69.jpg").product(productRepository.findById(18).get()).build(),
                        ProductImage.builder().imageUrl("https://media.karousell.com/media/photos/products/2023/5/22/hirm_1100_wing_gundam_zero_ew__1684719991_cb4e1b69.jpg").product(productRepository.findById(18).get()).build(),
                        ProductImage.builder().imageUrl("https://media.karousell.com/media/photos/products/2021/5/13/30_minutes_mission_30mm_1144_e_1620893615_aa3e65bc_progressive.jpg").product(productRepository.findById(19).get()).build(),
                        ProductImage.builder().imageUrl("https://gundampros.com/wp-content/uploads/2020/05/30mm-01-eexm-17-alto-white-7.jpg").product(productRepository.findById(19).get()).build(),
                        ProductImage.builder().imageUrl("https://cdn11.bigcommerce.com/s-89ffd/images/stencil/532x532/products/2509/5450/1__69347.1327663923.jpg?c=2").product(productRepository.findById(20).get()).build(),
                        ProductImage.builder().imageUrl("https://cdn11.bigcommerce.com/s-89ffd/images/stencil/532x532/products/2509/5450/1__69347.1327663923.jpg?c=2").product(productRepository.findById(20).get()).build()
                ));
            }

        };
    }


}
