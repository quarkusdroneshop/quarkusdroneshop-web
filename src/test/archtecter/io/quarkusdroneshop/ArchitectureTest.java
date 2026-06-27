package io.quarkusdroneshop;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * ArchUnit によるアーキテクチャ適合性テスト。
 * パッケージ構造:
 *   io.quarkusdroneshop.domain.*          - ドメイン層 (POJO / イベント / 値オブジェクト)
 *   io.quarkusdroneshop.web.domain.*      - Web ドメイン層 (コマンド / DTO)
 *   io.quarkusdroneshop.web.infrastructure - インフラ層 (REST / Kafka / シリアライズ)
 */
@AnalyzeClasses(
        packages = "io.quarkusdroneshop",
        importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    // =========================================================================
    // 1. レイヤー間依存ルール
    // =========================================================================

    /**
     * web レイヤー全体へのアクセスは web・domain パッケージのみ許可。
     * (元から存在するルール)
     */
    @ArchTest
    static final ArchRule レイヤー間依存チェック =
        classes()
            .that().resideInAPackage("..web..")
            .should().onlyBeAccessed()
            .byAnyPackage("..web..", "..domain..");

    /**
     * ドメイン層 (io.quarkusdroneshop.domain) は
     * web パッケージに依存してはならない。
     * ※ web.domain.commands は "web" に属するため除外対象。
     */
    @ArchTest
    static final ArchRule ドメイン層はWebに依存しない =
        noClasses()
            .that().resideInAPackage("io.quarkusdroneshop.domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("io.quarkusdroneshop.web..");

    /**
     * ドメイン層は Jakarta JAX-RS アノテーションを使用しない。
     * (REST の関心事は infrastructure に閉じる)
     */
    @ArchTest
    static final ArchRule ドメイン層はJAX_RSを使用しない =
        noClasses()
            .that().resideInAPackage("io.quarkusdroneshop.domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("jakarta.ws.rs..");

    /**
     * 値オブジェクト層は web パッケージに依存しない。
     */
    @ArchTest
    static final ArchRule 値オブジェクト層はWebに依存しない =
        noClasses()
            .that().resideInAPackage("io.quarkusdroneshop.domain.valueobjects..")
            .should().dependOnClassesThat()
            .resideInAPackage("io.quarkusdroneshop.web..");

    /**
     * commands パッケージは infrastructure パッケージに依存しない。
     */
    @ArchTest
    static final ArchRule コマンドはInfrastructureに依存しない =
        noClasses()
            .that().resideInAPackage("..commands..")
            .should().dependOnClassesThat()
            .resideInAPackage("..infrastructure..");

    // =========================================================================
    // 2. 命名規則
    // =========================================================================

    /**
     * Kafka Deserializer 実装クラスは名前が "Deserializer" で終わること。
     */
    @ArchTest
    static final ArchRule Deserializer命名規則 =
        classes()
            .that().implement("org.apache.kafka.common.serialization.Deserializer")
            .or().areAssignableTo(
                io.quarkus.kafka.client.serialization.ObjectMapperDeserializer.class)
            .should().haveSimpleNameEndingWith("Deserializer");

    /**
     * Deserializer クラスは infrastructure パッケージに配置されること。
     */
    @ArchTest
    static final ArchRule Deserializerはinfrastructureに配置 =
        classes()
            .that().haveSimpleNameEndingWith("Deserializer")
            .should().resideInAPackage("..web.infrastructure..");

    /**
     * コマンドクラスは commands パッケージに配置されること。
     */
    @ArchTest
    static final ArchRule コマンドはCommandsパッケージに配置 =
        classes()
            .that().haveSimpleNameEndingWith("Command")
            .should().resideInAPackage("..commands..");

    /**
     * 例外クラスは名前が "Exception" で終わること。
     */
    @ArchTest
    static final ArchRule 例外クラスの命名規則 =
        classes()
            .that().areAssignableTo(Exception.class)
            .and().resideInAPackage("io.quarkusdroneshop..")
            .should().haveSimpleNameEndingWith("Exception");

    /**
     * REST エンドポイントクラスは名前が "Resource" で終わること。
     */
    @ArchTest
    static final ArchRule RESTリソースの命名規則 =
        classes()
            .that().areAnnotatedWith(jakarta.ws.rs.Path.class)
            .and().resideInAPackage("io.quarkusdroneshop..")
            .should().haveSimpleNameEndingWith("Resource");

    /**
     * REST リソースクラスは public であること。
     */
    @ArchTest
    static final ArchRule RESTリソースはPublic =
        classes()
            .that().haveSimpleNameEndingWith("Resource")
            .and().resideInAPackage("..infrastructure..")
            .should().bePublic();

    /**
     * REST リソースクラスは @Path アノテーションを持つこと。
     */
    @ArchTest
    static final ArchRule RESTリソースは_Pathアノテーションを持つ =
        classes()
            .that().haveSimpleNameEndingWith("Resource")
            .and().resideInAPackage("..infrastructure..")
            .should().beAnnotatedWith(jakarta.ws.rs.Path.class);

    // =========================================================================
    // 3. パッケージ配置ルール
    // =========================================================================

    /**
     * JsonUtil は infrastructure パッケージに配置されること。
     */
    @ArchTest
    static final ArchRule JsonUtilはInfrastructureに配置 =
        classes()
            .that().haveSimpleName("JsonUtil")
            .should().resideInAPackage("..web.infrastructure..");

    /**
     * Event インタフェースの実装は domain パッケージに配置されること。
     */
    @ArchTest
    static final ArchRule Eventの実装はDomainに配置 =
        classes()
            .that().implement("io.quarkusdroneshop.domain.Event")
            .should().resideInAPackage("io.quarkusdroneshop.domain..");

    /**
     * 値オブジェクト (OrderResult 実装) は valueobjects パッケージに配置されること。
     */
    @ArchTest
    static final ArchRule OrderResult実装はValueObjectsに配置 =
        classes()
            .that().implement("io.quarkusdroneshop.domain.valueobjects.OrderResult")
            .should().resideInAPackage("..valueobjects..");

    // =========================================================================
    // 4. 循環依存
    // =========================================================================

    /**
     * トップレベルパッケージ間に循環依存が存在しないこと。
     */
    @ArchTest
    static final ArchRule パッケージ間循環依存なし =
        slices()
            .matching("io.quarkusdroneshop.(*)..")
            .should().beFreeOfCycles();

    // =========================================================================
    // 5. クラス設計ルール
    // =========================================================================

    /**
     * ドメイン層のクラスは public であること (パッケージ外から利用される)。
     */
    @ArchTest
    static final ArchRule ドメインクラスはPublic =
        classes()
            .that().resideInAPackage("io.quarkusdroneshop.domain")
            .and().areNotInterfaces()
            .should().bePublic();

    /**
     * infrastructure のクラスは infrastructure または外部ライブラリのみに依存する。
     * (reactivestreams / resteasy を許可リストに含む)
     */
    @ArchTest
    static final ArchRule Infrastructureの依存範囲チェック =
        classes()
            .that().resideInAPackage("..web.infrastructure..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "..web.infrastructure..",
                "..web.domain..",
                "io.quarkusdroneshop.domain..",
                "java..",
                "javax..",
                "jakarta..",
                "io.quarkus..",
                "io.smallrye..",
                "org.eclipse.microprofile..",
                "org.apache.kafka..",
                "com.fasterxml..",
                "org.slf4j..",
                "org.jboss..",
                "org.reactivestreams..");
}
